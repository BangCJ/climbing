package com.bang.ap.dp.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerminalInfo implements Serializable {
    private int id;
    /**
     * 设备编码
     */
    private String code;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备状态
     * 1：正常
     * 0：异常
     */
    private String status;
    /**
     * 设备类型
     * camera：摄像头
     * sensor：传感器
     * others：其他
     */
    private String type;
    /**
     * 所属分组
     */
    private String groupInfo;
    /**
     * 所属房间id
     */
    private String roomId;
    /**
     * 所属房间名称
     */
    private String roomName;
    /**
     * 数据创建时间
     */
    private Date createTime;
    /**
     * 数据更新时间
     */
    private Date updateTime;

}
