package com.bang.ap.dp.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarningResponseDTO {
    public List<WarningTypeDTO> warningType;
    public WarningAmountDTO warningAmount;
    public List<WarningDetailDTO> warningDetail;

}
