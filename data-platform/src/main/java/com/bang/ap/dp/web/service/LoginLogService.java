package com.bang.ap.dp.web.service;


import com.bang.ap.dp.utils.PageRequest;
import com.bang.ap.dp.utils.PageResult;
import com.bang.ap.dp.web.entity.LoginLog;

import java.util.Map;

public interface LoginLogService {

    LoginLog getLoginLogById(int id);

    void addLoginLog(LoginLog loginLog);

    PageResult findPage(PageRequest pageRequest, Map<String, Object> searchMap);





}
