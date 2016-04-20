package org.codingwater.controller;

import org.codingwater.model.LagouJobInfo;
import org.codingwater.model.apiresp.APIListResponseDTO;
import org.codingwater.service.IJobSpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
      @RequestParam(value = "wy") String workYear,
      @RequestParam(value = "ms") String monthlySalary) {

    if (pageNumber < 1) {
      pageNumber = 1;
    }

    List<LagouJobInfo> ret =
        jobSpiderService.fetchJobInfosFromLagou(city, keyword, pageNumber, workYear, monthlySalary);


    return new APIListResponseDTO();
  }


}
