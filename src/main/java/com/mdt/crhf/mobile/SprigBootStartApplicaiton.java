package com.mdt.crhf.mobile;/*
/* @Program: medlogicprint
/* @Description: To start a web application
/* @Author: Peng Chen
/* @Created: 2019-10-27 11:57
/*/

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class SprigBootStartApplicaiton extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(MobileApplication.class);
    }

}
