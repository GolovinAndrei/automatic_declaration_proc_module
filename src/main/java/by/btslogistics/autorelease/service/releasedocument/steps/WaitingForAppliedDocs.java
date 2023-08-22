package by.btslogistics.autorelease.service.releasedocument.steps;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.dto.messageslogstage.MessagesLogStageDto;
import by.btslogistics.autorelease.service.read.messageslogstage.MessagesLogStageReadService;

import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;

import by.btslogistics.commons.dao.dto.messageslogstage.MessagesLogStageDefaultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static by.btslogistics.autorelease.service.releasedocument.steps.ElementaryChecks.*;
import static by.btslogistics.commons.dao.enums.Stage.*;

/**
 * Этап ожидание формирования сопутствующих документов
 */
public class WaitingForAppliedDocs extends RegistrationDocument {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaitingForAppliedDocs.class);

    private final MessagesLogStageReadService messagesLogStageReadService;

    private final CommonReportSaveService commonReportSaveService;

    private final int timeOut;

    public WaitingForAppliedDocs(MessagesLogStageReadService messagesLogStageReadService, CommonReportSaveService commonReportSaveService, int timeOut) {
        this.messagesLogStageReadService = messagesLogStageReadService;
        this.commonReportSaveService = commonReportSaveService;
        this.timeOut = timeOut;
    }

    @Override
    public RegistrationDto doStep(RegistrationDto dto) {
        int numberOfSteps = (int) (Math.abs(Math.sqrt(0.25 + 2 * timeOut)) - 0.5);
        LOGGER.debug("Waiting for finishing other modules");
        Set<String> expectedStages = chooseSetOfStages(dto.getMessageLog().getCodeProc());
        try {
            for (int i = 0; i <= numberOfSteps; i++) {
                LOGGER.debug("Sleeps {} minutes", i);
                TimeUnit.MINUTES.sleep(i);
                List<MessagesLogStageDto> messagesLogStageList = messagesLogStageReadService.getStagesByMessageLogId(dto.getMessageLog());
                if (messagesLogStageList != null && !messagesLogStageList.isEmpty()) {
                    Set<String> actualStages = messagesLogStageList.stream().map(MessagesLogStageDefaultDto::getStageDoc).collect(Collectors.toSet());
                    expectedStages.removeAll(actualStages);
                    if (expectedStages.isEmpty()) {
                      LOGGER.debug("Done! There are all reports!");
                        return nextStep(dto);
                    }
                }
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }

        commonReportSaveService.addNewReport(null, null, "Не завершены следующие этапы: "+ expectedStages);
        LOGGER.debug("The waiting time is too long! Process has been stopped!");
        return dto;
    }

    private Set<String> chooseSetOfStages (String proc) {
        String [] arrOfStages = null;
        switch (proc){
            case CODE_CUSTOM_PROCEDURE_1:
            case CODE_CUSTOM_PROCEDURE_2:
                arrOfStages = new String[] { FLC_PROCESSED2.getStatus(),
                        CUSTOM_PAYMENT_PROCESSED.getStatus(),
                        PASSED_THE_ZIO_STAGE.getStatus(),
                        PASSED_THE_OIS_STAGE.getStatus(),
                        PASSED_THE_QUOTAS_STAGE.getStatus(),
                        PASSED_THE_PROFILE_STAGE.getStatus()};
                break;
            case CODE_CUSTOM_PROCEDURE_3:
            case CODE_CUSTOM_PROCEDURE_4:
            case CODE_CUSTOM_PROCEDURE_5:
                arrOfStages = new String[] { FLC_PROCESSED2.getStatus(),
                        CUSTOM_PAYMENT_PROCESSED.getStatus(),
                        PASSED_THE_ZIO_STAGE.getStatus()};

                break;
        }

        return new HashSet<>(Arrays.asList(arrOfStages));
    }
}
