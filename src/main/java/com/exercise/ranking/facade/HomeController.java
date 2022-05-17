package com.exercise.ranking.facade;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home redirection to OpenAPI api documentation
 */
@Controller
public class HomeController {

  @RequestMapping("/")
  public String index() {
    return "redirect:api-docs/";
  }
}
