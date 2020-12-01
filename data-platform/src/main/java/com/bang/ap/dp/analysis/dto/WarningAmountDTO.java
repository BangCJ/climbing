package com.bang.ap.dp.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarningAmountDTO {
    public String historyAmount;
    public String todayAmount;
    public List<WarningDailyAmountDTO> dailyAmount;

}
