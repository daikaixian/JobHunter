package org.codingwater.service;

import org.codingwater.model.SalaryQueryResult;

/**
 * Created by water on 4/25/16.
 */
public interface IReportService {

  SalaryQueryResult calculateAverageSalary(String city, String keyword, String workYear);




}
