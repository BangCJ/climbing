package com.bang.ap.dp.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomUseTimeDTO {
    public int id;
    public int roomId;
    public long timeLength;
    public String earlierTime;
    public String laterTime;
    public String date;
    public Date createTime;
    public Date updateTime;
}
