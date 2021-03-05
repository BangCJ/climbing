package com.bang.ap.dp.analysis.service;

import java.util.Date;

public interface DataPesistenceService {

    void saveFrequenceInRoom(Date date);

    void saveRoomUseTimeLength(Date date);

    void saveStrangerInfo(Date date);

    void saveImportantPeopleInfo(Date date);

    void asyncSaveStranger(Date date);

    void asyncSaveImportantPeople(Date date);

}
