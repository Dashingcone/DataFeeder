package com.orbit.DataFeeder.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
@RequestMapping(path = "/getData")
public class controller {
    @GetMapping
    public void func()  {

    }
}
