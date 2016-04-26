package org.codingwater.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by water on 4/26/16.
 */
@Controller
public class ChartsController {

  @RequestMapping(value = "/charts/", method = RequestMethod.GET)
  public String chartsView() {

    return "charts";
  }


}
