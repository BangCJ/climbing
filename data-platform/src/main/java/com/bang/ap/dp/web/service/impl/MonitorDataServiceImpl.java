package com.bang.ap.dp.web.service.impl;


import com.bang.ap.dp.web.entity.MonitorData;
import com.bang.ap.dp.web.entity.UserInfo;
import com.bang.ap.dp.web.mapper.MonitorDataMapper;
import com.bang.ap.dp.web.mapper.UserMapper;
import com.bang.ap.dp.web.service.MonitorDataService;
import com.bang.ap.dp.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MonitorDataServiceImpl implements MonitorDataService {

    @Autowired
    private MonitorDataMapper monitorDataMapper;

    @Override
    public MonitorData getMonitorDataById(int id) {

        MonitorData monitorData = monitorDataMapper.selectById(id);
        return monitorData;
    }

    @Override
    public MonitorData getMonitorDataByParam(MonitorData monitorData) {
        return monitorDataMapper.getMonitorDataByParam(monitorData);
    }

    @Override
    public void addMonitorData(MonitorData monitorData) {
        monitorDataMapper.addMonitorData(monitorData);
    }

}
