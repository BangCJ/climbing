package com.bang.ap.dp.analysis.service;

import com.bang.ap.dp.analysis.dto.*;
import com.bang.ap.dp.web.entity.MonitorData;

import java.util.List;

public interface DataAnalysisService {

    List<FrequenceInRoomDTO> getFrequenceOfRoomInOneWeek(String rooId);

    List<RoomUseTimeDTO> getRoomUsedTimeInOneWeek(String rooId);

    int getRooUserdRateInOneWeek(String rooId);

    HighFrequenceResponseDTO  getHighFrequenceInfo();

    StrangerResponseDTO getStrangerInfo ();

    String getBispectrumUrl();

    WarningResponseDTO getWarningInfo();

    List<DataChangeDTO> getDataChangeForFire();

    List<DataRealTimeDTO> getRealTimeDataForFire();

    TerminalResponseDTO getTerminalInfo();

    List<MonitorData> getMonitorData();

    MonitorData getMonitorDataByType(String type);


}
