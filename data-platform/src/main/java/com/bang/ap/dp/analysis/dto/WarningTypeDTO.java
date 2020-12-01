package com.bang.ap.dp.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarningTypeDTO {
    public String warningTypeId;
    public String warningTypeName;
    public String rate;

}
