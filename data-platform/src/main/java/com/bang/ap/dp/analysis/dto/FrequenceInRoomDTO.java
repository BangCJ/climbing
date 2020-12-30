package com.bang.ap.dp.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrequenceInRoomDTO {
    public int id;
    public int roomId;
    public int times;
    public String checkDate;
    public Date createTime;
    public Date updateTime;
}
