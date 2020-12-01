package com.bang.ap.dp.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerminalHealthCheckDTO {
    public String unHealthyRate;
    public String healthyRate;
}
