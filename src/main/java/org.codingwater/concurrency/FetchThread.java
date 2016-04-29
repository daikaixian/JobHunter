package org.codingwater.concurrency;

import org.codingwater.model.LagouJobInfo;
import org.codingwater.service.IJobSpiderService;
import org.codingwater.serviceimpl.JobSpiderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by water on 4/26/16.
 */
public class FetchThread implements Runnable{

  private String keyword;
  private int startPageNo;
  private int limit;

  IJobSpiderService jobSpiderService ;

  public FetchThread(String keyword, int startPageNo, int limit,
      JobSpiderServiceImpl jobSpiderService) {
    this.keyword = keyword;
    this.startPageNo = startPageNo;
    this.limit = limit;
    this.jobSpiderService = jobSpiderService;
  }

  @Override
  public void run() {
    String threadName = Thread.currentThread().getName();
    System.out.println("Thread :" + threadName + " start");
    //根据时间戳,抓取昨天的数据.

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    //获取今日凌晨时间戳
    long todayMidNightTimeStamp = cal.getTimeInMillis();

    //获取截止日凌晨时间戳
    cal.add(Calendar.DATE, -14);
    long enddayMidNightTimeStamp = cal.getTimeInMillis();

    Predicate<LagouJobInfo> predicate = p -> p.getCreateTimeSort() > enddayMidNightTimeStamp
        && p.getCreateTimeSort() < todayMidNightTimeStamp;

    for (int pageNumber = startPageNo; pageNumber < startPageNo + limit; pageNumber ++) {

      String queryUrl = String.format("http://www.lagou.com/jobs/positionAjax.json"
          + "?first=true&px=new&kd=%s&pn=%d", keyword, pageNumber);
      String resultData = jobSpiderService.fetchWithCondition(queryUrl);
      List<LagouJobInfo> lagouJobInfoList = jobSpiderService.getJobListFromJson(resultData);

      List<LagouJobInfo> filtedList = lagouJobInfoList.stream().filter(predicate)
          .collect(Collectors.toList());

      filtedList.forEach(job -> System.out.println(
          threadName + ":" + job.getCompanyName() + ":" + job.getCity() + ":"
              + job.getCreateTime() + ":" + job.getCreateTimeSort()));
      //存入数据库
      jobSpiderService.saveToDataBase(filtedList);
    }
    System.out.println("Thread :" + threadName + " over");
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public int getStartPageNo() {
    return startPageNo;
  }

  public void setStartPageNo(int startPageNo) {
    this.startPageNo = startPageNo;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }
}
