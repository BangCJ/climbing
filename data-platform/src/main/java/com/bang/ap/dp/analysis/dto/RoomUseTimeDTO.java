package com.bang.ap.dp.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomUseTimeDTO {
    public int id;
    public int roomId;
    public String timeLength;
    public String date;
    public String createTime;
    public String updateTime;
}
