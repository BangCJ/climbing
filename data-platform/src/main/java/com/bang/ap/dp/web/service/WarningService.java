package com.bang.ap.dp.web.service;


import com.bang.ap.dp.utils.PageRequest;
import com.bang.ap.dp.utils.PageResult;
import com.bang.ap.dp.web.entity.MonitorData;
import com.bang.ap.dp.web.entity.WarningInfo;

import java.util.Map;

public interface WarningService {

    WarningInfo getWarningInfoById(int id);

    void addWarningInfo(WarningInfo warningInfo);

    PageResult findPage(PageRequest pageRequest, Map<String, Object> searchMap);

    void checkDateForWarning(MonitorData monitorData);

}
