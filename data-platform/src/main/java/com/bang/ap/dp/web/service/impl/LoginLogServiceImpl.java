package com.bang.ap.dp.web.service.impl;


import com.bang.ap.dp.utils.PageRequest;
import com.bang.ap.dp.utils.PageResult;
import com.bang.ap.dp.utils.PageUtils;
import com.bang.ap.dp.web.entity.LoginLog;
import com.bang.ap.dp.web.entity.TerminalInfo;
import com.bang.ap.dp.web.mapper.LoginLogMapper;
import com.bang.ap.dp.web.service.LoginLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;


    @Override
    public LoginLog getLoginLogById(int id) {
        return loginLogMapper.selectById(id);
    }

    @Override
    public void addLoginLog(LoginLog loginLog) {
        loginLogMapper.addLoginLog(loginLog);
    }

    @Override
    public PageResult findPage(PageRequest pageRequest, Map<String, Object> searchMap) {
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest,searchMap));
    }

    /**
     * 调用分页插件完成分页
     * @param
     * @return
     */
    private PageInfo<LoginLog> getPageInfo(PageRequest pageRequest, Map<String, Object> searchMap) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<LoginLog> terminalInfoList = loginLogMapper.selectPage(searchMap);
        return new PageInfo<LoginLog>(terminalInfoList);
    }
}
