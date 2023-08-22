package by.btslogistics.autorelease.service.releasedocument.steps.elementarychecksbyproc;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.releasedocument.steps.RegistrationDocument;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;
import by.btslogistics.autorelease.web.rest.proxyfeign.HelperRestProxy;
import eec.m.ca.complexdataobjects.v1_5_7.DeclarationGoodsItemDetailsType;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ElementaryChecksForProc78 extends RegistrationDocument {

    private final HelperRestProxy helperRestProxy;

    private final CommonReportSaveService commonReportSaveService;

    private static final Set<String> ptoSpecArr = Arrays.stream(new String[]{"072", "093", "136", "146", "164", "169", "170", "171"}).collect(Collectors.toSet());

    private static final String ID_OBJ_OF_GOODS_WITHOUT_AV = "00000004672228";

    private static final String USL_ID_FOR_CUSTOMS_UNION_COUNTRIES = "00000001214907";

    public ElementaryChecksForProc78(HelperRestProxy helperRestProxy, CommonReportSaveService commonReportSaveService) {
        this.helperRestProxy = helperRestProxy;
        this.commonReportSaveService = commonReportSaveService;
    }

    @Override
    public RegistrationDto doStep(RegistrationDto dto) {

        try {
            if (dto.getGoodsDeclarationTypeNew() != null) {
                by.btslogistics.xsdclasses.eec.v1_4_0.eec.r._036.goodsdeclaration.v1_4_0.GoodsDeclarationType newDoc = dto.getGoodsDeclarationTypeNew();

                //Гр. 14 «Декларант». В правом верхнем углу графы 14 ДТ после знака «№» указан УНП декларанта, имеющийся в представлении UNP_KCEZ
                if (newDoc.getDeclarantDetails() != null && newDoc.getDeclarantDetails().getTaxpayerId() != null) {
                    log.info("check 3.1");
                    if (!helperRestProxy.getUnpKcez(newDoc.getDeclarantDetails().getTaxpayerId())) {
                        log.debug("Helper has returned false from method getUnpKcez");
                        commonReportSaveService.addNewReport("14", "3.1", "Декларирование товаров осуществляется не резидентом свободной (особой, специальной) экономической зоны.");
                        return dto;
                    }
                }

                // Гр. 30 «Местонахождения товара». В гр. 30 ДТ указан номер зоны таможенного контроля, имеющийся в таблице NSI_ZTK
                if (newDoc.getDeclarationGoodsShipmentDetails() != null && newDoc.getDeclarationGoodsShipmentDetails().getGoodsLocationDetails() != null) {
                    String nomZtk = newDoc.getDeclarationGoodsShipmentDetails().getGoodsLocationDetails().getCustomsControlZoneId();
                    if (nomZtk != null) {
                        log.info("check 4.1");
                        if (!helperRestProxy.checkNomZtk(nomZtk, LocalDateTime.now())) {
                            log.debug("Helper has returned false from method checkNomZtk");
                            commonReportSaveService.addNewReport("30", "4.1", "Товары размещаются в незарегистрированной таможенной зоне.");
                            return dto;
                        }
                    }
                }

                //5.1. Проверка на наличие требуемой специализации ПТО: указанный в регистрационном номере ДТ код ПТО (3 символа) попадает
                // в перечень ПТО в таблице NSI_PTO_BR, отобранный по условию NSI_PTO_SPEC_BR CODE = (072,093,136,146,164,169,170,171).
                String pto = dto.getMessageLog().getPto();
                if (pto != null) {
                    log.info("check 5.1");
                    Iterable<String> specOfPto = helperRestProxy.getListOfSpecForPto(pto);
                    if (specOfPto != null && specOfPto.spliterator().getExactSizeIfKnown() > 0) {
                        HashSet<String> specOfPtoSet = (HashSet<String>) specOfPto;
                        if (!specOfPtoSet.containsAll(ptoSpecArr)) {
                            log.debug("Pto {} doesn't have necessary specialization", pto);
                            commonReportSaveService.addNewReport(null, "5.1", "ПТО не имеет требуемой специализации.");
                            return dto;
                        }
                    }
                }

                //7.1. Гр. 15а «Код страны отправления». В гр. 15а ДТ указан код страны отправления, который входит
                // в перечень стран в таблице OTS_XOX.NSI_TS_OKSMT, отобранный по условию OTS_XOX.NSI_TAR_OKSMT ID_USL= 00000001214907.
                if (newDoc.getDeclarationGoodsShipmentDetails() != null) {
                    if (newDoc.getDeclarationGoodsShipmentDetails().getDepartureCountryDetails() != null) {
                        log.info("check 7.1");
                        String codeCountry = newDoc.getDeclarationGoodsShipmentDetails().getDepartureCountryDetails().getShortCountryName();
                        if (codeCountry == null || !helperRestProxy.checkTarOksmt(USL_ID_FOR_CUSTOMS_UNION_COUNTRIES, codeCountry, LocalDateTime.now())) {
                            log.debug("Short country's code  isn't included in customs union country list");
                            commonReportSaveService.addNewReport("15a", "7.1", "Страна отправления не является членом ЕАЭС или код страны отправления не указан.");
                            return dto;
                        }
                    }
                    if (newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails() != null &&
                            !newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails().isEmpty()) {
                        List<by.btslogistics.xsdclasses.eec.v1_4_0.eec.m.ca.complexdataobjects.v1_5_8.DeclarationGoodsItemDetailsType> goodsItemList = newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails();

                        //9.1. Гр. 33 «Код товара». В гр. 33 ДТ указан код товара, который не входит в перечень товаров в таблице OTS_XOX.NSI_NO_TARIF,
                        // отобранный по условию ID_OBJ = 00000004672228
                        log.info("check 9.1");
                        if (goodsItemList.stream().anyMatch(item -> helperRestProxy.checkTnVedAndIdObjInNoTarif(ID_OBJ_OF_GOODS_WITHOUT_AV, item.getCommodityCode(), LocalDateTime.now()))) {
                            log.debug("On of from CommodityCodes was included in special list of goods with limits");
                            commonReportSaveService.addNewReport("33", "9.1", "Присутствует товар, включенный перечень товаров, в отношении которых автоматический выпуск в таможенной процедуре свободной таможенной зоны ограничен.");
                            return dto;
                        }
                    }
                }
            } else {
                eec.r._036.goodsdeclaration.v1_3_1.GoodsDeclarationType oldDoc = dto.getGoodsDeclarationTypeOld();
                //Гр. 14 «Декларант». В правом верхнем углу графы 14 ДТ после знака «№» указан УНП декларанта, имеющийся в представлении UNP_KCEZ
                if (oldDoc.getDeclarantDetails() != null && oldDoc.getDeclarantDetails().getTaxpayerId() != null) {
                    log.info("check 3.1");
                    if (!helperRestProxy.getUnpKcez(oldDoc.getDeclarantDetails().getTaxpayerId())) {
                        log.debug("Helper has returned false from method getUnpKcez");
                        commonReportSaveService.addNewReport("14", "3.1", "Декларирование товаров осуществляется не резидентом свободной (особой, специальной) экономической зоны.");
                        return dto;
                    }
                }

                // Гр. 30 «Местонахождения товара». В гр. 30 ДТ указан номер зоны таможенного контроля, имеющийся в таблице NSI_ZTK
                if (oldDoc.getDeclarationGoodsShipmentDetails() != null && oldDoc.getDeclarationGoodsShipmentDetails().getGoodsLocationDetails() != null) {
                    String nomZtk = oldDoc.getDeclarationGoodsShipmentDetails().getGoodsLocationDetails().getCustomsControlZoneId();
                    if (nomZtk != null) {
                        log.info("check 4.1");
                        if (!helperRestProxy.checkNomZtk(nomZtk, LocalDateTime.now())) {
                            log.debug("Helper has returned false from method checkNomZtk");
                            commonReportSaveService.addNewReport("30", "4.1", "Товары размещаются в незарегистрированной таможенной зоне.");
                            return dto;
                        }
                    }
                }

                //5.1. Проверка на наличие требуемой специализации ПТО: указанный в регистрационном номере ДТ код ПТО (3 символа) попадает
                // в перечень ПТО в таблице NSI_PTO_BR, отобранный по условию NSI_PTO_SPEC_BR CODE = (072,093,136,146,164,169,170,171).
                String pto = dto.getMessageLog().getPto();
                if (pto != null) {
                    log.info("check 5.1");
                    Iterable<String> specOfPto = helperRestProxy.getListOfSpecForPto(pto);
                    if (specOfPto != null && specOfPto.spliterator().getExactSizeIfKnown() > 0) {
                        HashSet<String> specOfPtoSet = (HashSet<String>) specOfPto;
                        if (!specOfPtoSet.containsAll(ptoSpecArr)) {
                            log.debug("Pto {} doesn't have necessary specialization", pto);
                            commonReportSaveService.addNewReport(null, "5.1", "ПТО не имеет требуемой специализации.");
                            return dto;
                        }
                    }
                }

                //7.1. Гр. 15а «Код страны отправления». В гр. 15а ДТ указан код страны отправления, который входит
                // в перечень стран в таблице OTS_XOX.NSI_TS_OKSMT, отобранный по условию OTS_XOX.NSI_TAR_OKSMT ID_USL= 00000001214907.
                if (oldDoc.getDeclarationGoodsShipmentDetails() != null) {
                    if (oldDoc.getDeclarationGoodsShipmentDetails().getDepartureCountryDetails() != null) {
                        log.info("check 7.1");
                        String codeCountry = oldDoc.getDeclarationGoodsShipmentDetails().getDepartureCountryDetails().getShortCountryName();
                        if (codeCountry == null || !helperRestProxy.checkTarOksmt(USL_ID_FOR_CUSTOMS_UNION_COUNTRIES, codeCountry, LocalDateTime.now())) {
                            log.debug("Short country's code  isn't included in customs union country list");
                            commonReportSaveService.addNewReport("15a", "7.1", "Страна отправления не является членом ЕАЭС или код страны отправления не указан.");
                            return dto;
                        }
                    }
                    if (oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails() != null &&
                            !oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails().isEmpty()) {
                        List<DeclarationGoodsItemDetailsType> goodsItemList = oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails();

                        //9.1. Гр. 33 «Код товара». В гр. 33 ДТ указан код товара, который не входит в перечень товаров в таблице OTS_XOX.NSI_NO_TARIF,
                        // отобранный по условию ID_OBJ = 00000004672228
                        log.info("check 9.1");
                        if (goodsItemList.stream().anyMatch(item -> helperRestProxy.checkTnVedAndIdObjInNoTarif(ID_OBJ_OF_GOODS_WITHOUT_AV, item.getCommodityCode(), LocalDateTime.now()))) {
                            log.debug("On of from CommodityCodes was included in special list of goods with limits");
                            commonReportSaveService.addNewReport("33", "9.1", "Присутствует товар, включенный перечень товаров, в отношении которых автоматический выпуск в таможенной процедуре свободной таможенной зоны ограничен.");
                            return dto;
                        }
                    }
                }
            }
        } catch (RetryableException e) {
            e.printStackTrace();
            commonReportSaveService.addNewReport(null, null, "Helper не отвечает!");
            return dto;
        }
        dto.setElementaryChecksPassed(true);
        return nextStep(dto);
    }
}
