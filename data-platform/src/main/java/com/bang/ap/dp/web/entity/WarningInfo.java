package com.bang.ap.dp.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarningInfo implements Serializable {
    /**
     * id
     */
    private int id;
    /**
     * code
     */
    private String code;
    /**
     * 预警内容
     */
    private String warningContent;
    /**
     *  预警类型
     */
    private String warningType;
    /**
     * 预警时间
     */
    private String warningTime;
    /**
     * 预警区域
     */
    private String warningArea;
    /**
     * 预警信息相关链接信息
     */
    private String warningAttach;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
