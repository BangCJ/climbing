package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.web.entity.PwdInfo;
import com.bang.ap.dp.web.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PwdMapper {

    PwdInfo getPwdByUserId(int userId);

    PwdInfo getPwdByUserCode(String userCode);

    void addPwdInfo(PwdInfo pwdInfo);

    void updatePwdInfo(PwdInfo pwdInfo);


}
