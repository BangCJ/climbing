package com.bang.ap.dp.web.mapper;

import com.bang.ap.dp.web.entity.TerminalInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TerminalMapper {

    TerminalInfo selectById(int id);

    List<TerminalInfo> getTerminalListByTypeAndGroup(@Param("type") String type, @Param("groupInfo") String groupInfo);

    void addTerminalInfo(TerminalInfo terminalInfo);

    void updateTerminalInfo(TerminalInfo terminalInfo);

    List<TerminalInfo> selectPage(@Param("searchMap") Map<String, Object> searchMap);

}
