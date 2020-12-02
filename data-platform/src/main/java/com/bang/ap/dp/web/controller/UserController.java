package com.bang.ap.dp.web.controller;


import com.bang.ap.dp.web.entity.UserInfo;
import com.bang.ap.dp.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public UserInfo getById(@PathVariable int id) {
        UserInfo user = null;
        try {
            user = userService.getUserById(id);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return user;
    }

    @RequestMapping(path = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    public String addUser(@RequestBody UserInfo userInfo) {
        try {
            userInfo.setCreateTime(new Date());
            userInfo.setUpdateTime(new Date());
           userService.addUserInfo(userInfo);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return "success";
    }




}
