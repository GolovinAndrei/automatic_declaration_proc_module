package by.btslogistics.autorelease.service.releasedocument.autorelease.impl;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import by.btslogistics.autorelease.service.read.messageslogstage.MessagesLogStageReadService;
import by.btslogistics.autorelease.service.read.ois.OisReportReadService;
import by.btslogistics.autorelease.service.read.prohibitrestrictreport.ProhibitRestrictReportReadService;
import by.btslogistics.autorelease.service.read.quota.QuotaReportReadService;
import by.btslogistics.autorelease.service.read.risks.CaseCustomControlReadService;
import by.btslogistics.autorelease.service.releasedocument.autorelease.AutoRelease;
import by.btslogistics.autorelease.service.releasedocument.createregistrationnumber.CreateRegistrationNumberService;
import by.btslogistics.autorelease.service.releasedocument.message.SendMessagesToMarsService;
import by.btslogistics.autorelease.service.releasedocument.steps.*;
import by.btslogistics.autorelease.service.releasedocument.unloaddocument.SendToAisUtpService;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;
import by.btslogistics.autorelease.service.save.messageslogstage.MessagesLogStageSaveService;
import by.btslogistics.autorelease.service.unmarshaller.UnmarshallerService;
import by.btslogistics.autorelease.service.update.messageLog.MessageLogUpdateService;
import by.btslogistics.autorelease.service.update.messageslogcustompart.MessagesLogCustomPartUpdateService;
import by.btslogistics.autorelease.service.update.messlogcustpartuploadstatus.MessagesLogCustomPartUploadStatusSaveService;
import by.btslogistics.autorelease.web.rest.proxyfeign.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static by.btslogistics.commons.dao.enums.Stage.AB_PROCESSED;
import static by.btslogistics.commons.dao.enums.Stage.AB_STAGE;

/**
 * Основная цепочка Автовыпуска
 */
@Service
public class AutoReleaseImpl implements AutoRelease {

    private final CreateRegistrationNumberService createRegistrationNumberService;

    private final MessageLogUpdateService messageLogUpdateService;

    private final SendMessagesToMarsService sendMessagesToMarsService;

    private final UnmarshallerService unmarshallerService;

    private final MessagesLogStageReadService messagesLogStageReadService;

    private final FlkResultRestProxy flkResultRestProxy;

    private final RegistrationSvxRestProxy svxRestProxy;

    private final MessagesLogStageSaveService messagesLogStageSaveService;

    private final PaymentResultRestProxy paymentResultRestProxy;

    private final QuotaReportReadService quotaReportReadService;

    private final ProhibitRestrictReportReadService prohibitRestrictReportReadService;

    private final HelperRestProxy helperRestProxy;

    private final ZioRestProxy zioRestProxy;

    private final MessagesLogCustomPartUpdateService messagesLogCustomPartUpdateService;

    private final CaseCustomControlReadService riskReadService;

    private final OisReportReadService oisReportReadService;

    private final CommonReportSaveService commonReportSaveService;

    private final KstpRestProxy kstpRestProxy;

    private final SendToAisUtpService sendToAisUtpService;

    private final MessagesLogCustomPartUploadStatusSaveService messagesLogCustomPartUploadStatusSaveService;
    /**
     * Вкл/выкл. блок проверок в цепочке автовыпуска. Если блок проверок выкл. (false), то результат ожидает всех протоколов, минует все проверки и уходит на АРТД
     */
    @Value("${autorelease-enable}")
    private Boolean isEnable;

    /**
     * Максимальное время ожидания отработки иных модулей и их протоколов (мин.)
     */
    @Value("${waiting-for-applied-docs.timeout}")
    private int timeOut;


    @Autowired
    public AutoReleaseImpl(CreateRegistrationNumberService createRegistrationNumberService,
                           MessageLogUpdateService updateService,
                           SendMessagesToMarsService sendMessagesToMarsService,
                           UnmarshallerService unmarshallerService,
                           FlkResultRestProxy flkResultRestProxy,
                           RegistrationSvxRestProxy svxRestProxy,
                           MessagesLogStageReadService messagesLogStageReadService,
                           MessagesLogStageSaveService messagesLogStageSaveService,
                           PaymentResultRestProxy paymentResultRestProxy,
                           QuotaReportReadService quotaReportReadService,
                           CaseCustomControlReadService riskReadService,
                           ProhibitRestrictReportReadService prohibitRestrictReportReadService,
                           HelperRestProxy helperRestProxy,
                           ZioRestProxy zioRestProxy,
                           OisReportReadService oisReportReadService,
                           MessagesLogCustomPartUpdateService messagesLogCustomPartUpdateService,
                           CommonReportSaveService commonReportSaveService,
                           KstpRestProxy kstpRestProxy,
                           SendToAisUtpService sendToAisUtpService,
                           MessagesLogCustomPartUploadStatusSaveService messagesLogCustomPartUploadStatusSaveService) {
        this.createRegistrationNumberService = createRegistrationNumberService;
        this.messageLogUpdateService = updateService;
        this.sendMessagesToMarsService = sendMessagesToMarsService;
        this.unmarshallerService = unmarshallerService;
        this.flkResultRestProxy = flkResultRestProxy;
        this.svxRestProxy = svxRestProxy;
        this.messagesLogStageReadService = messagesLogStageReadService;
        this.messagesLogStageSaveService = messagesLogStageSaveService;
        this.paymentResultRestProxy = paymentResultRestProxy;
        this.quotaReportReadService = quotaReportReadService;
        this.riskReadService = riskReadService;
        this.prohibitRestrictReportReadService = prohibitRestrictReportReadService;
        this.helperRestProxy = helperRestProxy;
        this.zioRestProxy = zioRestProxy;
        this.oisReportReadService = oisReportReadService;
        this.messagesLogCustomPartUpdateService = messagesLogCustomPartUpdateService;
        this.commonReportSaveService = commonReportSaveService;
        this.kstpRestProxy = kstpRestProxy;
        this.sendToAisUtpService = sendToAisUtpService;
        this.messagesLogCustomPartUploadStatusSaveService = messagesLogCustomPartUploadStatusSaveService;
    }

    @Override
    public void getRegistered(MessageLogDto messagesLog) {

        messagesLogStageSaveService.addStageRec(AB_STAGE, messagesLog.getId());
        commonReportSaveService.setDocumentId(messagesLog.getId());
        RegistrationDto registrationDto = new RegistrationDto(messagesLog);
        RegistrationDocument registration;
        if (isEnable) {
            registration = new UnmarshallerDtStep(unmarshallerService);
            registration
                    .linkWith(new ElementaryChecks(helperRestProxy, commonReportSaveService))
                    .linkWith(new WaitingForAppliedDocs(messagesLogStageReadService, commonReportSaveService, timeOut))
                    .linkWith(new ChecksWithAppliedDocs(paymentResultRestProxy, quotaReportReadService, zioRestProxy,
                            prohibitRestrictReportReadService, helperRestProxy, oisReportReadService, flkResultRestProxy, riskReadService, commonReportSaveService))
                    .linkWith(new AutoReleaseStep(createRegistrationNumberService, sendMessagesToMarsService, messageLogUpdateService, messagesLogCustomPartUpdateService, messagesLogCustomPartUploadStatusSaveService))
                    .linkWith(new SvxStep(svxRestProxy, messagesLogStageSaveService))
                    .linkWith(new AfterAutoreleaseActions(kstpRestProxy, sendToAisUtpService));

        } else {
            registration = new WaitingForAppliedDocs(messagesLogStageReadService, commonReportSaveService, timeOut);
        }
        registration.doStep(registrationDto);
        messagesLogStageSaveService.addStageRec(AB_PROCESSED, messagesLog.getId());
    }

}