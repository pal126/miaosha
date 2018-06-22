package com.pal.miaosha.controller;

import com.pal.miaosha.rabbitmq.MQSender;
import com.pal.miaosha.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test/")
public class TestContrller {

    @Autowired
    MQSender mqSender;

    @RequestMapping("test")
    @ResponseBody
    public Result<String> test() {
        mqSender.sendTopic("wktest");
        return Result.success("success");
    }
}
