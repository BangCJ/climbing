package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.web.entity.MonitorDataThreshold;
import com.bang.ap.dp.web.entity.WarningInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MonitorDataThresholdMapper {

    List<MonitorDataThreshold> getThreshold();

    MonitorDataThreshold getThresholdByType(String type);

}
