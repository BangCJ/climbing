package com.bang.ap.dp.web.service;


import com.bang.ap.dp.utils.PageRequest;
import com.bang.ap.dp.utils.PageResult;
import com.bang.ap.dp.web.entity.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserInfo getUserById(int id);

    List<UserInfo> getUserByUserInfo(UserInfo userInfo);

    void addUserInfo(UserInfo userInfo);

    void updateUserInfo(UserInfo userInfo);

    List<UserInfo> getUserList();

    PageResult findPage(PageRequest pageRequest, Map<String, Object> searchMap);

    boolean checkRepeat(UserInfo userInfo) throws Exception;




}
