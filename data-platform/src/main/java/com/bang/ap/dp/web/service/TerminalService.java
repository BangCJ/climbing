package com.bang.ap.dp.web.service;


import com.bang.ap.dp.utils.PageRequest;
import com.bang.ap.dp.utils.PageResult;
import com.bang.ap.dp.web.entity.TerminalInfo;

import java.util.Map;


public interface TerminalService {

    TerminalInfo getTerminalInfoById(int id);

    void addTerminalInfo(TerminalInfo TerminalInfo);

    void  updateTerminalInfo(TerminalInfo terminalInfo);

    PageResult findPage(PageRequest pageRequest, Map<String, Object> searchMap);

}
