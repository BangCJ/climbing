package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.analysis.dto.ImportantPeopleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImportantPeopleMapper {

    List<ImportantPeopleDTO> getImportantPeopleDTO(ImportantPeopleDTO importantPeopleDTO);

    List<ImportantPeopleDTO> getImportantPeopleDTOByDataTime(String dataTime);

    List<ImportantPeopleDTO> getTop10ImportantPeopleDTO(ImportantPeopleDTO importantPeopleDTO);

    int getAccount(String dataTime);

    void insertImportantPeopleDTO(ImportantPeopleDTO importantPeopleDTO);

    void insertImportantPeopleDTOList(List<ImportantPeopleDTO> importantPeopleDTOList);

    void updateImportantPeopleDTO(ImportantPeopleDTO importantPeopleDTO);


}
