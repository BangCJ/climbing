package com.bang.ap.dp.web.service;


import com.bang.ap.dp.web.entity.MessageInfo;

public interface MessageService {

    MessageInfo getMessageById(int id);

    void addMessageInfo(MessageInfo messageInfo);
}
