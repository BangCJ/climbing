package com.bang.ap.dp.receive.hikvision.controller;

import com.bang.ap.dp.receive.hikvision.service.HikSensorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/monitor/hikvision")
@Slf4j
public class MonitorDataReceiveController {
    @Autowired
    public HikSensorService hikSensorService;


    @RequestMapping(path = "/saveSensor", method = RequestMethod.GET)
    @ResponseBody
    public void saveSensor(HttpServletRequest request) {
        hikSensorService.saveSensorInfo();

    }

}
