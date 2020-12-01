package com.bang.ap.dp.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.utils.PageRequest;
import com.bang.ap.dp.utils.PageResult;
import com.bang.ap.dp.utils.ResponseUtil;
import com.bang.ap.dp.web.entity.RoomInfo;
import com.bang.ap.dp.web.entity.UserInfo;
import com.bang.ap.dp.web.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/manage")
@Slf4j
public class ManageController {
    @Autowired
    private UserService userService;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private WarningService warningService;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private RoomService roomService;

    @RequestMapping(path = "/getUserList", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getUserList() {
        try {
            List<UserInfo> userList = userService.getUserList();
            return ResponseUtil.buildSuccessResponse(userList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
    }


    @RequestMapping(path = "/userInfo/{id}", method = RequestMethod.GET)
    @ResponseBody
    public UserInfo getUserInfoById(@PathVariable int id) {
        UserInfo user = null;
        try {
            user = userService.getUserById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return user;
    }

    @RequestMapping(path = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addUser(@RequestBody UserInfo userInfo) {
        try {
            userInfo.setCreateTime(new Date());
            userInfo.setUpdateTime(new Date());
            if (userInfo.getId() != 0) {
                userService.updateUserInfo(userInfo);
            }
            userService.addUserInfo(userInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
        return ResponseUtil.buildSuccessResponse();
    }

    @RequestMapping(path = "/addRoom", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addRoom(@RequestBody RoomInfo roomInfo) {
        try {
            roomInfo.setCreateTime(new Date());
            roomInfo.setUpdateTime(new Date());
            if (roomInfo.getId() != 0) {
                roomService.updateRoomInfo(roomInfo);
            }
            roomService.addRoomInfo(roomInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
        return ResponseUtil.buildSuccessResponse();
    }
    @RequestMapping(path = "/getRoomList", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getRoomList() {
        try {
            List<RoomInfo> roomInfoList = roomService.getRoomList();
            return ResponseUtil.buildSuccessResponse(roomInfoList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
    }


    @RequestMapping(value = "/getUserPage", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getUserPage(@RequestParam(name = "code", defaultValue = "") String code,
                              @RequestParam(name = "name", defaultValue = "") String name,
                              @RequestParam(name = "email", defaultValue = "") String email,
                              @RequestParam(name = "phone", defaultValue = "") String phone,
                              @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        PageRequest pageRequest =new PageRequest(pageNum,pageSize);
        Map<String,Object>parMap=new HashMap<>();
        parMap.put("name",name);
        parMap.put("email",email);
        parMap.put("code",code);
        parMap.put("phone",phone);
        PageResult pageResult= null;
        try {
            pageResult = userService.findPage(pageRequest,parMap);
        } catch (Exception e) {
            log.error("分页查询用户数据失败",e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
        return ResponseUtil.buildSuccessResponse(pageResult);

    }

    @RequestMapping(value = "/getTerminalPage", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getTerminalPage(@RequestParam(name = "code", defaultValue = "") String code,
                                  @RequestParam(name = "name", defaultValue = "") String name,
                                  @RequestParam(name = "status", defaultValue = "") String status,
                                  @RequestParam(name = "type", defaultValue = "") String type,
                                  @RequestParam(name = "roomId", defaultValue = "") String roomId,
                                  @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        PageRequest pageRequest =new PageRequest(pageNum,pageSize);
        Map<String,Object>parMap=new HashMap<>();
        parMap.put("name",name);
        parMap.put("type",type);
        parMap.put("code",code);
        parMap.put("status",status);
        parMap.put("roomId",roomId);
        PageResult pageResult= null;
        try {
            pageResult = terminalService.findPage(pageRequest,parMap);
        } catch (Exception e) {
            log.error("分页查询设备数据失败",e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
        return ResponseUtil.buildSuccessResponse(pageResult);

    }

    @RequestMapping(value = "/getWarningPage", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getWarningPage(@RequestParam(name = "warningType", defaultValue = "") String warningType,
                                  @RequestParam(name = "startTime", defaultValue = "") String startTime,
                                     @RequestParam(name = "endTime", defaultValue = "") String endTime,
                                  @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        PageRequest pageRequest =new PageRequest(pageNum,pageSize);
        Map<String,Object>parMap=new HashMap<>();
        parMap.put("warningType",warningType);
        parMap.put("startTime",startTime);
        parMap.put("endTime",endTime);
        PageResult pageResult= null;
        try {
            pageResult = warningService.findPage(pageRequest,parMap);
        } catch (Exception e) {
            log.error("分页查询预警数据失败",e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
        return ResponseUtil.buildSuccessResponse(pageResult);

    }

    @RequestMapping(value = "/getLoginPage", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getLoginPage(@RequestParam(name = "loginUserName", defaultValue = "") String loginUserName,
                                   @RequestParam(name = "loginIp", defaultValue = "") String loginIp,
                                   @RequestParam(name = "startTime", defaultValue = "") String startTime,
                                   @RequestParam(name = "endTime", defaultValue = "") String endTime,
                                   @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        PageRequest pageRequest =new PageRequest(pageNum,pageSize);
        Map<String,Object>parMap=new HashMap<>();
        parMap.put("loginUserName",loginUserName);
        parMap.put("loginIp",loginIp);
        parMap.put("endTime",endTime);
        parMap.put("startTime",startTime);
        PageResult pageResult= null;
        try {
            pageResult = loginLogService.findPage(pageRequest,parMap);
        } catch (Exception e) {
            log.error("分页查询登录日志数据失败",e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
        return ResponseUtil.buildSuccessResponse(pageResult);

    }

    @RequestMapping(value = "/getRoomPage", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getRoomPage(@RequestParam(name = "name", defaultValue = "") String name,
                                   @RequestParam(name = "owner", defaultValue = "") String owner,
                                   @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        PageRequest pageRequest =new PageRequest(pageNum,pageSize);
        Map<String,Object>parMap=new HashMap<>();
        parMap.put("name",name);
        parMap.put("owner",owner);
        PageResult pageResult= null;
        try {
            pageResult = roomService.findPage(pageRequest,parMap);
        } catch (Exception e) {
            log.error("分页查询登录日志数据失败",e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
        return ResponseUtil.buildSuccessResponse(pageResult);

    }

}
