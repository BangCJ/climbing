package com.bang.ap.dp.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageLimitInfo {
    public int id;
    public String type;
    public String date;
    public int num;
    private Date createTime;
    private Date updateTime;
}
