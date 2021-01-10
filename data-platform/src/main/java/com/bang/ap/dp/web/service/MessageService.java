package com.bang.ap.dp.web.service;


import com.bang.ap.dp.web.entity.MessageInfo;
import com.bang.ap.dp.web.entity.WarningInfo;

public interface MessageService {

    MessageInfo getMessageById(int id);

    void addMessageInfo(MessageInfo messageInfo);

    void sendMessage(WarningInfo warning);
}
