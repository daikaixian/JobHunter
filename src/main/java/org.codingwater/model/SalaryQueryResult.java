package org.codingwater.model;

/**
 * Created by water on 4/27/16.
 */
public class SalaryQueryResult {
  private String keyword;
  private String city;
  private String workYear;

  private int totalCount;
  private double averageSalary; //单位 k.


  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getWorkYear() {
    return workYear;
  }

  public void setWorkYear(String workYear) {
    this.workYear = workYear;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }

  public double getAverageSalary() {
    return averageSalary;
  }

  public void setAverageSalary(double averageSalary) {
    this.averageSalary = averageSalary;
  }
}
