package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.web.entity.MonitorData;
import com.bang.ap.dp.web.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MonitorDataMapper {

    MonitorData selectById(long id);

    List<MonitorData> getLastMonitorDataByType(String monitorType);

    MonitorData getMonitorDataByParam(MonitorData monitorData);

    void addMonitorData(MonitorData monitorData);
}
