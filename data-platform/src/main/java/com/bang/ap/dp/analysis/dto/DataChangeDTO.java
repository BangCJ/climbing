package com.bang.ap.dp.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataChangeDTO {
    public String dataTime;

    public String smokehangeValue;

    public String temperaturehangeValue;

    public String gashangeValue;
}
