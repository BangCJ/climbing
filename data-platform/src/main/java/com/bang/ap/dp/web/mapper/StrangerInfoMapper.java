package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.analysis.dto.StrangerInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StrangerInfoMapper {

    List<StrangerInfoDTO> getStrangerInfoDTO(StrangerInfoDTO strangerInfoDTO);

    List<StrangerInfoDTO> getTop10StrangerInfoDTO(StrangerInfoDTO strangerInfoDTO);

    int getAccount(String dataTime);

    void insertStrangerInfoDTO(StrangerInfoDTO strangerInfoDTO);

    void insertStrangerInfoDTOList(List<StrangerInfoDTO> strangerInfoDTOList);

    void updateStrangerInfoDTO(StrangerInfoDTO strangerInfoDTO);


}
