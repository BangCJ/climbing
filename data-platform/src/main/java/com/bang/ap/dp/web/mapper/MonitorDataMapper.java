package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.web.entity.MonitorData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MonitorDataMapper {

    MonitorData selectById(long id);

    List<MonitorData> getLastMonitorDataByType(String monitorType);

    MonitorData getMonitorDataByParam(MonitorData monitorData);

    List<MonitorData> getMonitorDataByMonitorTypeAndOriginDataTime(@Param("monitorType") String monitorType, @Param("originDataTime") String originDataTime);

    void addMonitorData(MonitorData monitorData);
}
