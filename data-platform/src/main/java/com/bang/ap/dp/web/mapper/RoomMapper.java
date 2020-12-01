package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.web.entity.RoomInfo;
import com.bang.ap.dp.web.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoomMapper {

    RoomInfo selectById(long roomId);

    List<RoomInfo> getRoomList();

    void addRoomInfo(RoomInfo roomInfo);


    void updateRoomInfo(RoomInfo roomInfo);

    List<RoomInfo>selectPage(@Param("searchMap") Map<String, Object> searchMap);


}
