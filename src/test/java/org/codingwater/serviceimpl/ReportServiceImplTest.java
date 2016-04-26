package org.codingwater.serviceimpl;

import org.codingwater.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.omg.CORBA.BAD_POLICY;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by water on 4/26/16.
 */
public class ReportServiceImplTest extends BaseTest {

  @Autowired
  ReportServiceImpl reportService;

  @Test
  public void testCalculateAverageSalary() throws Exception {

    String city = "上海";
    String keyword = "Java";
    String workYear = "1-3年";
    reportService.calculateAverageSalary(city, keyword, workYear);

//    System.out.println(reportS);
  }

  @Test
  public void testGetIntSalaryFromString() throws Exception {

    Assert.assertEquals(4.0, reportService.getIntSalaryFromString("3k-5k"), 0); //4.0
    Assert.assertEquals(6.5,reportService.getIntSalaryFromString("3k-10k"), 0); //6.5
    Assert.assertEquals(15.0,reportService.getIntSalaryFromString("12k-18k"), 0); //15.0
    System.out.println(reportService.getIntSalaryFromString("3-5k")); // 0.0
    System.out.println(reportService.getIntSalaryFromString("3k-5")); // 0.0
    System.out.println(reportService.getIntSalaryFromString("3-5")); // 0.0
    System.out.println(reportService.getIntSalaryFromString("5k以上")); // 0

  }

}