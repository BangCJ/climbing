package com.bang.ap.dp.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HighFrequenceResponseDTO {
    public List<HighFrequenceDTO> allData;
    public HighestFrequenceDTO highest;
    public List<ImportantPeopleDTO> importantPeople;

}
