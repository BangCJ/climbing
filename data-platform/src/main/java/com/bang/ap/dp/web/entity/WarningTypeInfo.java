package com.bang.ap.dp.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarningTypeInfo implements Serializable {
    private int id;
    private String code;
    private String name;
    private int account;
    private Date createTime;
    private Date updateTime;

}
