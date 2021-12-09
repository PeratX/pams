package org.itxtech.pams.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @RequestMapping("/")
    public String index(){
        return "PeratX Anything Management System<br/>Made with ❤ by PeratX@iTXTech.org<br/>Copyright (c) 2021 PeratX@iTXTech.org All Rights Reserved.";
    }
}
