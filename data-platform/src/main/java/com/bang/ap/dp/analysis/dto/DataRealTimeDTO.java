package com.bang.ap.dp.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataRealTimeDTO {
    public String currentDataTime;

    public String realSmoke;
    public String nextSmoke;

    public String realTemperature;
    public String nextTemperature;

    public String realGas;
    public String nextGas;
}
