package com.bang.ap.dp;

import com.bang.ap.dp.analysis.service.DataPesistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class OnCommandLineRunner implements CommandLineRunner {
    @Autowired
    private DataPesistenceService dataPesistenceService;

    @Override
    public void run(String... args) {
        try {
            log.info("OnCommandLineRunner:");
            //do things about stranger
            dataPesistenceService.saveStrangerInfo(new Date());
            //do things about importantPeopleInfo
            dataPesistenceService.saveImportantPeopleInfo(new Date());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}