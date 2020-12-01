package com.bang.ap.dp.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorData  implements Serializable {
    private int id;
    private String code ;
    private String name ;
    /**
     * 监控数据类型
     */
    private String monitorType;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 数值
     */
    private String value ;

    /**
     * 数据产生时间
     */
    private String originDateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}
