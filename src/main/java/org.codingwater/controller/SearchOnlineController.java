package org.codingwater.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by water on 4/19/16.
 */

@Controller
public class SearchOnlineController {

  @RequestMapping(value = "/")
  public String index() {
    return "index";
  }


}
