package by.btslogistics.autorelease.service.releasedocument.steps.checkswithapplieddocsbyproc;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.dto.prohibitrestrictreport.ProhibitRestrictReportDto;
import by.btslogistics.autorelease.service.read.prohibitrestrictreport.ProhibitRestrictReportReadService;
import by.btslogistics.autorelease.service.releasedocument.steps.RegistrationDocument;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;
import by.btslogistics.autorelease.web.rest.proxyfeign.FlkResultRestProxy;
import by.btslogistics.autorelease.web.rest.proxyfeign.HelperRestProxy;
import by.btslogistics.autorelease.web.rest.proxyfeign.PaymentResultRestProxy;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ChecksForAppliedDocsForProc78 extends RegistrationDocument {

    private final CommonChecksForAppliedDocs commonChecksForAppliedDocs;

    private final HelperRestProxy helperRestProxy;

    private final CommonReportSaveService commonReportSaveService;

    private final ProhibitRestrictReportReadService prohibitRestrictReportReadService;

    public ChecksForAppliedDocsForProc78(FlkResultRestProxy flkResultRestProxy, PaymentResultRestProxy paymentResultRestProxy, CommonReportSaveService commonReportSaveService,
                                         HelperRestProxy helperRestProxy, ProhibitRestrictReportReadService prohibitRestrictReportReadService) {
        this.commonChecksForAppliedDocs = new CommonChecksForAppliedDocs(flkResultRestProxy, paymentResultRestProxy, commonReportSaveService);
        this.helperRestProxy = helperRestProxy;
        this.prohibitRestrictReportReadService = prohibitRestrictReportReadService;
        this.commonReportSaveService = commonReportSaveService;
    }


    @Override
    public RegistrationDto doStep(RegistrationDto dto) {

        //ФЛК
        if (!commonChecksForAppliedDocs.getFlkResult(dto)) return dto;

        boolean isThereUnpInReestrFirm = helperRestProxy.getUnpFromReestrFirm(dto.getGoodsDeclarationTypeNew().getDeclarantDetails().getTaxpayerId()).iterator().hasNext();
        log.info("check 6.1");
        List<ProhibitRestrictReportDto> prohibitRestrictReportList = prohibitRestrictReportReadService.getProhibitRestrictReportByMessageLogId(dto.getMessageLog().getId());
        if (prohibitRestrictReportList != null && !prohibitRestrictReportList.isEmpty()) {

            for (ProhibitRestrictReportDto p : prohibitRestrictReportList) {

                if (!commonChecksForAppliedDocs.doesHaveItemProhibitionFreeCodeC(dto, p.getGoodsNumeric()) || !isThereUnpInReestrFirm) {
                    log.info("check 6.2");
                    if (p.getCodeResult() != 3 || p.getIntermediateResult() != null) {
                        log.debug("ZiO protocol has errors. Autorelease has canceled");
                        commonReportSaveService.addNewReport("33", "6.2", "В отношении товаров применены запреты и ограничения");
                        return dto;
                    }
                }
            }
        }
        //Платежи & риски
        if (!commonChecksForAppliedDocs.getPaymentResult(dto) || !commonChecksForAppliedDocs.getRisksResult(dto))
            return dto;
        dto.setChecksWithAppliedDocsPassed(true);
        return nextStep(dto);
    }
}
