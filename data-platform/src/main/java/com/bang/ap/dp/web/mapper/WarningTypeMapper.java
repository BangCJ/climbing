package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.web.entity.WarningTypeInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WarningTypeMapper {

    List<WarningTypeInfo> getWarningType();

    WarningTypeInfo getWarningTypeByType(String type);

}
