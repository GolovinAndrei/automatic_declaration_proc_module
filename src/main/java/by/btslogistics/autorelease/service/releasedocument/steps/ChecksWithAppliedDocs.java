package by.btslogistics.autorelease.service.releasedocument.steps;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.read.ois.OisReportReadService;
import by.btslogistics.autorelease.service.read.prohibitrestrictreport.ProhibitRestrictReportReadService;
import by.btslogistics.autorelease.service.read.quota.QuotaReportReadService;
import by.btslogistics.autorelease.service.read.risks.CaseCustomControlReadService;
import by.btslogistics.autorelease.service.releasedocument.steps.checkswithapplieddocsbyproc.*;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;
import by.btslogistics.autorelease.web.rest.proxyfeign.*;

/**
 * Цепочка проверок и анализа протоколов сторонних модулей
 */
public class ChecksWithAppliedDocs extends RegistrationDocument {

    private final PaymentResultRestProxy paymentResultRestProxy;

    private final QuotaReportReadService quotaReportReadService;

    private final ZioRestProxy zioRestProxy;

    private final ProhibitRestrictReportReadService prohibitRestrictReportReadService;

    private final HelperRestProxy helperRestProxy;

    private final OisReportReadService oisReportReadService;

    private final FlkResultRestProxy flkResultRestProxy;

    private final CaseCustomControlReadService riskReadService;

    private final CommonReportSaveService commonReportSaveService;

    public ChecksWithAppliedDocs(PaymentResultRestProxy paymentResultRestProxy, QuotaReportReadService quotaReportReadService,
                                 ZioRestProxy zioRestProxy, ProhibitRestrictReportReadService prohibitRestrictReportReadService, HelperRestProxy helperRestProxy, OisReportReadService oisReportReadService,
                                 FlkResultRestProxy flkResultRestProxy, CaseCustomControlReadService riskReadService, CommonReportSaveService commonReportSaveService) {
        this.paymentResultRestProxy = paymentResultRestProxy;
        this.quotaReportReadService = quotaReportReadService;
        this.zioRestProxy = zioRestProxy;
        this.prohibitRestrictReportReadService = prohibitRestrictReportReadService;
        this.helperRestProxy = helperRestProxy;
        this.oisReportReadService = oisReportReadService;
        this.flkResultRestProxy = flkResultRestProxy;
        this.riskReadService = riskReadService;
        this.commonReportSaveService = commonReportSaveService;
    }

    @Override
    public RegistrationDto doStep(RegistrationDto dto) {

        switch (dto.getMessageLog().getCodeProc()) {

            case ElementaryChecks.CODE_CUSTOM_PROCEDURE_1:
                ChecksForAppliedDocsForProc10 checksForAppliedDocsForProc10 = new ChecksForAppliedDocsForProc10(paymentResultRestProxy, quotaReportReadService, zioRestProxy,
                        prohibitRestrictReportReadService, helperRestProxy, oisReportReadService, flkResultRestProxy, riskReadService, commonReportSaveService);
                checksForAppliedDocsForProc10.doStep(dto);
                break;

            case ElementaryChecks.CODE_CUSTOM_PROCEDURE_2:
                ChecksForAppliedDocsForProc40 checksForAppliedDocsForProc40 = new ChecksForAppliedDocsForProc40(flkResultRestProxy, paymentResultRestProxy, riskReadService, commonReportSaveService, quotaReportReadService);
                checksForAppliedDocsForProc40.doStep(dto);
                break;

            case ElementaryChecks.CODE_CUSTOM_PROCEDURE_3:
                ChecksForAppliedDocsForProc96 checksForAppliedDocsForProc96 = new ChecksForAppliedDocsForProc96(flkResultRestProxy, paymentResultRestProxy, commonReportSaveService, prohibitRestrictReportReadService);
                checksForAppliedDocsForProc96.doStep(dto);
                break;

            case ElementaryChecks.CODE_CUSTOM_PROCEDURE_4:
                ChecksForAppliedDocsForProc23 checksForAppliedDocsForProc23 = new ChecksForAppliedDocsForProc23(flkResultRestProxy,
                        helperRestProxy, prohibitRestrictReportReadService, commonReportSaveService, paymentResultRestProxy);
                checksForAppliedDocsForProc23.doStep(dto);
                break;

            case ElementaryChecks.CODE_CUSTOM_PROCEDURE_5:
                ChecksForAppliedDocsForProc70 checksForAppliedDocsForProc70 = new ChecksForAppliedDocsForProc70(flkResultRestProxy,
                        paymentResultRestProxy, prohibitRestrictReportReadService, commonReportSaveService);
                checksForAppliedDocsForProc70.doStep(dto);
                break;

            case ElementaryChecks.CODE_CUSTOM_PROCEDURE_6:
                ChecksForAppliedDocsForProc78 checksForAppliedDocsForProc78 = new ChecksForAppliedDocsForProc78(flkResultRestProxy, paymentResultRestProxy, commonReportSaveService, helperRestProxy,
                        prohibitRestrictReportReadService);
                checksForAppliedDocsForProc78.doStep(dto);
                break;
        }
        if (dto.isChecksWithAppliedDocsPassed()) {
            return nextStep(dto);
        }
        return dto;
    }
}

