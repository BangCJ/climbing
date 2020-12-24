package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.analysis.dto.RoomUseTimeDTO;
import com.bang.ap.dp.analysis.dto.RoomUseTimeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoomUsedTimeLengthMapper {

    List<RoomUseTimeDTO> getRoomUseTimeDTO(RoomUseTimeDTO roomUseTimeDTO);

    List<RoomUseTimeDTO> getTop7RoomUseTime(RoomUseTimeDTO roomUseTimeDTO);

    void insertRoomUseTimeDTO(RoomUseTimeDTO roomUseTimeDTO);

    void updateRoomUseTimeDTO(RoomUseTimeDTO roomUseTimeDTO);


}
