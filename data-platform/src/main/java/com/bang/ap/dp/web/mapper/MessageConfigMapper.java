package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.web.entity.MessageReceiverInfo;
import com.bang.ap.dp.web.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessageConfigMapper {


    List<MessageReceiverInfo> getMessageReceiveConfigList();

    void addMessageReceiveConfig(MessageReceiverInfo messageReceiverInfo);

    void updateMessageReceiveConfig(MessageReceiverInfo messageReceiverInfo);

    void deleteMessageReceiveConfigByUserId(String userId);

    void deleteMessageReceiveConfigById(int id);



}
