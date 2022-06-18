package com.smart.tech.start.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("showMyLoginPage")
    public String showLoginPage(){
        return "login-page";
    }
}
