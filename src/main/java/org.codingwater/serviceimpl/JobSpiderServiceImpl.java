package org.codingwater.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.codingwater.concurrency.FetchThread;
import org.codingwater.dao.BaseJobInfoDAO;
import org.codingwater.model.LagouJobInfo;
import org.codingwater.model.NeituiJobInfo;
import org.codingwater.service.IJobSpiderService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Created by water on 4/19/16.
 */
@Service
public class JobSpiderServiceImpl implements IJobSpiderService {

  @Autowired
  BaseJobInfoDAO baseJobInfoDAO;

  private ObjectMapper mapper = new ObjectMapper();

  @Autowired
  private ModelMapper modelMapper;

  Logger logger = LoggerFactory.getLogger("spiderLogger");

  @Override
  public List<LagouJobInfo> fetchJobInfosFromLagou(String city, String keyword,
      int pageNumber, String monthlySalary, String workYears) {

    if (city == null) {
      city = "";
    }
    if (keyword == null) {
      keyword = "";
    }
    if (pageNumber == 0) {
      pageNumber = 1;
    }
    if (monthlySalary == null) {
      monthlySalary = "";
    }
    if (workYears == null) {
      workYears = "";
    }
    //拉勾抓数据的url.
    String queryUrl = String.format("http://www.lagou.com/jobs/positionAjax.json"
        + "?city=%s&first=true&px=new&pn=%d&kd=%s&gj=%s&yx=%s",
        city, pageNumber, keyword, workYears, monthlySalary);

    String resultData = fetchWithCondition(queryUrl);
    List<LagouJobInfo> lagouJobInfoList = getJobListFromJson(resultData);
    
    return lagouJobInfoList;
  }

  @Override
  public List<NeituiJobInfo> fetchJobInfosFromNeitui(String city, String keyword, int pageNumber,
      String monthlySalary, String workYears) {

    if (city == null) {
      city = "";
    }
    if (keyword == null) {
      keyword = "";
    }
    if (pageNumber == 0) {
      pageNumber = 1;
    }
    if (monthlySalary == null) {
      monthlySalary = "";
    }
    //todo 可能需要做转换.
    if (workYears == null) {
      workYears = "";
    }
//    http://www.neitui.me/?name=neitui&handle=lists&kcity=%E5%85%A8%E5%9B%BD&keyword=Java&workage=2&page=1&payrange=5-10
    //抓内推数据的url.

    String neiTuiUrl = String.format("http://www.neitui.me/"
        + "?name=neitui&handle=lists&keyword=%s&kcity=%s&page=%d&workage=%s&payrange=%s",
        keyword, city, pageNumber, workYears, monthlySalary);
    logger.info(neiTuiUrl);

    Elements joblis = getLisFromDoc(neiTuiUrl);
    List<NeituiJobInfo> neituiJobInfos = getJobListFromElements(joblis);

    return neituiJobInfos;
  }

  private List<NeituiJobInfo> getJobListFromElements(Elements joblis) {

    List<NeituiJobInfo> ret = Lists.newArrayList();
    for (Element job : joblis) {
      NeituiJobInfo neituiJob = new NeituiJobInfo();

      try {
        String createTime = job.getElementsByAttributeValue("class", "createtime").text();
        Element elementLeft = job.getElementsByAttributeValue("class", "jobnote-l").get(0);
        String positionId = elementLeft.getElementsByTag("a").get(0).attr("href").split("/")[2];
        String positionName =
            elementLeft.getElementsByAttributeValue("class", "padding-r10").get(0).text();
        String city = elementLeft.getElementsByAttributeValue("class", "padding-r10").get(1).text();
        String monthSalary = elementLeft.getElementsByAttributeValue("class", "padding-r10").get(2)
            .text();
        String workYear = elementLeft.getElementsByAttributeValue("class", "padding-r10").get(3)
            .text();
        Element elementRight = job.getElementsByAttributeValue("class", "jobnote-r").get(0);
        String companyName = elementRight.getElementsByAttributeValue("class", "padding-r10").get(0)
            .text();


        neituiJob.setPositionId("N" + positionId);
        neituiJob.setDetailPage("http://www.neitui.me/j/" + positionId);
        neituiJob.setPositionName(positionName);
        neituiJob.setCity(removeBrackets(city));
        neituiJob.setSalary(addUintForNeituiSalary(removeBrackets(monthSalary)));
        neituiJob.setWorkYear(removeBrackets(workYear));
        neituiJob.setCompanyName(companyName);
        // set time
        createTime = wrapCreateTimeForNeitui(createTime);
        neituiJob.setCreateTime(createTime);
        neituiJob.setCreateTimeSort(Timestamp.valueOf(createTime).getTime());

        neituiJob.setEducation("");
        neituiJob.setIndustryField("");
      } catch (Exception e) {
        System.out.println("跳过一条不合法的数据");
      }

      System.out.println(neituiJob);
      ret.add(neituiJob);
    }
    return ret;
  }

  private String wrapCreateTimeForNeitui(String createTime) {
    //暂时将内推数据全部设置为16年每天8点钟发布.
    return "2016-" + createTime + " 08:00:00";

  }

  private String addUintForNeituiSalary(String salary) {
    return salary.replace("-", "k-");
  }

  private String removeBrackets(String originStr) {
    return originStr.replace("[", "").replace("]", "");
  }

  private Elements getLisFromDoc(String neiTuiUrl) {

    Document doc = null;
    try {
      doc = Jsoup.connect(neiTuiUrl).ignoreContentType(true).timeout(10 * 1000).get();
    } catch (IOException e) {
      logger.error("connect error while try to fetch from neitui.me", e);
    }
    if (doc == null) {
      logger.info("请求异常, 抓取数据为空");
      return new Elements();
    }
    return doc.getElementsByAttributeValue("class", "jobinfo brjob clearfix");
  }


  public List<LagouJobInfo> getJobListFromJson(String resultData) {
    List<LagouJobInfo> ret = Lists.newArrayList();

    Map<String, Object> resultMap =  null;
    Map<String, Object> contentMap = null;
    List<Map<String, Object>> jobIndoList = null;
    try {
      //解析api数据
      resultMap = mapper.readValue(resultData, Map.class);
      contentMap = (Map<String, Object>) resultMap.get("content");
      jobIndoList = (List<Map<String, Object>>) contentMap.get("result");
    } catch (Exception e) {
      logger.error("data transfor error.", e);
    }

    if (jobIndoList == null) {
      return ret;
    }
    for (Map<String, Object> job : jobIndoList) {
      LagouJobInfo lagouJobInfo = modelMapper.map(job, LagouJobInfo.class);
      lagouJobInfo.setDetailPage(String.format("http://www.lagou.com/jobs/%s.html",
          lagouJobInfo.getPositionId()));
      lagouJobInfo.setPositionId("L" + lagouJobInfo.getPositionId());
      ret.add(lagouJobInfo);
    }
    return ret;
  }

  @Override
  public void fetchYesterdayDataFromLagou(String keyword) {
    //根据时间戳,抓取昨天的数据.

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    //获取今日凌晨时间戳
    long todayMidNightTimeStamp = cal.getTimeInMillis();

    //获取昨日凌晨时间戳
    cal.add(Calendar.DATE, -1);
    long yesterdayMidNightTimeStamp = cal.getTimeInMillis();

    int pageNumber = 1;
    boolean isContinue = true;
    Predicate<LagouJobInfo> predicate = p -> p.getCreateTimeSort() > yesterdayMidNightTimeStamp
        && p.getCreateTimeSort() < todayMidNightTimeStamp;

    List<LagouJobInfo> yesterdayJobList = Lists.newArrayList();
    while (isContinue) {
      String queryUrl = String.format("http://www.lagou.com/jobs/positionAjax.json"
          + "?first=true&px=new&kd=%s&pn=%d", keyword, pageNumber);

      String resultData = fetchWithCondition(queryUrl);
      List<LagouJobInfo> lagouJobInfoList = getJobListFromJson(resultData);

      if (CollectionUtils.isEmpty(lagouJobInfoList)) {
        //retry 5 times
        for (int i = 0; i < 5 ; i ++) {
          System.out.println("sleep 10s");

          try {
            Thread.sleep(10000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          System.out.println("retry :" + queryUrl);
          lagouJobInfoList = getJobListFromJson(fetchWithCondition(queryUrl));
          if (lagouJobInfoList.size() > 0) {
            break;
          }
        }
        if (lagouJobInfoList.size() == 0) {
          System.out.println("failed");
          return;
        }

      }

      //过滤
      List<LagouJobInfo> filtedList = lagouJobInfoList.stream().filter(predicate)
          .collect(Collectors.toList());

      filtedList.forEach(job -> System.out.println(
          job.getCompanyName() + ":" + job.getCity() + ":"
              + job.getCreateTime() + ":" + job.getCreateTimeSort()));
      yesterdayJobList.addAll(filtedList);
      //存入数据库
      saveToDataBase(filtedList);

      LagouJobInfo lastOne = Iterables.getLast(lagouJobInfoList);
      if (lastOne.getCreateTimeSort() < yesterdayMidNightTimeStamp) {
        isContinue = false;
      }
      pageNumber ++ ;
    }

    System.out.println("==============over===================");
    System.out.println("本次爬取数据:" + yesterdayJobList.size());

  }

  @Override
  public void multiThreadFetch(String keyword) {

    Thread fetchThreadOne = new Thread(new FetchThread(keyword, 1, 100, this));
    Thread fetchThreadTwo = new Thread(new FetchThread(keyword, 101, 100, this));
    Thread fetchThreadThree = new Thread(new FetchThread(keyword, 201, 100, this));
    Thread fetchThreadFour = new Thread(new FetchThread(keyword, 301, 100, this));

    fetchThreadOne.start();
    fetchThreadTwo.start();
    fetchThreadThree.start();
    fetchThreadFour.start();

  }

  public void saveToDataBase(List<LagouJobInfo> yesterdayJobList) {
    for (LagouJobInfo job : yesterdayJobList) {
      if (baseJobInfoDAO.findPositionById(job.getPositionId()) == null) {
        baseJobInfoDAO.savePosition(job);
      }
    }
  }



  @Override
  public void fetchAndSaveDataFromNeitui(String keyword) {

    int pageNumber = 1;
    boolean isContinue = true;

    while (isContinue) {

      String neituiQueryUrl = String.format("http://www.neitui.me/index"
          + ".php?name=neitui&handle=lists&page=%d"
          + "&kcity=全国&keyword=%s", pageNumber, keyword);

      Elements joblis = getLisFromDoc(neituiQueryUrl);
      if (joblis.size() == 0) {
        System.out.println("没有数据了");
        break;
      }
      List<NeituiJobInfo> neituiJobInfos = getJobListFromElements(joblis);
      saveNeituiJobInfo(neituiJobInfos);
      pageNumber ++;
      //neitui只允许爬去30页的数据
      if (pageNumber > 30) {
        isContinue = false;
      }
    }
  }

  private void saveNeituiJobInfo(List<NeituiJobInfo> neituiJobInfos) {
    for (NeituiJobInfo job : neituiJobInfos) {
      if (!StringUtils.isEmpty(job.getPositionId())) {
        if (baseJobInfoDAO.findPositionById(job.getPositionId()) == null) {
          baseJobInfoDAO.savePosition(job);
        }
      }
    }


  }

  public String fetchWithCondition(String queryUrl) {
    logger.info(queryUrl);

    Document doc = null;
    try {
      doc = Jsoup.connect(queryUrl).ignoreContentType(true).timeout(10 * 1000).get();
    } catch (IOException e) {
      logger.error("connect error while try to fetch from lagou.com", e);
    }
    if (doc == null) {
      logger.info("请求异常, 抓取数据为空");
      return "";
    }
    return doc.body().text();
  }
}
