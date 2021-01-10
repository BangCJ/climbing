package com.bang.ap.dp.web.service;


import com.bang.ap.dp.web.entity.PwdInfo;

public interface PwdService {

    PwdInfo getPwdByUserId(int userId);

    PwdInfo getPwdByUserCode(String userCode);

    void addPwdInfo(PwdInfo pwdInfo);

    void updatePwdInfo(PwdInfo pwdInfo);

    boolean checkPwd(String code ,String pwd);

    boolean checkPwd(String code ,String pwd,String ip);


}
