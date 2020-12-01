package com.bang.ap.dp.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataRealTimeDTO {
    public String currentDataTime;
    public String nextTimeValue;
    public String realTimeValue;
    public String type;
}
