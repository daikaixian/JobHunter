package org.codingwater.controller;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.codingwater.model.LagouJobInfo;
import org.codingwater.model.apiresp.APIListResponseDTO;
import org.codingwater.service.IJobSpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

/**
 * Created by water on 4/19/16.
 */

@Controller
public class SearchOnlineController {

  @Autowired
  private IJobSpiderService jobSpiderService;

  @RequestMapping(value = "/")
  public String index() {
    return "index";
  }

  @RequestMapping(value = "job/data/lagou/", method = RequestMethod.GET)
  @ResponseBody
  public APIListResponseDTO dataFromLagou(@RequestParam(value = "ct")String city,
      @RequestParam(value = "kw") String keyword,
      @RequestParam(value = "pn") int pageNumber,
      @RequestParam(value = "wy", required = false) String workYear,
      @RequestParam(value = "ms", required = false) String monthlySalary) {

    if (pageNumber < 1) {
      pageNumber = 1;
    }

    List<LagouJobInfo> ret =
        jobSpiderService.fetchJobInfosFromLagou(city, keyword, pageNumber, workYear, monthlySalary);

    boolean hasMore = false;
    int limit = 0;
    long totalCount = 0;
    int nextStart = 0;

    return new APIListResponseDTO(hasMore, limit, totalCount, nextStart,
        Lists.newArrayList(Iterables.filter(ret, Object.class)));
  }


}
