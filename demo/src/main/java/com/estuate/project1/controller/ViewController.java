package com.estuate.project1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping(value = "/{path:[^\\.]*}") // Matches all paths except those with a dot (e.g., .css, .js)
    public String redirect() {
        return "forward:/index.html";
    }
}