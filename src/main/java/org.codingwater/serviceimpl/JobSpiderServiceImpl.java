package org.codingwater.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.codingwater.model.LagouJobInfo;
import org.codingwater.service.IJobSpiderService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by water on 4/19/16.
 */
public class JobSpiderServiceImpl implements IJobSpiderService {

  //todo  need change to Autowired.
  private ObjectMapper mapper = new ObjectMapper();
  private ModelMapper modelMapper = new ModelMapper();

  Logger logger = LoggerFactory.getLogger(JobSpiderServiceImpl.class);

  @Override
  public List<LagouJobInfo> fetchJobInfosFromLagou(String city, String keyword,
      int pageNumber, String monthlySalary, String workYears) {

    //拉勾抓数据的url.
    String queryUrl = String.format("http://www.lagou.com/jobs/positionAjax.json"
        + "?city=%s&first=false&pn=%d&kd=%s&gj=%s&yx=%s",
        city, pageNumber, keyword, workYears, monthlySalary);
    logger.info(queryUrl);
    Document doc = null;
    try {
      doc = Jsoup.connect(queryUrl).ignoreContentType(true).get();
    } catch (IOException e) {
      //todo logger
      logger.error("connect error while try to fetch from lagou.com", e);
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
      //todo logger
      logger.error("data transfor error.", e);
    }
    System.out.println("joblist.size = " + jobIndoList.size());

    List<LagouJobInfo> lagouJobInfoList = new ArrayList<>();
    for (Map<String, Object> job : jobIndoList) {
      LagouJobInfo lagouJobInfo = modelMapper.map(job, LagouJobInfo.class);
      lagouJobInfoList.add(lagouJobInfo);
      System.out.println(lagouJobInfo);
    }
    return lagouJobInfoList;
  }
}
