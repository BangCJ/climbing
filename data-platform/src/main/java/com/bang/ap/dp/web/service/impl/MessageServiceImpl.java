package com.bang.ap.dp.web.service.impl;


import com.bang.ap.dp.web.entity.MessageInfo;
import com.bang.ap.dp.web.entity.UserInfo;
import com.bang.ap.dp.web.mapper.MessageMapper;
import com.bang.ap.dp.web.mapper.UserMapper;
import com.bang.ap.dp.web.service.MessageService;
import com.bang.ap.dp.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;


    @Override
    public MessageInfo getMessageById(int id) {

        MessageInfo messageInfo = messageMapper.selectById(id);

        return messageInfo;
    }

    @Override
    public void addMessageInfo(MessageInfo messageInfo) {
        messageMapper.addMessageInfo(messageInfo);
    }

}
