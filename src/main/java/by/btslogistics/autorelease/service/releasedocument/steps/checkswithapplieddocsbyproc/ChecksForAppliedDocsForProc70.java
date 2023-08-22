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
 * Контроль и анализ протоколов сторонних модулей для процедуры с кодом 70
 */
@Slf4j
public class ChecksForAppliedDocsForProc70 extends RegistrationDocument {

    private final CommonChecksForAppliedDocs commonChecksForAppliedDocs;

    private final ProhibitRestrictReportReadService prohibitRestrictReportReadService;

    private final CommonReportSaveService commonReportSaveService;

    private final static int VALID_CODE_VALUE = 3;


    public ChecksForAppliedDocsForProc70(FlkResultRestProxy flkResultRestProxy, PaymentResultRestProxy paymentResultRestProxy,
                                         ProhibitRestrictReportReadService prohibitRestrictReportReadService, CommonReportSaveService commonReportSaveService) {
        this.commonReportSaveService = commonReportSaveService;
        this.commonChecksForAppliedDocs = new CommonChecksForAppliedDocs(flkResultRestProxy, paymentResultRestProxy, commonReportSaveService);
        this.prohibitRestrictReportReadService = prohibitRestrictReportReadService;
    }

    public RegistrationDto doStep(RegistrationDto dto) {

        //ФЛК
        if (commonChecksForAppliedDocs.getFlkResult(dto)) {
            //Платежи
            if (commonChecksForAppliedDocs.getPaymentResult(dto)) {

                    //ЗиО
                    log.debug("ZIO report is being analyzed");
                    List<ProhibitRestrictReportDto> prohibitRestrictReportList = prohibitRestrictReportReadService.getProhibitRestrictReportByMessageLogId(dto.getMessageLog().getId());
                    if (prohibitRestrictReportList == null || prohibitRestrictReportList.isEmpty() ||
                            prohibitRestrictReportList.stream()
                                    .allMatch(prohibitRestrictReportDto -> prohibitRestrictReportDto.getCodeResult().equals(VALID_CODE_VALUE)&&prohibitRestrictReportDto.getIntermediateResult()==null)) {
                        log.debug("ZIO - ok");
                            dto.setChecksWithAppliedDocsPassed(true);
                            return nextStep(dto);
                        } else {
                            commonReportSaveService.addNewReport(null, "3.1", "В отношении товаров применены запреты и ограничения");
                            log.debug("The value of NSI_CATALOG_ID is not in required list. Autorelease have canceled!(10)");
                        }
                    }
                }
        return dto;
    }
}