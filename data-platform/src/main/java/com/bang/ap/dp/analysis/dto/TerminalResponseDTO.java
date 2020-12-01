package com.bang.ap.dp.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerminalResponseDTO {
    public List<TerminalAmountDTO> teminal;
    public TerminalHealthCheckDTO healthCheck;
}
