package org.codingwater.serviceimpl;

import org.codingwater.dao.BaseJobInfoDAO;
import org.codingwater.model.BaseJobInfo;
import org.codingwater.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jca.cci.core.InteractionCallback;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by water on 4/25/16.
 */
@Service
public class ReportServiceImpl implements IReportService {

  @Autowired
  BaseJobInfoDAO baseJobInfoDAO;

  /**
   * 计算平均工资
   * @param city .
   * @param keyword .
   * @param workYear .
   * @return 计算出来的平均工资.
   */
  @Override
  public String calculateAverageSalary(String city, String keyword, String workYear) {

    //最终能得出的结论是:在所选城市,所属工种,以及所属工资经验中,查询了多少条数据,得出的平均工资是多少.
    //比如,上海,Java,1-3年,查到多少数据.平均工资多少
    List<BaseJobInfo> jobInfoList =
        baseJobInfoDAO.queryPositionsWithCondition(city, keyword, workYear);

    System.out.println("begins at : " + Calendar.getInstance().getTimeInMillis());
    int totalCount = jobInfoList.size();
    double sum = 0; //单位为k
    for (BaseJobInfo jobInfo : jobInfoList) {
      double salary  = getIntSalaryFromString(jobInfo.getSalary());  //单位为k.
      if (salary > 0 ) {
        sum += salary;
      } else {
        totalCount --;
      }
    }

    double averageSalary = sum / totalCount;
    averageSalary = (Math.round(averageSalary * 100) / 100.0);
//    averageSalary = ((int)(averageSalary * 100))/100;
    System.out.println("加入平均值计算的职位一共" + totalCount + "个, 平均工资为:" + averageSalary + "k");
    System.out.println("over at : " + Calendar.getInstance().getTimeInMillis());
    return null;
  }

  public double getIntSalaryFromString(String salary) {
    salary = salary.toLowerCase(); // 先全部换成小写
    double ret = 0;
    String pattern = "\\d+k-\\d+k";   // 3k-5k,9k-12k, 30-50k. etc.
    boolean isMatch = Pattern.matches(pattern, salary);
    if (!isMatch) {
      return 0;
    } else {
      String [] salaryArray = salary.replace("k", "").split("-");
      double sum = Integer.parseInt(salaryArray[0]) + Integer.parseInt(salaryArray[1]);
      ret = sum / 2;

    }
    return ret;
  }
}
