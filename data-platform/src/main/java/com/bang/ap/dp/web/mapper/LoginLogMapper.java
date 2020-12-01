package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.web.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface LoginLogMapper {

    LoginLog selectById(int id);

    void addLoginLog(LoginLog loginLog);

    List<LoginLog> selectPage(@Param("searchMap") Map<String, Object> searchMap);

}
