package by.btslogistics.autorelease.service.releasedocument.steps.elementarychecksbyproc;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.releasedocument.steps.RegistrationDocument;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;
import by.btslogistics.autorelease.web.rest.proxyfeign.HelperRestProxy;

import eec.m.ca.complexdataobjects.v1_5_7.DeclarationGoodsItemDetailsType;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * Первичные проверки для декларации с кодом процедуры 23
 */
@Slf4j
public class ElementaryChecksForProc23 extends RegistrationDocument {

    private final HelperRestProxy helperRestProxy;

    private final CommonReportSaveService commonReportSaveService;

    public static List<String> RETURNABLE_PACKAGING_CODES = Arrays.asList(
            "3921909000",
            "3923100000",
            "3923299000",
            "3923409000",
            "3923900000",
            "3925100000",
            "4415101000",
            "4415109000",
            "4415202000",
            "4415209000",
            "4823709000",
            "6305390000",
            "7308909809",
            "7309005900",
            "7310100000",
            "7326904000",
            "7326905000",
            "7326909807");

    public ElementaryChecksForProc23(HelperRestProxy helperRestProxy, CommonReportSaveService commonReportSaveService) {
        this.helperRestProxy = helperRestProxy;
        this.commonReportSaveService = commonReportSaveService;
    }

    @Override
    public RegistrationDto doStep(RegistrationDto dto) {

        if (dto.getGoodsDeclarationTypeNew() != null) {
            by.btslogistics.xsdclasses.eec.v1_4_0.eec.r._036.goodsdeclaration.v1_4_0.GoodsDeclarationType newDoc = dto.getGoodsDeclarationTypeNew();

            if (newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails() != null &&
                    !newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails().isEmpty()) {

                for (by.btslogistics.xsdclasses.eec.v1_4_0.eec.m.ca.complexdataobjects.v1_5_8.DeclarationGoodsItemDetailsType goodItem : newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails()) {

                    //3.1. Для кодов ТН ВЭД ЕАЭС, содержащихся в перечне товаров, являющихся многооборотной (возвратной) тарой.
                    if (RETURNABLE_PACKAGING_CODES.contains(goodItem.getCommodityCode())) {
                        if (goodItem.getCustomsProcedureDetails().getGoodsMoveFeatureCode().getValue().equals("027")) {
                            log.debug("Указан неверный код последующей процедуры для возвратной тары");
                            commonReportSaveService.addNewReport("37", "3.1", "Товар под процедурой временного вывоза не помечен как многооборотная тара");
                            return dto;
                        }
                    }

                    //Гр. 37 «Процедура». В первом подразделе графы элемент 1 указан «23», элемент 2 – «00».
                    if (!goodItem.getCustomsProcedureDetails().getCustomsProcedureCode().getValue().equals("23") || !goodItem.getCustomsProcedureDetails().getPreviousCustomsProcedureModeCode().getValue().equals("00")) {
                        log.debug("Код таможенной процедуры не 96 или присутствует предшествующая таможенная процедура.");
                        commonReportSaveService.addNewReport("37", "4.2", "Неверные коды в графе 37");
                        return dto;
                    }
                }
            }

            //Гр. 14 «Декларант». В правом верхнем углу графы после знака «N» указан УНП декларанта, имеющий в поле STATUS таблицы NAL_PLAT признак «1»
            try {
                String taxPayer = newDoc.getDeclarantDetails().getTaxpayerId();
                if (taxPayer != null) {
                    if (!helperRestProxy.getNsiNalPlatByUnp(taxPayer, "1")) {
                        log.debug("Декларирование осуществляется не юрлицом РБ!");
                        commonReportSaveService.addNewReport("14", "4.1", "Декларирование осуществляет не юр.лицо из РБ");
                        return dto;
                    }
                } else {
                    log.debug("Нет сведений о декларанте!");
                    commonReportSaveService.addNewReport("14", "4.1", "Нет сведений о декларанте");
                    return dto;
                }
            } catch (RetryableException e) {
                e.printStackTrace();
                commonReportSaveService.addNewReport("14", "4.1", "Helper не отвечает!");
                return dto;
            }

            log.debug("First checks block with criteria 4 have completed");
            dto.setElementaryChecksPassed(true);
            return nextStep(dto);

        } else {
            eec.r._036.goodsdeclaration.v1_3_1.GoodsDeclarationType oldDoc = dto.getGoodsDeclarationTypeOld();

            if (oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails() != null &&
                    !oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails().isEmpty()) {

                for (DeclarationGoodsItemDetailsType goodItem : oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails()) {

                    //3.1. Для кодов ТН ВЭД ЕАЭС, содержащихся в перечне товаров, являющихся многооборотной (возвратной) тарой.
                    if (RETURNABLE_PACKAGING_CODES.contains(goodItem.getCommodityCode())) {
                        if (goodItem.getCustomsProcedureDetails().getGoodsMoveFeatureCode().getValue().equals("027")) {
                            log.debug("Указан неверный код последующей процедуры для возвратной тары");
                            commonReportSaveService.addNewReport("37", "3.1", "Товар под процедурой временного вывоза не помечен как многооборотная тара");
                            return dto;
                        }
                    }

                    //Гр. 37 «Процедура». В первом подразделе графы элемент 1 указан «23», элемент 2 – «00».
                    if (!goodItem.getCustomsProcedureDetails().getCustomsProcedureCode().getValue().equals("23") || !goodItem.getCustomsProcedureDetails().getPreviousCustomsProcedureModeCode().getValue().equals("00")) {
                        log.debug("Код таможенной процедуры не 96 или присутствует предшествующая таможенная процедура.");
                        commonReportSaveService.addNewReport("37", "4.2", "Неверные коды в графе 37");
                        return dto;
                    }
                }
            }

            //Гр. 14 «Декларант». В правом верхнем углу графы после знака «N» указан УНП декларанта, имеющий в поле STATUS таблицы NAL_PLAT признак «1»
            try {
                String taxPayer = oldDoc.getDeclarantDetails().getTaxpayerId();
                if (taxPayer != null) {
                    if (!helperRestProxy.getNsiNalPlatByUnp(taxPayer, "1")) {
                        log.debug("Декларирование осуществляется не юрлицом РБ!");
                        commonReportSaveService.addNewReport("14", "4.1", "Декларирование осуществляет не юр.лицо из РБ");
                        return dto;
                    }
                } else {
                    log.debug("Нет сведений о декларанте!");
                    commonReportSaveService.addNewReport("14", "4.1", "Нет сведений о декларанте");
                    return dto;
                }
            } catch (RetryableException e) {
                e.printStackTrace();
                commonReportSaveService.addNewReport("14", "4.1", "Helper не отвечает!");
                return dto;
            }

            log.debug("First checks block with criteria 4 have completed");
            dto.setElementaryChecksPassed(true);
            return nextStep(dto);
        }
    }
}
