package org.codingwater.controller;

import org.codingwater.service.IJobSpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
  public String dataFromLagou() {



    return "ok";
  }


}
