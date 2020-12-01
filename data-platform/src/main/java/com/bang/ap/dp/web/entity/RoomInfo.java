package com.bang.ap.dp.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomInfo {
    private int id;
    /**
     * 实验室编码
     */
    private String code;
    /**
     * 实验室名称
     */
    private String name;
    /**
     * 实验室负责人
     */
    private String owner;
    /**
     * 数据创建时间
     */
    private Date createTime;
    /**
     * 数据修改时间
     */
    private Date updateTime;
}
