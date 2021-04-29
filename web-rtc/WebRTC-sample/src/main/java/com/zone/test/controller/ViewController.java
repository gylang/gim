package com.zone.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Owen Pan on 2016/10/8.
 */
@Controller
public class ViewController {
    @RequestMapping("/")
    public String index(HttpServletRequest request) {
        return "login";
    }
    @RequestMapping("/view/**")
    public String path(HttpServletRequest request) {
        return request.getRequestURI().replace(request.getContextPath()+"/view/", "/");
    }

}
