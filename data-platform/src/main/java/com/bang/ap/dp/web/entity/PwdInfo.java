package com.bang.ap.dp.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PwdInfo implements Serializable {
    private int id;
    /**
     * 用户编码
     */
    private int userId;
    /**
     * 用户code
     */
    private String userCode;
    /**
     * 用户性别
     */
    private String pwd;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}
