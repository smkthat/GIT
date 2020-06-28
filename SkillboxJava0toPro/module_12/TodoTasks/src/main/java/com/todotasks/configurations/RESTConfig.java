package com.todotasks.configurations;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
public class RESTConfig {

  @Bean
  public View todotasks() {
    MappingJackson2JsonView view = new MappingJackson2JsonView();
    view.setPrettyPrint(true);
    return view;
  }

  @Bean
  public ViewResolver viewResolver() {
    return new BeanNameViewResolver();
  }
}