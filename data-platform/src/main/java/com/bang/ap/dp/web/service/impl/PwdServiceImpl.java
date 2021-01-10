package com.bang.ap.dp.web.service.impl;


import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.utils.DPTimeUtil;
import com.bang.ap.dp.web.entity.LoginLog;
import com.bang.ap.dp.web.entity.PwdInfo;
import com.bang.ap.dp.web.entity.UserInfo;
import com.bang.ap.dp.web.mapper.LoginLogMapper;
import com.bang.ap.dp.web.mapper.PwdMapper;
import com.bang.ap.dp.web.mapper.UserMapper;
import com.bang.ap.dp.web.service.PwdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PwdServiceImpl implements PwdService {

    @Autowired
    private PwdMapper pwdMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginLogMapper loginLogMapper;


    @Override
    public PwdInfo getPwdByUserId(int userId) {
        return pwdMapper.getPwdByUserId(userId);
    }

    @Override
    public PwdInfo getPwdByUserCode(String userCode) {
        return pwdMapper.getPwdByUserCode(userCode);
    }

    @Override
    public void addPwdInfo(PwdInfo pwdInfo) {
        pwdMapper.addPwdInfo(pwdInfo);

    }

    @Override
    public void updatePwdInfo(PwdInfo pwdInfo) {
        pwdMapper.updatePwdInfo(pwdInfo);

    }

    @Override
    public boolean checkPwd(String code, String pwd) {
        PwdInfo pwdInfo = pwdMapper.getPwdByUserCode(code);
        if (null != pwdInfo && pwd.equals(pwdInfo.getPwd())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkPwd(String code, String pwd, String ip) {
        PwdInfo pwdInfo = pwdMapper.getPwdByUserCode(code);
        UserInfo userInfoParam = new UserInfo();
        userInfoParam.setCode(code);
        List<UserInfo> userInfoList = userMapper.getUserByUserInfo(userInfoParam);
        if (null==userInfoList || userInfoList.size()==0){
            return false;
        }

        if (null != pwdInfo && pwd.equals(pwdInfo.getPwd())) {
            saveLoginLog(userInfoList.get(0), "1", ip);
            return true;
        }
        return false;
    }

    private void saveLoginLog(UserInfo userInfo, String status, String ip) {
        LoginLog loginLog = new LoginLog();
        loginLog.setLoginIp(ip);
        loginLog.setLoginUserId(String.valueOf(userInfo.getId()));
        loginLog.setLoginUserName(userInfo.getName());
        loginLog.setLoginTime(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT));
        loginLog.setCreateTime(new Date());
        loginLog.setUpdateTime(new Date());
        loginLogMapper.addLoginLog(loginLog);

    }
}
