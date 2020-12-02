package com.bang.ap.dp.web.service.impl;


import com.bang.ap.dp.web.entity.PwdInfo;
import com.bang.ap.dp.web.entity.UserInfo;
import com.bang.ap.dp.web.mapper.PwdMapper;
import com.bang.ap.dp.web.service.PwdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PwdServiceImpl implements PwdService {

    @Autowired
    private PwdMapper pwdMapper;

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
       PwdInfo pwdInfo= pwdMapper.getPwdByUserCode(code);
       if (null !=pwdInfo && pwd.equals(pwdInfo.getPwd())){
           return true;
       }
        return false;
    }
}
