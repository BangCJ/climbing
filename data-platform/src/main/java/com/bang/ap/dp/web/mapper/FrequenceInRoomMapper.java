package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.analysis.dto.FrequenceInRoomDTO;
import com.bang.ap.dp.web.entity.PwdInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FrequenceInRoomMapper {

    FrequenceInRoomDTO getFrequenceInRoomDTOById(int userId);

    List<FrequenceInRoomDTO> getFrequenceInRoomDTO(FrequenceInRoomDTO frequenceInRoomDTO);

    List<FrequenceInRoomDTO> getFrequenceByRoomId(String roomId);

    List<FrequenceInRoomDTO> getTop7FrequenceByRoomId(String roomId);

    void insertFrequenceInRoomDTO(FrequenceInRoomDTO frequenceInRoomDTO);

    void updateFrequenceInRoomDTO(FrequenceInRoomDTO frequenceInRoomDTO);


}
