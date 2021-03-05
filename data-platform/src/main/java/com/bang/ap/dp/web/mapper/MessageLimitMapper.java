package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.web.entity.MessageLimitInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MessageLimitMapper {

    MessageLimitInfo selectById(int id);

    void addMessageLimit(MessageLimitInfo messageLimitInfo);

    void updateNumByDateAndType(@Param("date") String date, @Param("type") String type, @Param("num") int num);

    MessageLimitInfo getMessageLimitByDateAndType(@Param("date") String date, @Param("type") String type);

}
