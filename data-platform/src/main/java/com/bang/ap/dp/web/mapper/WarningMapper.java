package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.web.entity.WarningInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WarningMapper {

    WarningInfo selectById(int id);

    void addWarningInfo(WarningInfo warningInfo);

    List<WarningInfo> selectPage(@Param("searchMap") Map<String, Object> searchMap);

}
