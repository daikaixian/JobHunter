package org.codingwater.model;

/**
 * Created by water on 4/14/16.
 */
public class BaseJobInfo {

  private String positionId;
  private String companyName;
  private String positionName;
  private String salary;
  private String workYear;
  private String city;
  private String createTime;
  private Long createTimeSort;
  private String education;
  private String industryField;

  private Integer infoSource;
  private String detailPage;

  public String getDetailPage() {
    return detailPage;
  }

  public void setDetailPage(String detailPage) {
    this.detailPage = detailPage;
  }

  public String getPositionId() {
    return positionId;
  }

  public void setPositionId(String positionId) {
    this.positionId = positionId;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getPositionName() {
    return positionName;
  }

  public void setPositionName(String positionName) {
    this.positionName = positionName;
  }

  public String getSalary() {
    return salary;
  }

  public void setSalary(String salary) {
    this.salary = salary;
  }

  public String getWorkYear() {
    return workYear;
  }

  public void setWorkYear(String workYear) {
    this.workYear = workYear;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getEducation() {
    return education;
  }

  public void setEducation(String education) {
    this.education = education;
  }

  public String getIndustryField() {
    return industryField;
  }

  public void setIndustryField(String industryField) {
    this.industryField = industryField;
  }

  public Integer getInfoSource() {
    return infoSource;
  }

  public void setInfoSource(Integer infoSource) {
    this.infoSource = infoSource;
  }

  public Long getCreateTimeSort() {
    return createTimeSort;
  }

  public void setCreateTimeSort(Long createTimeSort) {
    this.createTimeSort = createTimeSort;
  }

  @Override
  public String toString() {
    return "BaseJobInfo{"
        + "positionId=" + positionId
        + ", companyName='" + companyName + '\''
        + ", positionName='" + positionName + '\''
        + ", salary='" + salary + '\''
        + ", workYear='" + workYear + '\''
        + ", city='" + city + '\''
        + ", createTime='" + createTime + '\''
        + ", createTimeSort='" + createTimeSort + '\''
        + ", education='" + education + '\''
        + ", industryField='" + industryField + '\''
        + ", infoSource=" + infoSource
        + '}';
  }
}
