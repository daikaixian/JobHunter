package org.codingwater.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.codingwater.dao.BaseJobInfoDAO;
import org.codingwater.model.BaseJobInfo;
import org.codingwater.model.LagouJobInfo;
import org.codingwater.service.IJobSpiderService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
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

  private List<LagouJobInfo> getJobListFromJson(String resultData) {
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
        System.out.println("no data fetched:" + queryUrl);
        return;
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

  private void saveToDataBase(List<LagouJobInfo> yesterdayJobList) {
    for (LagouJobInfo job : yesterdayJobList) {
      if (baseJobInfoDAO.findPositionById(job.getPositionId()) == null) {
        baseJobInfoDAO.savePosition(job);
      }
    }


  }

  private String fetchWithCondition(String queryUrl) {
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
