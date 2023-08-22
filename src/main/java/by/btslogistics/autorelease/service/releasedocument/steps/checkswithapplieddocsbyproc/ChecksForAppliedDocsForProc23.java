package by.btslogistics.autorelease.service.releasedocument.steps.checkswithapplieddocsbyproc;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.dto.prohibitrestrictreport.ProhibitRestrictReportDto;
import by.btslogistics.autorelease.service.read.prohibitrestrictreport.ProhibitRestrictReportReadService;
import by.btslogistics.autorelease.service.releasedocument.steps.RegistrationDocument;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;
import by.btslogistics.autorelease.web.rest.proxyfeign.FlkResultRestProxy;
import by.btslogistics.autorelease.web.rest.proxyfeign.HelperRestProxy;
import by.btslogistics.autorelease.web.rest.proxyfeign.PaymentResultRestProxy;
import by.btslogistics.commons.dto.nsi.NsiReestrUoByDto;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Контроль и анализ протоколов сторонних модулей для процедуры с кодом 23
 */
@Slf4j
public class ChecksForAppliedDocsForProc23 extends RegistrationDocument {

    private final CommonChecksForAppliedDocs commonChecksForAppliedDocs;

    private final ProhibitRestrictReportReadService prohibitRestrictReportReadService;

    private final HelperRestProxy helperRestProxy;

    public static final String NSI_CATALOG_ID = "00000000757773";

    public static final String GVPK = "ГВПК";

    public static final String TYPE_OF_NUM_R = "ТИП3";

    private final CommonReportSaveService commonReportSaveService;


    public ChecksForAppliedDocsForProc23(FlkResultRestProxy flkResultRestProxy, HelperRestProxy helperRestProxy, ProhibitRestrictReportReadService prohibitRestrictReportReadService,
                                         CommonReportSaveService commonReportSaveService, PaymentResultRestProxy paymentResultRestProxy) {
        this.commonChecksForAppliedDocs = new CommonChecksForAppliedDocs(flkResultRestProxy, paymentResultRestProxy,  commonReportSaveService);
        this.helperRestProxy = helperRestProxy;
        this.prohibitRestrictReportReadService = prohibitRestrictReportReadService;
        this.commonReportSaveService = commonReportSaveService;
    }

    @Override
    public RegistrationDto doStep(RegistrationDto dto) {

        //ФЛК
        if (commonChecksForAppliedDocs.getFlkResult(dto)) {

            //Платежи
            if (commonChecksForAppliedDocs.getPaymentResult(dto)) {

                if (dto.getGoodsDeclarationTypeNew() != null) {
                    by.btslogistics.xsdclasses.eec.v1_4_0.eec.r._036.goodsdeclaration.v1_4_0.GoodsDeclarationType newDoc = dto.getGoodsDeclarationTypeNew();

                    if (newDoc.getDeclarationGoodsShipmentDetails() != null && newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails() != null &&
                            !newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails().isEmpty()) {

                        //ЗИО
                        log.debug("ZIO report is being analyzed");
                        List<ProhibitRestrictReportDto> prohibitRestrictReportList = prohibitRestrictReportReadService.getProhibitRestrictReportByMessageLogId(dto.getMessageLog().getId());
                        if (prohibitRestrictReportList != null) {
                            for (ProhibitRestrictReportDto p : prohibitRestrictReportList) {

                                if (p.getNsiCatalogId().equals(NSI_CATALOG_ID) && !commonChecksForAppliedDocs.doesHaveItemProhibitionFreeCodeC(dto, p.getGoodsNumeric())) {
                                    log.debug("There is not sign \"C\" for NSI_CATALOG_ID {}. Autorelease have canceled!", NSI_CATALOG_ID);
                                    commonReportSaveService.addNewReport("33", "4.1", "Отсутствует признак С для товара из перечня культурных ценностей");
                                    return dto;
                                }
                                try {
                                    if (helperRestProxy.getNsiCatalogById(p.getNsiCatalogId()).getNameIn().contains(GVPK)) {
                                        if (!commonChecksForAppliedDocs.doesHaveItemProhibitionFreeCodeC(dto, p.getGoodsNumeric()) || !hasUnpType3(newDoc.getDeclarantDetails().getTaxpayerId())) {
                                            log.debug("There is not UNP in NSI_REESTR_UO_BY or there is tupe 3 of this UNP");
                                            commonReportSaveService.addNewReport("33", "4.2", "Отсутствует признак УЭО третьего типа или отсутствует признак С для товаролв ГВПК");
                                            return dto;
                                        }
                                    }
                                } catch (RetryableException e) {
                                    e.printStackTrace();
                                    commonReportSaveService.addNewReport("33", "4.2", "Helper не отвечает!");
                                    return dto;
                                }
                            }
                        }
                        log.debug("ZIO - ok");
                        dto.setChecksWithAppliedDocsPassed(true);
                        return nextStep(dto);
                    }
                    log.debug("DT doesn't have goods!");

                } else {
                    eec.r._036.goodsdeclaration.v1_3_1.GoodsDeclarationType oldDoc = dto.getGoodsDeclarationTypeOld();

                    if (oldDoc.getDeclarationGoodsShipmentDetails() != null && oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails() != null &&
                            !oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails().isEmpty()) {

                        //ЗИО
                        log.debug("ZIO report is being analyzed");
                        List<ProhibitRestrictReportDto> prohibitRestrictReportList = prohibitRestrictReportReadService.getProhibitRestrictReportByMessageLogId(dto.getMessageLog().getId());
                        if (prohibitRestrictReportList != null) {
                            for (ProhibitRestrictReportDto p : prohibitRestrictReportList) {

                                if (p.getNsiCatalogId().equals(NSI_CATALOG_ID) && !commonChecksForAppliedDocs.doesHaveItemProhibitionFreeCodeC(dto, p.getGoodsNumeric())) {
                                    log.debug("There is not sign \"C\" for NSI_CATALOG_ID {}. Autorelease have canceled!", NSI_CATALOG_ID);
                                    commonReportSaveService.addNewReport("33", "4.1", "Отсутствует признак С для товара из перечня культурных ценностей");
                                    return dto;
                                }
                                try {
                                    if (helperRestProxy.getNsiCatalogById(p.getNsiCatalogId()).getNameIn().contains(GVPK)) {
                                        if (!commonChecksForAppliedDocs.doesHaveItemProhibitionFreeCodeC(dto, p.getGoodsNumeric()) || !hasUnpType3(oldDoc.getDeclarantDetails().getTaxpayerId())) {
                                            log.debug("There is not UNP in NSI_REESTR_UO_BY or there is tupe 3 of this UNP");
                                            commonReportSaveService.addNewReport("33", "4.2", "Отсутствует признак УЭО третьего типа или отсутствует признак С для товаролв ГВПК");
                                            return dto;
                                        }
                                    }
                                } catch (RetryableException e) {
                                    e.printStackTrace();
                                    commonReportSaveService.addNewReport("33", "4.2", "Helper не отвечает!");
                                    return dto;
                                }
                            }
                        }
                        log.debug("ZIO - ok");
                        dto.setChecksWithAppliedDocsPassed(true);
                        return nextStep(dto);
                    }
                }
            }
        }
        log.debug("DT doesn't have goods!");
        return dto;
    }

    public boolean hasUnpType3(String unn) throws RetryableException {

        NsiReestrUoByDto nsiReestrUoByDto = helperRestProxy.getNsiReestrUoByByUnn(unn);
        if (nsiReestrUoByDto.getNumR() != null) {
            return nsiReestrUoByDto.getNumR().endsWith(TYPE_OF_NUM_R) && nsiReestrUoByDto.getdOn().isBefore(LocalDateTime.now()) &&
                    nsiReestrUoByDto.getdOff().isAfter(LocalDateTime.now());
        }
        return false;
    }
}
