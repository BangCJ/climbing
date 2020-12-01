package com.bang.ap.dp.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginLog implements Serializable {
    private int id;
    private String loginUserId;
    private String loginUserName;
    private String loginIp;
    private String loginTime;

    private Date createTime;
    private Date updateTime;

}
