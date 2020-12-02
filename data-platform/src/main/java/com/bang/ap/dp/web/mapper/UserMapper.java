package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.web.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    UserInfo selectById(long userId);

    List<UserInfo> getUserByUserInfo(UserInfo userInfo);

    List<UserInfo> getUserList();

    int addUserInfo(UserInfo userInfo);


    void updateUserInfo(UserInfo userInfo);

    List<UserInfo>selectPage( @Param("searchMap") Map<String, Object> searchMap);


}
