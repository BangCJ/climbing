package com.bang.ap.dp.analysis.service.impl;

import com.bang.ap.dp.analysis.dto.*;
import com.bang.ap.dp.analysis.service.DataAnalysisService;
import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.utils.DPTimeUtil;
import com.bang.ap.dp.web.entity.MonitorData;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@ConditionalOnProperty(prefix = "ap", name = "test-enable", havingValue = "false", matchIfMissing = true)
public class DataAnalysisServiceImpl implements DataAnalysisService {

    @Override
    public List<FrequenceInRoomDTO> getFrequenceOfRoomInOneWeek(String rooId) {
        return null;
    }

    @Override
    public List<RoomUseTimeDTO> getRoomUsedTimeInOneWeek(String rooId) {
        return null;
    }

    @Override
    public int getRooUserdRateInOneWeek(String rooId) {
        return 0;
    }

    @Override
    public HighFrequenceResponseDTO getHighFrequenceInfo() {
        return null;
    }

    @Override
    public StrangerResponseDTO getStrangerInfo() {
        return null;
    }

    @Override
    public String getBispectrumUrl() {
        return null;
    }

    @Override
    public WarningResponseDTO getWarningInfo() {
        return null;
    }

    @Override
    public List<DataChangeDTO> getDataChangeForFire() {
        return null;
    }

    @Override
    public List<DataRealTimeDTO> getRealTimeDataForFire() {
        return null;
    }

    @Override
    public TerminalResponseDTO getTerminalInfo() {
        return null;
    }

    @Override
    public List<MonitorData> getMonitorData() {
        return null;
    }

    @Override
    public MonitorData getMonitorDataByType(String type) {
        return null;
    }
}
