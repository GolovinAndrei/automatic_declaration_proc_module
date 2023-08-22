package by.btslogistics.autorelease.service.releasedocument.steps.checkswithapplieddocsbyproc;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.dto.quota.QuotaReportDto;
import by.btslogistics.autorelease.service.read.quota.QuotaReportReadService;
import by.btslogistics.autorelease.service.read.risks.CaseCustomControlReadService;
import by.btslogistics.autorelease.service.releasedocument.steps.RegistrationDocument;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;
import by.btslogistics.autorelease.web.rest.proxyfeign.FlkResultRestProxy;
import by.btslogistics.autorelease.web.rest.proxyfeign.PaymentResultRestProxy;
import eec.m.ca.complexdataobjects.v1_5_7.DeclarationGoodsItemDetailsType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Контроль и анализ протоколов сторонних модулей для процедуры с кодом 40
 */
@Slf4j
public class ChecksForAppliedDocsForProc40 extends RegistrationDocument {

    private final CommonChecksForAppliedDocs commonChecksForAppliedDocs;

    private final QuotaReportReadService quotaReportReadService;

    private final CommonReportSaveService commonReportSaveService;

    public ChecksForAppliedDocsForProc40(FlkResultRestProxy flkResultRestProxy, PaymentResultRestProxy paymentResultRestProxy, CaseCustomControlReadService riskReadService,
                                         CommonReportSaveService commonReportSaveService, QuotaReportReadService quotaReportReadService) {
        this.commonChecksForAppliedDocs = new CommonChecksForAppliedDocs(flkResultRestProxy, paymentResultRestProxy, riskReadService, commonReportSaveService);
        this.quotaReportReadService = quotaReportReadService;
        this.commonReportSaveService = commonReportSaveService;
    }

    public RegistrationDto doStep(RegistrationDto dto) {

        //ФЛК
        if (!commonChecksForAppliedDocs.getFlkResult(dto)) return dto;

        //Платежи
        if (!commonChecksForAppliedDocs.getPaymentResult(dto)) return dto;

        //Риски
        if (!commonChecksForAppliedDocs.getRisksResult(dto)) return dto;

        log.debug("Quota report is being analyzing");
        if (dto.getGoodsDeclarationTypeNew() != null) {
            by.btslogistics.xsdclasses.eec.v1_4_0.eec.r._036.goodsdeclaration.v1_4_0.GoodsDeclarationType newDoc = dto.getGoodsDeclarationTypeNew();

            if (newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails() != null &&
                    !newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails().isEmpty()) {
                List<by.btslogistics.xsdclasses.eec.v1_4_0.eec.m.ca.complexdataobjects.v1_5_8.DeclarationGoodsItemDetailsType> goodsItemList = newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails();

                //Квоты
                List<QuotaReportDto> quotaReports = quotaReportReadService.getReportByMessageLogId(dto.getMessageLog().getId());
                if (quotaReports!=null && !quotaReports.isEmpty()) {
                    if (!quotaReports.stream().allMatch(quota -> quota.getResultCode() == 1)) {
                        log.debug("Отчет по квотам не пустой и codeResult не 1!");
                        commonReportSaveService.addNewReport("39", "8.1", "Отчет по квотам не пустой и codeResul не равен 1");
                        return dto;
                    }
                    for (by.btslogistics.xsdclasses.eec.v1_4_0.eec.m.ca.complexdataobjects.v1_5_8.DeclarationGoodsItemDetailsType goodItem : goodsItemList) {

                        //8.1. Если в гр. 44 «Дополнительная информация/Предоставленные документы» есть документы под кодами 01011 и (или) 01211, то гр. 39 «Квота» должна быть заполнена.
                        if (goodItem.getPresentedDocDetails().stream().anyMatch(presentedDoc -> presentedDoc.getDocKindCode().getValue().equals("01011") ||
                                presentedDoc.getDocKindCode().getValue().equals("01211"))) {
                            if (goodItem.getQuotaDetails() == null) {
                                log.debug("Для товара {} с представленными документами 01011 и 01211 не заполнен раздел квот!", goodItem.getCommodityCode());
                                commonReportSaveService.addNewReport("39", "8.1", "Графа квот не заполнена должным образом для товара № " + goodItem.getCommodityCode());
                                return dto;
                            }
                        }
                    }
                }
            }
        } else {
            eec.r._036.goodsdeclaration.v1_3_1.GoodsDeclarationType oldDoc = dto.getGoodsDeclarationTypeOld();

            if (oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails() != null &&
                    !oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails().isEmpty()) {
                List<DeclarationGoodsItemDetailsType> goodsItemList = oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails();

                //Квоты
                List<QuotaReportDto> quotaReports = quotaReportReadService.getReportByMessageLogId(dto.getMessageLog().getId());
                if (quotaReports!=null && !quotaReports.isEmpty()) {
                    if (!quotaReports.stream().allMatch(quota -> quota.getResultCode() == 1)) {
                        log.debug("Отчет по квотам не пустой и codeResult не 1!");
                        commonReportSaveService.addNewReport("39", "8.1", "Отчет по квотам не пустой и codeResul не равен 1");
                        return dto;
                    }
                    for (DeclarationGoodsItemDetailsType goodItem : goodsItemList) {

                        //8.1. Если в гр. 44 «Дополнительная информация/Предоставленные документы» есть документы под кодами 01011 и (или) 01211, то гр. 39 «Квота» должна быть заполнена.
                        if (goodItem.getPresentedDocDetails().stream().anyMatch(presentedDoc -> presentedDoc.getDocKindCode().getValue().equals("01011") ||
                                presentedDoc.getDocKindCode().getValue().equals("01211"))) {
                            if (goodItem.getQuotaDetails() == null) {
                                log.debug("Для товара {} с представленными документами 01011 и 01211 не заполнен раздел квот!", goodItem.getCommodityCode());
                                commonReportSaveService.addNewReport("39", "8.1", "Графа квот не заполнена должным образом для товара № " + goodItem.getCommodityCode());
                                return dto;
                            }
                        }
                    }
                }
            }
        }
        log.debug("Second check block passed");
        dto.setChecksWithAppliedDocsPassed(true);
        return nextStep(dto);
    }
}