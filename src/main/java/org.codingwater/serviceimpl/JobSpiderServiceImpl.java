package org.codingwater.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.codingwater.model.LagouJobInfo;
import org.codingwater.service.IJobSpiderService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Created by water on 4/19/16.
 */
@Service
public class JobSpiderServiceImpl implements IJobSpiderService {


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
    logger.info(queryUrl);
    Document doc = null;
    try {
      doc = Jsoup.connect(queryUrl).ignoreContentType(true).timeout(10 * 1000).get();
    } catch (IOException e) {
      logger.error("connect error while try to fetch from lagou.com", e);
    }
    if (doc == null) {
      logger.info("请求异常, 抓取数据为空");
      return new ArrayList<>();
    }
    System.out.println(doc.body().text());

    Map<String, Object> resultMap =  null;
    Map<String, Object> contentMap = null;
    List<Map<String, Object>> jobIndoList = null;
    try {
      resultMap = mapper.readValue(doc.body().text(), Map.class);
      contentMap = (Map<String, Object>) resultMap.get("content");
      jobIndoList = (List<Map<String, Object>>) contentMap.get("result");
    } catch (Exception e) {
      logger.error("data transfor error.", e);
    }

    List<LagouJobInfo> lagouJobInfoList = new ArrayList<>();
    if (jobIndoList == null) {
      return lagouJobInfoList;
    }
    for (Map<String, Object> job : jobIndoList) {
      LagouJobInfo lagouJobInfo = modelMapper.map(job, LagouJobInfo.class);
      lagouJobInfo.setDetailPage(String.format("http://www.lagou.com/jobs/%s.html", lagouJobInfo
          .getPositionId()));
      lagouJobInfoList.add(lagouJobInfo);
    }
    return lagouJobInfoList;
  }
}
