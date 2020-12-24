package com.bang.ap.dp.analysis.service;

import java.util.Date;

public interface DataPesistenceService {

    void saveFrequenceInRoom(Date date);

    void saveRoomUseTimeLength(Date date);
}
