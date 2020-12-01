package com.bang.ap.dp.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageInfo implements Serializable {
    /**
     * id
     */
    private int id;
    /**
     * 编码
     */
    private String code;
    /**
     * 主题
     */
    private String subject;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息类型
     */
    private String msgType;
    /**
     * 消息通道
     */
    private String msgChannel;
    /**
     * 消息状态
     */
    private int status;
    /**
     * 消息发送人
     */
    private String sender;
    /**
     * 消息接收人
     */
    private String receiver;
    /**
     * 消息发送时间
     */
    private String sendTime;


}
