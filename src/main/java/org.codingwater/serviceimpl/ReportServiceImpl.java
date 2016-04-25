package org.codingwater.serviceimpl;

import org.codingwater.dao.BaseJobInfoDAO;
import org.codingwater.model.BaseJobInfo;
import org.codingwater.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by water on 4/25/16.
 */
@Service
public class ReportServiceImpl implements IReportService {

  @Autowired
  BaseJobInfoDAO baseJobInfoDAO;

  @Override
  public String calculateAverageSalary(String city, String keyword, String workYear) {

    List<BaseJobInfo> jobInfoList =
        baseJobInfoDAO.queryPositionsWithCondition(city, keyword, workYear);
    return null;
  }
}
