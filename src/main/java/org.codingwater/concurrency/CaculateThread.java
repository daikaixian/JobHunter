package org.codingwater.concurrency;

import org.codingwater.model.SalaryQueryResult;
import org.codingwater.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Callable;

/**
 * Created by water on 4/27/16.
 */
public class CaculateThread implements Callable<SalaryQueryResult> {

  IReportService reportService;

  private String city;
  private String keyword;
  private String workYear;

  public CaculateThread(String city, String keyword, String workyear, IReportService
      reportService) {

    this.city = city;
    this.keyword = keyword;
    this.workYear = workyear;
    this.reportService = reportService;
  }

  public CaculateThread(){

  }

  @Override
  public SalaryQueryResult call() throws Exception {
//    return 1;
    return reportService.calculateAverageSalary(city, keyword, workYear);
  }
}
