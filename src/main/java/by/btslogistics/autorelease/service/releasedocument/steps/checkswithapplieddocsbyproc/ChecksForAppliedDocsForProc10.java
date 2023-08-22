package by.btslogistics.autorelease.service.releasedocument.steps.checkswithapplieddocsbyproc;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.dto.ois.OisReportDto;
import by.btslogistics.autorelease.service.dto.prohibitrestrictreport.ProhibitRestrictReportDto;
import by.btslogistics.autorelease.service.dto.quota.QuotaReportDto;
import by.btslogistics.autorelease.service.read.ois.OisReportReadService;
import by.btslogistics.autorelease.service.read.prohibitrestrictreport.ProhibitRestrictReportReadService;
import by.btslogistics.autorelease.service.read.quota.QuotaReportReadService;
import by.btslogistics.autorelease.service.read.risks.CaseCustomControlReadService;
import by.btslogistics.autorelease.service.releasedocument.steps.RegistrationDocument;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;
import by.btslogistics.autorelease.web.rest.proxyfeign.*;
import by.btslogistics.commons.dto.nsi.NsiPrUnpDto;
import by.btslogistics.commons.dto.nsi.NsiReestrIsLicoDto;
import by.btslogistics.commons.dto.nsi.NsiUnpOsobPeremDto;
import eec.m.ca.complexdataobjects.v1_5_7.DeclarationGoodsItemDetailsType;
import feign.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChecksForAppliedDocsForProc10 extends RegistrationDocument {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChecksForAppliedDocsForProc10.class);

    private final CommonChecksForAppliedDocs commonChecksForAppliedDocs;

    private final QuotaReportReadService quotaReportReadService;

    private final ZioRestProxy zioRestProxy;

    private final ProhibitRestrictReportReadService prohibitRestrictReportReadService;

    private final HelperRestProxy helperRestProxy;

    private final OisReportReadService oisReportReadService;

    private final CommonReportSaveService commonReportSaveService;

    public static final List<String> registrySx = Arrays.asList(
            "00000000757773",
            "00000001149814",
            "00000000757129",
            //--------------
            "00000000757785",
            "00000000709947",
            "00000000353595",
            "00000000353594",
            "00000000368514",
            "00000002545440",
            "00000003458798",
            //--------------
            "00000000871568",
            //"00000002591305",
            "00000000729733"
    );

    private static final String MEDICAL_FACILITY_EXPORT_ATTRIBUTE = "М";

    private static final String HELPER = "Helper";

    private static final String ZiO = "ZiO";

    private static final String BLR_CODE = "BY";

    public final static List<String> listObjId = Arrays.asList(
            "00000003452894",
            "00000003452900",
            "00000003452902",
            "00000003452896",
            "00000003452898"
    );

    public ChecksForAppliedDocsForProc10(PaymentResultRestProxy paymentResultRestProxy, QuotaReportReadService quotaReportReadService,
                                         ZioRestProxy zioRestProxy, ProhibitRestrictReportReadService prohibitRestrictReportReadService, HelperRestProxy helperRestProxy, OisReportReadService oisReportReadService,
                                         FlkResultRestProxy flkResultRestProxy, CaseCustomControlReadService riskReadService, CommonReportSaveService commonReportSaveService) {
        this.quotaReportReadService = quotaReportReadService;
        this.zioRestProxy = zioRestProxy;
        this.prohibitRestrictReportReadService = prohibitRestrictReportReadService;
        this.helperRestProxy = helperRestProxy;
        this.oisReportReadService = oisReportReadService;
        this.commonReportSaveService = commonReportSaveService;
        this.commonChecksForAppliedDocs = new CommonChecksForAppliedDocs(flkResultRestProxy, paymentResultRestProxy, riskReadService, commonReportSaveService);
    }

    @Override
    public RegistrationDto doStep(RegistrationDto dto) {

        String checkCode = null;
        String externalService = null;
        //ФЛК
        if (!commonChecksForAppliedDocs.getFlkResult(dto)) return dto;

        //платежи
        if (!commonChecksForAppliedDocs.getPaymentResult(dto)) return dto;
        try {

            by.btslogistics.xsdclasses.eec.v1_4_0.eec.r._036.goodsdeclaration.v1_4_0.GoodsDeclarationType newDoc = dto.getGoodsDeclarationTypeNew();
            eec.r._036.goodsdeclaration.v1_3_1.GoodsDeclarationType oldDoc = dto.getGoodsDeclarationTypeOld();

            externalService = HELPER;
            String unp = getActualUnp(dto);
            List<NsiPrUnpDto> nsiPrUnpDtoList = (List<NsiPrUnpDto>) helperRestProxy.getSignByUnp(unp);
            //Проверка профилей риска при осутствии у УНП признака L
            LOGGER.debug("Risks step");
            if (nsiPrUnpDtoList.stream().noneMatch(nsiPrUnpDto -> nsiPrUnpDto.getSignUnp().equalsIgnoreCase("L"))) {
                if (!commonChecksForAppliedDocs.getRisksResult(dto)) {
                    return dto;
                }
            }

            //Квоты
            if (newDoc != null) {
                LOGGER.debug("Quota report is being analyzing");
                List<QuotaReportDto> quotaReports = quotaReportReadService.getReportByMessageLogId(dto.getMessageLog().getId());
                if (quotaReports != null && !quotaReports.isEmpty()) {
                    for (QuotaReportDto quotaReportDto : quotaReports) {
                        if (quotaReportDto.getResultCode() != 1 || newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails()
                                .stream()
                                .filter(declarationGoodsItemDetailsType -> declarationGoodsItemDetailsType.getConsignmentItemOrdinal().compareTo(BigInteger.valueOf(quotaReportDto.getGoodsNumeric())) == 0)
                                .collect(Collectors.toList()).get(0).getQuotaDetails() == null) {
                            LOGGER.debug("Quota report has errors. Autorelease have canceled!");
                            commonReportSaveService.addNewReport("44", "4.1", "Контроль квот не пройден или иинформация о квотах отсутствует");
                            return dto;
                        }
                    }
                }
            } else {
                LOGGER.debug("Quota report is being analyzing");
                List<QuotaReportDto> quotaReports = quotaReportReadService.getReportByMessageLogId(dto.getMessageLog().getId());
                if (quotaReports != null && !quotaReports.isEmpty()) {
                    for (QuotaReportDto quotaReportDto : quotaReports) {
                        if (quotaReportDto.getResultCode() != 1 || oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails()
                                .stream()
                                .filter(declarationGoodsItemDetailsType -> declarationGoodsItemDetailsType.getConsignmentItemOrdinal().compareTo(BigInteger.valueOf(quotaReportDto.getGoodsNumeric())) == 0)
                                .collect(Collectors.toList()).get(0).getQuotaDetails() == null) {
                            LOGGER.debug("Quota report has errors. Autorelease have canceled!");
                            commonReportSaveService.addNewReport("44", "4.1", "Контроль квот не пройден или иинформация о квотах отсутствует");
                            return dto;
                        }
                    }
                }
            }


            //ZIO
            LOGGER.debug("ZIO report is being analyzed Common part (10)");
            List<ProhibitRestrictReportDto> prohibitRestrictReportList = prohibitRestrictReportReadService.getProhibitRestrictReportByMessageLogId(dto.getMessageLog().getId());
            if (prohibitRestrictReportList != null && !prohibitRestrictReportList.isEmpty()) {

                for (ProhibitRestrictReportDto p : prohibitRestrictReportList) {

                    if (p.getCodeResult() == 2 || p.getCodeResult() == 3) {
                        if (p.getNsiCatalogId() != null) {
                            if (registrySx
                                    .stream()
                                    .noneMatch(reg -> reg.equals(p.getNsiCatalogId()))) {
                                LOGGER.debug("The value of NSI_CATALOG_ID is not in required list. Autorelease has canceled!(10)");
                                commonReportSaveService.addNewReport("", "4.2", "По проверяемой ДТ в протоколе ЗИО есть записи. Результат не включен ив перечень СХ");
                                return dto;
                            }
                        }
                    } else {
                        LOGGER.debug("");
                        commonReportSaveService.addNewReport("", "4.2", "По проверяемой ДТ в протоколе ЗИО есть записи. Дополнительные условия не соблюдены.");
                    }

                    if (newDoc != null) {

                        if (newDoc.getDeclarationGoodsShipmentDetails() != null) {
                            if (newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails() != null &&
                                    !newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails().isEmpty()) {
                                if (p.getNsiCatalogId() != null) {

                                    //12
                                    if (registrySx.subList(0, 3).stream().anyMatch(e -> e.equals(p.getNsiCatalogId()))) {
                                        LOGGER.debug("ZIO report is being analyzing for lists 00000000757773, 00000001149814, 00000000757129 (10.1)");
                                        if (!commonChecksForAppliedDocs.doesHaveItemProhibitionFreeCodeC(dto, p.getGoodsNumeric())) {
                                            LOGGER.debug("There is not sign \"C\". Autorelease have canceled!");
                                            commonReportSaveService.addNewReport("", "4.3.1", "Отсутствует признак С");
                                            return dto;
                                        }

                                        if (dto.getMessageLog().getSignAeo() == null || !dto.getMessageLog().getSignAeo().equalsIgnoreCase("AEO") && !dto.getMessageLog().getSignAeo().equalsIgnoreCase("AEOR")) {
                                            checkCode="4.3.2";
                                            if (p.getNsiCatalogId().equals("00000000757773")) {
                                                List<NsiUnpOsobPeremDto> osobPerem = (List<NsiUnpOsobPeremDto>) helperRestProxy.getUnpOsobPeremByIdObj("00000003416676");
                                                if (osobPerem.stream().noneMatch(e -> e.getCodeTn().equals(p.getCodeTnVed()) && e.getUnp().equals(unp))) {
                                                    LOGGER.debug("UNP and TNVED is not included in NSI_UNP_OSOB_PEREM . Autorelease have canceled! (10.1)");
                                                    commonReportSaveService.addNewReport("", "4.3.2", "Код товара не включен в список отечественных производителей или отсутствуют признаки AEO (AEOR)");
                                                    return dto;
                                                }
                                            }
                                        }
                                    }

                                    //13
                                    if (registrySx.get(10).equals(p.getNsiCatalogId()) || registrySx.get(11).equals(p.getNsiCatalogId()) || p.getNsiCatalogId().equals("00000002591305")) {
                                        if (newDoc.getDeclarationGoodsShipmentDetails().getOriginCountryDetails() != null) {
                                            if (newDoc.getDeclarationGoodsShipmentDetails().getOriginCountryDetails().getCACountryCode() != null) {
                                                if (!newDoc.getDeclarationGoodsShipmentDetails().getOriginCountryDetails().getCACountryCode().getValue().equalsIgnoreCase(BLR_CODE)) {
                                                    LOGGER.debug("There is not sign \"BY\". Autorelease have canceled! (13)");
                                                    commonReportSaveService.addNewReport("16", "4.5", "Cтрана происхождения товаров не РБ");
                                                    return dto;
                                                }
                                            }
                                        }
                                    }

                                    //14
                                    if (registrySx.subList(3, 10).stream().anyMatch(e -> e.equals(p.getNsiCatalogId()))) {
                                        LOGGER.debug("ZIO report is analyzed for lists 00000000757785, 00000000709947, 00000000353595, 00000000353594, 00000000368514, 00000002545440, 00000003458798");
                                        if (unp != null) {
                                            if (nsiPrUnpDtoList.stream().noneMatch(nsiPrUnpDto -> nsiPrUnpDto.getSignUnp().equalsIgnoreCase(MEDICAL_FACILITY_EXPORT_ATTRIBUTE))) {
                                                LOGGER.debug("There is not sign \"M\". Autorelease have canceled!(10.3)");
                                                commonReportSaveService.addNewReport("", "4.7.2", "Товар не помещен под таможенную процедуру экспорта медицинскими учреждениями");
                                                return dto;
                                            }
                                        }
                                        if (Stream.of("3002901000", "3002120005", "3001902000").noneMatch(e -> e.equals(p.getCodeTnVed()))) {
                                            LOGGER.debug("Code tn-ved does not match");
                                            commonReportSaveService.addNewReport("", "4.7.1", "Товары не классифицируются кодами ТН ВЭД ЕАЭС 3002 90 100 0, 3002 12 000 5, 3001 90 200 0");
                                            return dto;
                                        }

                                        if (!newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails()
                                                .stream()
                                                .filter(declarationGoodsItemDetailsType -> declarationGoodsItemDetailsType.getConsignmentItemOrdinal().intValue() == (p.getGoodsNumeric()))
                                                .allMatch(declarationGoodsItemDetailsType -> {
                                                    if (declarationGoodsItemDetailsType.getPresentedDocDetails() != null && !declarationGoodsItemDetailsType.getPresentedDocDetails().isEmpty()) {
                                                        return declarationGoodsItemDetailsType.getPresentedDocDetails()
                                                                .stream()
                                                                .filter(presentedDocDetailsType -> {
                                                                    if (presentedDocDetailsType.getDocKindCode() != null) {
                                                                        return presentedDocDetailsType.getDocKindCode().getValue().equals("01131");
                                                                    }
                                                                    return false;
                                                                })
                                                                .allMatch(presentedDocDetailsType -> {
                                                                    if (presentedDocDetailsType.getDocStartDate() != null && presentedDocDetailsType.getDocValidityDate() != null) {
                                                                        return presentedDocDetailsType.getDocStartDate().toGregorianCalendar().toZonedDateTime().toLocalDateTime().isBefore(dto.getMessageLog().getDateReg()) &&
                                                                                presentedDocDetailsType.getDocValidityDate().toGregorianCalendar().toZonedDateTime().toLocalDateTime().isAfter(dto.getMessageLog().getDateReg());
                                                                    }
                                                                    return false;
                                                                });
                                                    }
                                                    return false;
                                                })) {
                                            LOGGER.debug("Wrong date in count 44 for doc. 01131. Autorelease have canceled!(14)");
                                            commonReportSaveService.addNewReport("44", "4,7,3", "Нарушены сроки выдачи или действия для документа 01131");
                                            return dto;
                                        }
                                    }
                                }

                                //15 Ограничения автоматического выпуска
                                LOGGER.debug("Start checks for restrictions on TnVed (15)");
                                externalService=ZiO;
                                for (by.btslogistics.xsdclasses.eec.v1_4_0.eec.m.ca.complexdataobjects.v1_5_8.DeclarationGoodsItemDetailsType item : newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails()) {
                                    int positionNumber = item.getConsignmentItemOrdinal().intValue();
                                    LOGGER.debug("Check TnVed for item number: {} (11)", positionNumber);
                                    List<String> allObjIdByTnVed = zioRestProxy.getAllObjIdByTnVed(positionNumber, item.getCommodityCode(), dto.getMessageLog().getDateReg());
                                    LOGGER.info("in NsiNoTarif found {} records (11)", allObjIdByTnVed.isEmpty() ? 0 : allObjIdByTnVed.size());
                                    //если найти эти коды, заменить на reteanAll
                                    if (CollectionUtils.containsAny(allObjIdByTnVed, listObjId)) {
                                        if (nsiPrUnpDtoList.stream().noneMatch(nsiPrUnpDto -> nsiPrUnpDto.getSignUnp().equalsIgnoreCase("L"))) {
                                            LOGGER.debug("Check for item number {}, with TN VED {} failed (11)", positionNumber, item.getCommodityCode());
                                            commonReportSaveService.addNewReport("", "", "ТН ВЭД");
                                            return dto;
                                        }
                                    }

                                    //16 ОиС
                                    LOGGER.debug("Ois control start for DT ID {}, UNP: {}", dto.getMessageLog().getId(), unp);
                                    List<OisReportDto> oisReportsWithResult = oisReportReadService.getAllOisRecordsForGoods(dto.getMessageLog().getId(), item.getConsignmentItemOrdinal().intValue())
                                            .stream()
                                            .filter(ois -> ois.getResultCode() == 1)
                                            .collect(Collectors.toList());
                                    externalService=HELPER;
                                    checkCode="8.1";
                                    if (!oisReportsWithResult.isEmpty()) {
                                        for (OisReportDto oisReportDto : oisReportsWithResult) {

                                            List<NsiReestrIsLicoDto> nsiReestrIsLicoDtos = new ArrayList<>();
                                            for (int i = 1; i < 6; i = i + 2) {
                                                List<NsiReestrIsLicoDto> tempListFromNsi = helperRestProxy.getUnpByReshParams(oisReportDto.getIdResh(), oisReportDto.getYearResh(),
                                                        oisReportDto.getTypeResh(), i);
                                                if (tempListFromNsi != null && !tempListFromNsi.isEmpty()) {
                                                    nsiReestrIsLicoDtos.addAll(tempListFromNsi);
                                                }
                                            }
                                            if (!nsiReestrIsLicoDtos.isEmpty()) {
                                                if (nsiReestrIsLicoDtos.stream().noneMatch(nsiReestrIsLicoDto -> nsiReestrIsLicoDto.getUnn() != null && nsiReestrIsLicoDto.getUnn().equals(unp))) {
                                                    LOGGER.debug("Ois control not passed for goods number: {}", item.getConsignmentItemOrdinal());
                                                    commonReportSaveService.addNewReport("14", "8.1", "В отношении товаров применяются меры по защите прав на объекты интеллектуальной собственности");
                                                    return dto;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {

                        if (oldDoc.getDeclarationGoodsShipmentDetails() != null) {
                            if (oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails() != null &&
                                    !oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails().isEmpty()) {
                                if (p.getNsiCatalogId() != null) {

                                    //12
                                    if (registrySx.subList(0, 3).stream().anyMatch(e -> e.equals(p.getNsiCatalogId()))) {
                                        LOGGER.debug("ZIO report is being analyzing for lists 00000000757773, 00000001149814, 00000000757129 (10.1)");
                                        if (!commonChecksForAppliedDocs.doesHaveItemProhibitionFreeCodeC(dto, p.getGoodsNumeric())) {
                                            LOGGER.debug("There is not sign \"C\". Autorelease have canceled!");
                                            commonReportSaveService.addNewReport("", "4.3.1", "Отсутствует признак С");
                                            return dto;
                                        }

                                        if (dto.getMessageLog().getSignAeo() == null || !dto.getMessageLog().getSignAeo().equalsIgnoreCase("AEO") && !dto.getMessageLog().getSignAeo().equalsIgnoreCase("AEOR")) {
                                            checkCode="4.3.2";
                                            externalService=HELPER;
                                            if (p.getNsiCatalogId().equals("00000000757773")) {
                                                List<NsiUnpOsobPeremDto> osobPerem = (List<NsiUnpOsobPeremDto>) helperRestProxy.getUnpOsobPeremByIdObj("00000003416676");
                                                if (osobPerem.stream().noneMatch(e -> e.getCodeTn().equals(p.getCodeTnVed()) && e.getUnp().equals(unp))) {
                                                    LOGGER.debug("UNP and TNVED is not included in NSI_UNP_OSOB_PEREM . Autorelease have canceled! (10.1)");
                                                    commonReportSaveService.addNewReport("", "4.3.2", "Код товара не включен в список отечественных производителей или отсутствуют признаки AEO (AEOR");
                                                    return dto;
                                                }
                                            }
                                        }
                                    }

                                    //13
                                    if (registrySx.get(10).equals(p.getNsiCatalogId()) || registrySx.get(11).equals(p.getNsiCatalogId()) || p.getNsiCatalogId().equals("00000002591305")) {
                                        if (oldDoc.getDeclarationGoodsShipmentDetails().getOriginCountryDetails() != null) {
                                            if (oldDoc.getDeclarationGoodsShipmentDetails().getOriginCountryDetails().getCACountryCode() != null) {
                                                if (!oldDoc.getDeclarationGoodsShipmentDetails().getOriginCountryDetails().getCACountryCode().getValue().equalsIgnoreCase(BLR_CODE)) {
                                                    LOGGER.debug("There is not sign \"BY\". Autorelease have canceled! (13)");
                                                    commonReportSaveService.addNewReport("16", "4.5", "Cтрана происхождения товаров не РБ");
                                                    return dto;
                                                }
                                            }
                                        }
                                    }

                                    //14
                                    if (registrySx.subList(3, 10).stream().anyMatch(e -> e.equals(p.getNsiCatalogId()))) {
                                        LOGGER.debug("ZIO report is analyzed for lists 00000000757785, 00000000709947, 00000000353595, 00000000353594, 00000000368514, 00000002545440, 00000003458798");
                                        if (unp != null) {
                                            if (nsiPrUnpDtoList.stream().noneMatch(nsiPrUnpDto -> nsiPrUnpDto.getSignUnp().equalsIgnoreCase(MEDICAL_FACILITY_EXPORT_ATTRIBUTE))) {
                                                LOGGER.debug("There is not sign \"M\". Autorelease have canceled!(10.3)");
                                                commonReportSaveService.addNewReport("", "4.7.2", "Товар не помещен под таможенную процедуру экспорта медицинскими учреждениями");
                                                return dto;
                                            }
                                        }
                                        if (Stream.of("3002901000", "3002120005", "3001902000").noneMatch(e -> e.equals(p.getCodeTnVed()))) {
                                            LOGGER.debug("Code tn-ved does not match");
                                            commonReportSaveService.addNewReport("", "4.7.1", "Товары не классифицируются кодами ТН ВЭД ЕАЭС 3002 90 100 0, 3002 12 000 5, 3001 90 200 0");
                                            return dto;
                                        }

                                        if (!oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails()
                                                .stream()
                                                .filter(declarationGoodsItemDetailsType -> declarationGoodsItemDetailsType.getConsignmentItemOrdinal().intValue() == (p.getGoodsNumeric()))
                                                .allMatch(declarationGoodsItemDetailsType -> {
                                                    if (declarationGoodsItemDetailsType.getPresentedDocDetails() != null && !declarationGoodsItemDetailsType.getPresentedDocDetails().isEmpty()) {
                                                        return declarationGoodsItemDetailsType.getPresentedDocDetails()
                                                                .stream()
                                                                .filter(presentedDocDetailsType -> {
                                                                    if (presentedDocDetailsType.getDocKindCode() != null) {
                                                                        return presentedDocDetailsType.getDocKindCode().getValue().equals("01131");
                                                                    }
                                                                    return false;
                                                                })
                                                                .allMatch(presentedDocDetailsType -> {
                                                                    if (presentedDocDetailsType.getDocStartDate() != null && presentedDocDetailsType.getDocValidityDate() != null) {
                                                                        return presentedDocDetailsType.getDocStartDate().toGregorianCalendar().toZonedDateTime().toLocalDateTime().isBefore(dto.getMessageLog().getDateReg()) &&
                                                                                presentedDocDetailsType.getDocValidityDate().toGregorianCalendar().toZonedDateTime().toLocalDateTime().isAfter(dto.getMessageLog().getDateReg());
                                                                    }
                                                                    return false;
                                                                });
                                                    }
                                                    return false;
                                                })) {
                                            LOGGER.debug("Wrong date in count 44 for doc. 01131. Autorelease have canceled!(14)");
                                            commonReportSaveService.addNewReport("44", "4,7,3", "Нарушены сроки выдачи или действия для документа 01131");
                                            return dto;
                                        }
                                    }
                                }

                                //15 Ограничения автоматического выпуска
                                LOGGER.debug("Start checks for restrictions on TnVed (15)");
                                externalService=ZiO;
                                for (DeclarationGoodsItemDetailsType item : oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails()) {
                                    int positionNumber = item.getConsignmentItemOrdinal().intValue();
                                    LOGGER.debug("Check TnVed for item number: {} (11)", positionNumber);
                                    List<String> allObjIdByTnVed = zioRestProxy.getAllObjIdByTnVed(positionNumber, item.getCommodityCode(), dto.getMessageLog().getDateReg());
                                    LOGGER.info("in NsiNoTarif found {} records (11)", allObjIdByTnVed.isEmpty() ? 0 : allObjIdByTnVed.size());
                                    //если найти эти коды, заменить на reteanAll
                                    if (CollectionUtils.containsAny(allObjIdByTnVed, listObjId)) {
                                        if (nsiPrUnpDtoList.stream().noneMatch(nsiPrUnpDto -> nsiPrUnpDto.getSignUnp().equalsIgnoreCase("L"))) {
                                            LOGGER.debug("Check for item number {}, with TN VED {} failed (11)", positionNumber, item.getCommodityCode());
                                            commonReportSaveService.addNewReport("", "", "ТН ВЭД");
                                            return dto;
                                        }

                                    }

                                    //16 ОиС
                                    LOGGER.debug("Ois control start for DT ID {}, UNP: {}", dto.getMessageLog().getId(), unp);
                                    List<OisReportDto> oisReportsWithResult = oisReportReadService.getAllOisRecordsForGoods(dto.getMessageLog().getId(), item.getConsignmentItemOrdinal().intValue())
                                            .stream()
                                            .filter(ois -> ois.getResultCode() == 1)
                                            .collect(Collectors.toList());

                                    if (!oisReportsWithResult.isEmpty()) {
                                        externalService=HELPER;
                                        checkCode="8.1";
                                        for (OisReportDto oisReportDto : oisReportsWithResult) {

                                            List<NsiReestrIsLicoDto> nsiReestrIsLicoDtos = new ArrayList<>();
                                            for (int i = 1; i < 6; i = i + 2) {
                                                List<NsiReestrIsLicoDto> tempListFromNsi = helperRestProxy.getUnpByReshParams(oisReportDto.getIdResh(), oisReportDto.getYearResh(),
                                                        oisReportDto.getTypeResh(), i);
                                                if (tempListFromNsi != null && !tempListFromNsi.isEmpty()) {
                                                    nsiReestrIsLicoDtos.addAll(tempListFromNsi);
                                                }
                                            }
                                            if (!nsiReestrIsLicoDtos.isEmpty()) {
                                                if (nsiReestrIsLicoDtos.stream().noneMatch(nsiReestrIsLicoDto -> nsiReestrIsLicoDto.getUnn() != null && nsiReestrIsLicoDto.getUnn().equals(unp))) {
                                                    LOGGER.debug("Ois control not passed for goods number: {}", item.getConsignmentItemOrdinal());
                                                    commonReportSaveService.addNewReport("14", "8.1", "В отношении товаров применяются меры по защите прав на объекты интеллектуальной собственности");
                                                    return dto;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (RetryableException e) {
            e.printStackTrace();
            commonReportSaveService.addNewReport("", checkCode, externalService + " не отвечает!");
            return dto;
        }
        LOGGER.debug("Second check block passed");
        dto.setChecksWithAppliedDocsPassed(true);
        return nextStep(dto);
    }

    public static String getActualUnp(RegistrationDto dto) {
        String unp = null;

        if (dto.getGoodsDeclarationTypeNew() != null) {

            by.btslogistics.xsdclasses.eec.v1_4_0.eec.r._036.goodsdeclaration.v1_4_0.GoodsDeclarationType newDoc = dto.getGoodsDeclarationTypeNew();

            if (newDoc.getDeclarantDetails() != null) {
                if (newDoc.getDeclarantDetails().getSubjectBranchDetails() != null) {
                    unp = newDoc.getDeclarantDetails().getSubjectBranchDetails().getTaxpayerId();
                }
                if (unp == null) {
                    unp = newDoc.getDeclarantDetails().getTaxpayerId();
                }
            }
            LOGGER.debug("Current UNP is {}", unp);
            return unp;
        } else {
            eec.r._036.goodsdeclaration.v1_3_1.GoodsDeclarationType oldDoc = dto.getGoodsDeclarationTypeOld();

            if (oldDoc.getDeclarantDetails() != null) {
                if (oldDoc.getDeclarantDetails().getSubjectBranchDetails() != null) {
                    unp = oldDoc.getDeclarantDetails().getSubjectBranchDetails().getTaxpayerId();
                }
                if (unp == null) {
                    unp = oldDoc.getDeclarantDetails().getTaxpayerId();
                }
            }
            LOGGER.debug("Current UNP is {}", unp);
            return unp;
        }
    }
}