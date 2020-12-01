package com.bang.ap.dp.web.service;


import com.bang.ap.dp.web.entity.MonitorData;

public interface MonitorDataService {

    MonitorData getMonitorDataById(int id);

    MonitorData getMonitorDataByParam(MonitorData monitorData);

    void addMonitorData(MonitorData monitorData);
}
