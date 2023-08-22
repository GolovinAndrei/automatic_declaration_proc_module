package by.btslogistics.autorelease.service.releasedocument.steps.elementarychecksbyproc;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.releasedocument.steps.RegistrationDocument;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;
import by.btslogistics.autorelease.web.rest.proxyfeign.HelperRestProxy;
import by.btslogistics.commons.dto.nsi.NsiCatalogDto;
import by.btslogistics.commons.dto.nsi.NsiPrUnpDto;
import by.btslogistics.commons.dto.nsi.NsiTarifDto;
import by.btslogistics.commons.dto.nsi.NsiTnvedSpDto;
import eec.m.ca.complexdataobjects.v1_5_7.DeclarationGoodsItemDetailsType;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Первичные проверки для декларации с кодом процедуры 40
 */

@Slf4j
public class ElementaryChecksForProc40 extends RegistrationDocument {

    private final HelperRestProxy helperRestProxy;

    private final CommonReportSaveService commonReportSaveService;

    public static final List<String> GROUP_87 = Arrays.asList("870600", "8707", "8708", "8714", "8716901000", "8716903000", "8716905000", "8716909000");

    public static final String TARIFF_REGULATION_ANTIDUMPING = "031";

    public static final String TARIFF_REGULATION_EXCISES = "037";

    public static final String PREFERENCE_SYMBOL_1 = "ОО";

    public static final String PREFERENCE_SYMBOL_2 = "ПД";

    public ElementaryChecksForProc40(HelperRestProxy helperRestProxy, CommonReportSaveService commonReportSaveService) {
        this.helperRestProxy = helperRestProxy;
        this.commonReportSaveService = commonReportSaveService;
    }

    @Override
    public RegistrationDto doStep(RegistrationDto dto) {

        try {
            if (dto.getGoodsDeclarationTypeNew() != null) {

                //2.7 УНП декларанта указанный в правом верхнем углу графы 14 «Декларант» после знака «N» имеет в таблице NSI_PR_UNP признак «D»
                List<NsiPrUnpDto> unpList = (List<NsiPrUnpDto>) helperRestProxy.getSignByUnp(dto.getGoodsDeclarationTypeNew().getDeclarantDetails().getTaxpayerId());
                if (unpList.stream().anyMatch(unpRec -> unpRec.getSignUnp().equals("D"))) {
                    log.debug("Для лица, осуществляющего декларирование, выпуск товаров в таможенной процедуре выпуска для внутреннего потребления ограничен.");
                    commonReportSaveService.addNewReport("14", "2.7", "Декларант имеет признак D");
                    return dto;
                }

                if (dto.getGoodsDeclarationTypeNew().getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails() != null &&
                        !dto.getGoodsDeclarationTypeNew().getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails().isEmpty()) {
                    List<by.btslogistics.xsdclasses.eec.v1_4_0.eec.m.ca.complexdataobjects.v1_5_8.DeclarationGoodsItemDetailsType> goodsItemList = dto.getGoodsDeclarationTypeNew().getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails();
                    //Гр.33 «Код товара». Товар не относится к товарной группе 87. За исключением: 870600, 8707, 8708, 8714, 8716901000,8716903000, 8716905000, 8716909000
                    List<by.btslogistics.xsdclasses.eec.v1_4_0.eec.m.ca.complexdataobjects.v1_5_8.DeclarationGoodsItemDetailsType> goodsItem87 = goodsItemList
                            .stream()
                            .filter(declItem -> declItem.getCommodityCode().startsWith("87"))
                            .collect(Collectors.toList());
                    if (!goodsItem87.isEmpty()) {
                        if (goodsItem87.stream()
                                .noneMatch(item -> GROUP_87.stream()
                                        .anyMatch(group87 -> item.getCommodityCode().startsWith(group87)))) {
                            log.debug("Код относится к товарной группе 87");
                            commonReportSaveService.addNewReport("33", "4.1", "Товар относится к товарной группе 87");
                            return dto;
                        }
                    }

                    for (by.btslogistics.xsdclasses.eec.v1_4_0.eec.m.ca.complexdataobjects.v1_5_8.DeclarationGoodsItemDetailsType goodItem : goodsItemList) {
                        String commodityCode = goodItem.getCommodityCode();
                        //Код товара гр.33 «Код товара» отсутствует в таблице nsi_type_sp, code=’037’
                        if (getRecordFromTarifWithTnVedAndIdObjList(commodityCode, getIdObjListByTypeSp(TARIFF_REGULATION_EXCISES)) != null) {
                            log.debug("Товар {} является подакцизным!", commodityCode);
                            commonReportSaveService.addNewReport("33", "5.1", "Товар является подакцизным");
                            return dto;
                        }

                        //Гр. 36 «Преференции» Элемент 1 должен содержать значение «ОО», элемент 2 – «ОО», элемент 3 – «-», элемент 4 – «ОО» или «ПД»
                        if (!goodItem.getPreferenceDetails().getCustomsClearanceChargesPrefCode().getValue().equals(PREFERENCE_SYMBOL_1) ||
                                !goodItem.getPreferenceDetails().getCustomsDutyPrefCode().getValue().equals(PREFERENCE_SYMBOL_1) ||
                                !goodItem.getPreferenceDetails().getExcisePrefCode().getValue().equals("-") ||
                                !goodItem.getPreferenceDetails().getVATPrefCode().getValue().equals(PREFERENCE_SYMBOL_1) &&
                                        !goodItem.getPreferenceDetails().getVATPrefCode().getValue().equals(PREFERENCE_SYMBOL_2)) {
                            log.debug("Товар {} должен без преференций!", commodityCode);
                            commonReportSaveService.addNewReport("36", "6.1", "К товару применимы преференции или льготы");
                            return dto;
                        }

                        //6.3.1 Если в элементе 4 гр.36 указано «ПД»: Код товара гр.33 «Код товара» включен в перечень продовольственных товаров (id_obj = 00000000077153 и SUM_RATE=100 в таблице ots_xox.NSI_TARIF)
                        if (goodItem.getPreferenceDetails().getVATPrefCode().getValue().equals(PREFERENCE_SYMBOL_2)) {
                            NsiTarifDto nsiTarifDto = getRecordFromTarifWithTnVedAndIdObjList(commodityCode, Collections.singletonList("00000000077153"));
                            if (nsiTarifDto == null || nsiTarifDto.getSumRate() != 100) {
                                log.debug("Товар {} не включен в перечень продовольственных товаров!", commodityCode);
                                commonReportSaveService.addNewReport("36", "6.3.1", "Товар c преференцией ПД не включен в перечень продовольственных товаров");
                                return dto;
                            }
                        }

                        // 7.1. Код товара гр.33 «Код товара» отсутствует в таблице nsi_type_sp, code=’031’
                        if (getRecordFromTarifWithTnVedAndIdObjList(commodityCode, getIdObjListByTypeSp(TARIFF_REGULATION_ANTIDUMPING)) != null) {
                            log.debug("В отношении товара {} применены антидемпинговые и компенсационные меры", commodityCode);
                            commonReportSaveService.addNewReport("33", "7.1", "В отношении товара применены специальные защитные, антидемпинговые и компенсационные меры");
                            return dto;
                        }
                    }
                }


            } else {

                if (dto.getGoodsDeclarationTypeOld() != null) {

                    //2.7 УНП декларанта указанный в правом верхнем углу графы 14 «Декларант» после знака «N» имеет в таблице NSI_PR_UNP признак «D»

                    List<NsiPrUnpDto> unpList = (List<NsiPrUnpDto>) helperRestProxy.getSignByUnp(dto.getGoodsDeclarationTypeNew().getDeclarantDetails().getTaxpayerId());
                    if (unpList.stream().anyMatch(unpRec -> unpRec.getSignUnp().equals("D"))) {
                        log.debug("Для лица, осуществляющего декларирование, выпуск товаров в таможенной процедуре выпуска для внутреннего потребления ограничен.");
                        commonReportSaveService.addNewReport("14", "2.7", "Декларант имеет признак D");
                        return dto;
                    }

                    if (dto.getGoodsDeclarationTypeOld().getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails() != null &&
                            !dto.getGoodsDeclarationTypeOld().getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails().isEmpty()) {
                        List<DeclarationGoodsItemDetailsType> goodsItemList = dto.getGoodsDeclarationTypeOld().getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails();

                        //Гр.33 «Код товара». Товар не относится к товарной группе 87. За исключением: 870600, 8707, 8708, 8714, 8716901000,8716903000, 8716905000, 8716909000
                        List<DeclarationGoodsItemDetailsType> goodsItem87 = goodsItemList
                                .stream()
                                .filter(declItem -> declItem.getCommodityCode().startsWith("87"))
                                .collect(Collectors.toList());
                        if (!goodsItem87.isEmpty()) {
                            if (goodsItem87.stream()
                                    .noneMatch(item -> GROUP_87.stream()
                                            .anyMatch(group87 -> item.getCommodityCode().startsWith(group87)))) {
                                log.debug("Код относится к товарной группе 87");
                                commonReportSaveService.addNewReport("33", "4.1", "Товар относится к товарной группе 87");
                                return dto;
                            }
                        }

                        for (DeclarationGoodsItemDetailsType goodItem : goodsItemList) {
                            String commodityCode = goodItem.getCommodityCode();
                            //Код товара гр.33 «Код товара» отсутствует в таблице nsi_type_sp, code=’037’
                            if (getRecordFromTarifWithTnVedAndIdObjList(commodityCode, getIdObjListByTypeSp(TARIFF_REGULATION_EXCISES)) != null) {
                                log.debug("Товар {} является подакцизным!", commodityCode);
                                commonReportSaveService.addNewReport("33", "5.1", "Товар является подакцизным");
                                return dto;
                            }

                            //Гр. 36 «Преференции» Элемент 1 должен содержать значение «ОО», элемент 2 – «ОО», элемент 3 – «-», элемент 4 – «ОО» или «НИ»
                            if (!goodItem.getPreferenceDetails().getCustomsClearanceChargesPrefCode().getValue().equals(PREFERENCE_SYMBOL_1) ||
                                    !goodItem.getPreferenceDetails().getCustomsDutyPrefCode().getValue().equals(PREFERENCE_SYMBOL_1) ||
                                    !goodItem.getPreferenceDetails().getExcisePrefCode().getValue().equals("-") ||
                                    !goodItem.getPreferenceDetails().getVATPrefCode().getValue().equals(PREFERENCE_SYMBOL_1) &&
                                            !goodItem.getPreferenceDetails().getVATPrefCode().getValue().equals(PREFERENCE_SYMBOL_2)) {
                                log.debug("Товар {} должен без преференций!", commodityCode);
                                commonReportSaveService.addNewReport("36", "6.1", "К товару применимы преференции или льготы");
                                return dto;
                            }

                            //6.3.1 Если в элементе 4 гр.36 указано «ПД»: Код товара гр.33 «Код товара» включен в перечень продовольственных товаров (id_obj = 00000000077153 и SUM_RATE=100 в таблице ots_xox.NSI_TARIF)
                            if (goodItem.getPreferenceDetails().getVATPrefCode().getValue().equals(PREFERENCE_SYMBOL_2)) {
                                NsiTarifDto nsiTarifDto = getRecordFromTarifWithTnVedAndIdObjList(commodityCode, Collections.singletonList("00000000077153"));
                                if (nsiTarifDto == null || nsiTarifDto.getSumRate() != 100) {
                                    log.debug("Товар {} не включен в перечень продовольственных товаров!", commodityCode);
                                    commonReportSaveService.addNewReport("36", "6.3.1", "Товар включен в перечень продовольственных товаров");
                                    return dto;
                                }
                            }

                            // 7.1. Код товара гр.33 «Код товара» отсутствует в таблице nsi_type_sp, code=’031’
                            if (getRecordFromTarifWithTnVedAndIdObjList(commodityCode, getIdObjListByTypeSp(TARIFF_REGULATION_ANTIDUMPING)) != null) {
                                log.debug("В отношении товара {} применены антидемпинговые и компенсационные меры",commodityCode);
                                commonReportSaveService.addNewReport("33", "7.1", "В отношении товара применены специальные защитные, антидемпинговые и компенсационные меры");
                                return dto;
                            }
                        }
                    }
                }
            }
        } catch (RetryableException e) {
            e.printStackTrace();
            commonReportSaveService.addNewReport("", "", "Helper не отвечает!");
            return dto;
        }

        log.debug("First checks block with criteria 2 have completed");
        dto.setElementaryChecksPassed(true);
        return nextStep(dto);
    }

    private NsiTarifDto getRecordFromTarifWithTnVedAndIdObjList(String tnVed, List<String> idObjList) throws RetryableException {
        NsiTnvedSpDto tnVedSpDto = helperRestProxy.getFullTnVed(tnVed, LocalDateTime.now()).orElse(null);
        if (tnVedSpDto != null) {
            return helperRestProxy.getActualByListIdsObjAndIdSpIn(idObjList, tnVedSpDto.getIdSp(), LocalDateTime.now()).orElse(null);
        }
        return null;
    }

    private List<String> getIdObjListByTypeSp(String typeSp) throws RetryableException {
        return helperRestProxy.getNsiCatalogsBySpec(typeSp)
                .stream()
                .map(NsiCatalogDto::getIdSpr)
                .collect(Collectors.toList());
    }
}