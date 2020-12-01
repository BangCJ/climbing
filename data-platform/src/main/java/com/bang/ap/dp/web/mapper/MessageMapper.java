package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.web.entity.MessageInfo;
import com.bang.ap.dp.web.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper {

    MessageInfo selectById(int messageId);

    void addMessageInfo(MessageInfo messageInfo);
}
