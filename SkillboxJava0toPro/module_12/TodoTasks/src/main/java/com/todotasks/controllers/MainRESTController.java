package com.todotasks.controllers;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainRESTController {

  @RequestMapping
  @ResponseStatus(HttpStatus.FOUND)
  void root(HttpServletResponse response) throws IOException {
    response.sendRedirect("/todotask");
  }
}
