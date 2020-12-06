package com.bang.ap.dp.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.utils.PageRequest;
import com.bang.ap.dp.utils.PageResult;
import com.bang.ap.dp.utils.ResponseUtil;
import com.bang.ap.dp.web.entity.MessageReceiverInfo;
import com.bang.ap.dp.web.entity.PwdInfo;
import com.bang.ap.dp.web.entity.RoomInfo;
import com.bang.ap.dp.web.entity.UserInfo;
import com.bang.ap.dp.web.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @Autowired
    private PwdService pwdService;

    @Autowired
    private MessageConfigService messageConfigService;

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
            if (StringUtils.isEmpty(userInfo.getCode())) {
                return ResponseUtil.buildFailureResponse("用户编码不能为空");
            }
            if (StringUtils.isEmpty(userInfo.getName())) {
                return ResponseUtil.buildFailureResponse("用户名称不能为空");
            }
            userService.checkRepeat(userInfo);
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
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        Map<String, Object> parMap = new HashMap<>();
        parMap.put("name", name);
        parMap.put("email", email);
        parMap.put("code", code);
        parMap.put("phone", phone);
        PageResult pageResult = null;
        try {
            pageResult = userService.findPage(pageRequest, parMap);
        } catch (Exception e) {
            log.error("分页查询用户数据失败", e);
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
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        Map<String, Object> parMap = new HashMap<>();
        parMap.put("name", name);
        parMap.put("type", type);
        parMap.put("code", code);
        parMap.put("status", status);
        parMap.put("roomId", roomId);
        PageResult pageResult = null;
        try {
            pageResult = terminalService.findPage(pageRequest, parMap);
        } catch (Exception e) {
            log.error("分页查询设备数据失败", e);
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
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        Map<String, Object> parMap = new HashMap<>();
        parMap.put("warningType", warningType);
        parMap.put("startTime", startTime);
        parMap.put("endTime", endTime);
        PageResult pageResult = null;
        try {
            pageResult = warningService.findPage(pageRequest, parMap);
        } catch (Exception e) {
            log.error("分页查询预警数据失败", e);
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
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        Map<String, Object> parMap = new HashMap<>();
        parMap.put("loginUserName", loginUserName);
        parMap.put("loginIp", loginIp);
        parMap.put("endTime", endTime);
        parMap.put("startTime", startTime);
        PageResult pageResult = null;
        try {
            pageResult = loginLogService.findPage(pageRequest, parMap);
        } catch (Exception e) {
            log.error("分页查询登录日志数据失败", e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
        return ResponseUtil.buildSuccessResponse(pageResult);

    }

    @RequestMapping(value = "/getRoomPage", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getRoomPage(@RequestParam(name = "name", defaultValue = "") String name,
                                  @RequestParam(name = "owner", defaultValue = "") String owner,
                                  @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        Map<String, Object> parMap = new HashMap<>();
        parMap.put("name", name);
        parMap.put("owner", owner);
        PageResult pageResult = null;
        try {
            pageResult = roomService.findPage(pageRequest, parMap);
        } catch (Exception e) {
            log.error("分页查询登录日志数据失败", e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
        return ResponseUtil.buildSuccessResponse(pageResult);

    }


    @RequestMapping(path = "/checkPwd", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject checkPwd(@RequestBody JSONObject loginInfo) {
        try {
            String code = (String) loginInfo.get("code");
            String pwd = (String) loginInfo.get("pwd");
            if (StringUtils.isEmpty(code) || StringUtils.isEmpty(pwd)) {
                return ResponseUtil.buildFailureResponse("用户名和密码不能为空");
            }
            if (pwdService.checkPwd(code, pwd)) {
                return ResponseUtil.buildSuccessResponse();
            } else {
                return ResponseUtil.buildFailureResponse("用户名或密码错误");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse("登录失败");
        }
    }

    @RequestMapping(path = "/resetPwd", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject resetPwd(@RequestBody JSONObject userCodeObj) {
        try {
            String code = (String) userCodeObj.get("userCode");
            PwdInfo pwdInfo = new PwdInfo();
            pwdInfo.setPwd("123456");
            pwdInfo.setUserCode(code);
            pwdInfo.setUpdateTime(new Date());
            pwdService.updatePwdInfo(pwdInfo);
            return ResponseUtil.buildSuccessResponse();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse("操作失败");
        }
    }

    @RequestMapping(path = "/changePwd", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject reseatPwd(@RequestBody JSONObject pwdChangeInfo) {
        try {
            String userCode = (String) pwdChangeInfo.get("userCode");
            String oldPwd = (String) pwdChangeInfo.get("oldPwd");
            String newPwd = (String) pwdChangeInfo.get("newPwd");
            if (!pwdService.checkPwd(userCode, oldPwd)) {
                return ResponseUtil.buildFailureResponse("原始密码不正确");
            }
            PwdInfo pwdInfo = new PwdInfo();
            pwdInfo.setPwd(newPwd);
            pwdInfo.setUserCode(userCode);
            pwdInfo.setUpdateTime(new Date());
            pwdService.updatePwdInfo(pwdInfo);
            return ResponseUtil.buildSuccessResponse();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse("操作失败");
        }
    }

    @RequestMapping(path = "/savaMessageConfig", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject savaMessageConfig(@RequestBody MessageReceiverInfo messageReceiverInfo) {
        try {
            if (0!=messageReceiverInfo.getId()){
                messageConfigService.updateMessageReceiveConfig(messageReceiverInfo);
            }else{
                messageReceiverInfo.setCreateTime(new Date());
                messageReceiverInfo.setUpdateTime(new Date());
                messageConfigService.addMessageReceiveConfig(messageReceiverInfo);
            }
            return ResponseUtil.buildSuccessResponse();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse("操作失败");
        }
    }

    @RequestMapping(path = "/getMessageConfig", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getMessageConfig() {
        try {
            MessageReceiverInfo messageReceiverInfo = messageConfigService.getMessageReceiveConfig();

            String receiverString =messageReceiverInfo.getReceiverString();
            if (null!=receiverString){
                String[]userIdArray=receiverString.split(",");
                if (userIdArray!=null&&userIdArray.length>0){
                    List<UserInfo>usrList=new ArrayList<>();
                    for (int i = 0; i < userIdArray.length; i++) {
                        UserInfo userInfo=new UserInfo();
                        userInfo=userService.getUserById(Integer.valueOf(userIdArray[i]));
                        usrList.add(userInfo);
                    }
                    messageReceiverInfo.setReceiverList(usrList);
                }
            }



            return ResponseUtil.buildSuccessResponse(messageReceiverInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse("操作失败");
        }
    }


}
