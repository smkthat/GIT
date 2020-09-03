package com.todotasks.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class TodoErrorController implements ErrorController {
  @RequestMapping("/error")
  public String handleError(HttpServletRequest request, Model model) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

    if (status != null) {
      int statusCode = Integer.parseInt(status.toString());
      //      model.addAttribute("errorStatus", statusCode);
      //      if (statusCode == HttpStatus.NOT_FOUND.value()) {
      //        return "404";
      //      } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
      //        return "500";
      //      } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
      //        return "403";
      //      }
    }
    model.addAttribute("errorStatus", status);
    return "error";
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }
}
