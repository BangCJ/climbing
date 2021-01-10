package com.bang.ap.dp.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageReceiverInfo implements Serializable {
    /**
     * id
     */
    private int id;
    /**
     * 是否发送邮件
     */
    private boolean emailEnable;
    /**
     * 是否发送短信
     */
    private boolean smsEnable;
    /**
     * 是否语音播报
     */
    private boolean voiceEnable;

    /**
     * 消息接收人id
     */
    private String  receiverId;

    private String receiverName;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;



}
