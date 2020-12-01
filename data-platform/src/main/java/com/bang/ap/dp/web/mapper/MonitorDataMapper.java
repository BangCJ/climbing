package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.web.entity.MonitorData;
import com.bang.ap.dp.web.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MonitorDataMapper {

    MonitorData selectById(long id);

    MonitorData getMonitorDataByParam(MonitorData monitorData);

    void addMonitorData(MonitorData monitorData);
}
