package by.btslogistics.autorelease.service.releasedocument.steps.checkswithapplieddocsbyproc;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.dto.prohibitrestrictreport.ProhibitRestrictReportDto;
import by.btslogistics.autorelease.service.read.prohibitrestrictreport.ProhibitRestrictReportReadService;
import by.btslogistics.autorelease.service.releasedocument.steps.RegistrationDocument;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;
import by.btslogistics.autorelease.web.rest.proxyfeign.FlkResultRestProxy;
import by.btslogistics.autorelease.web.rest.proxyfeign.PaymentResultRestProxy;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Контроль и анализ протоколов сторонних модулей для процедуры с кодом 96
 */
@Slf4j
public class ChecksForAppliedDocsForProc96 extends RegistrationDocument {

    private final CommonChecksForAppliedDocs commonChecksForAppliedDocs;

    private final ProhibitRestrictReportReadService prohibitRestrictReportReadService;

    private final CommonReportSaveService commonReportSaveService;


    public ChecksForAppliedDocsForProc96(FlkResultRestProxy flkResultRestProxy, PaymentResultRestProxy paymentResultRestProxy, CommonReportSaveService commonReportSaveService,
                                         ProhibitRestrictReportReadService prohibitRestrictReportReadService) {
        this.commonChecksForAppliedDocs = new CommonChecksForAppliedDocs(flkResultRestProxy, paymentResultRestProxy, commonReportSaveService);
        this.commonReportSaveService = commonReportSaveService;
        this.prohibitRestrictReportReadService = prohibitRestrictReportReadService;

    }
    @Override
    public RegistrationDto doStep(RegistrationDto dto) {

        //ФЛК
        if (!commonChecksForAppliedDocs.getFlkResult(dto)) return dto;

        //Платежи
        if (!commonChecksForAppliedDocs.getPaymentResult(dto)) return dto;

        //ЗиО
        log.debug("ZIO report is being analyzed (4)");
        List<ProhibitRestrictReportDto> prohibitRestrictReportList = prohibitRestrictReportReadService.getProhibitRestrictReportByMessageLogId(dto.getMessageLog().getId());
        if (prohibitRestrictReportList != null && !prohibitRestrictReportList.isEmpty()) {

            for (ProhibitRestrictReportDto p : prohibitRestrictReportList) {

                if (!commonChecksForAppliedDocs.doesHaveItemProhibitionFreeCodeC(dto, p.getGoodsNumeric())) {

                    if (p.getCodeResult()!= 3 || p.getIntermediateResult()!=null) {
                        log.debug("ZiO protocol has errors. Autorelease has canceled");
                        commonReportSaveService.addNewReport("", "4.2", "В отношении товаров применены запреты и ограничения");
                        return dto;
                    }
                }
            }
        }
        dto.setChecksWithAppliedDocsPassed(true);
        return nextStep(dto);
    }
}