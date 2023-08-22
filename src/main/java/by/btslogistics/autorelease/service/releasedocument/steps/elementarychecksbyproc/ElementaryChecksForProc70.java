package by.btslogistics.autorelease.service.releasedocument.steps.elementarychecksbyproc;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.releasedocument.steps.RegistrationDocument;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;
import by.btslogistics.autorelease.web.rest.proxyfeign.HelperRestProxy;
import by.btslogistics.commons.dto.nsi.NsiReestrTsDto;
import eec.m.ca.complexdataobjects.v1_5_7.PresentedDocDetailsType;
import eec.m.ca.complexdataobjects.v1_5_7.DeclarationGoodsItemDetailsType;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Первичные проверки для декларации с кодом процедуры 70
 */
@Slf4j
public class ElementaryChecksForProc70 extends RegistrationDocument {

    private final HelperRestProxy helperRestProxy;

    private final CommonReportSaveService commonReportSaveService;

    public static final String PRESENTED_DOC_CODE_1 = "10042";

    public static final String PRESENTED_DOC_CODE_2 = "10021";

    public ElementaryChecksForProc70(HelperRestProxy helperRestProxy, CommonReportSaveService commonReportSaveService) {
        this.helperRestProxy = helperRestProxy;
        this.commonReportSaveService = commonReportSaveService;
    }

    @Override
    public RegistrationDto doStep(RegistrationDto dto) {

        if (dto.getGoodsDeclarationTypeNew() != null) {

            //Гр. 14 «Декларант». В правом верхнем углу графы после знака «N» указан УНП декларанта. Графа должна быть заполнена
            if (dto.getGoodsDeclarationTypeNew().getDeclarantDetails() != null && dto.getGoodsDeclarationTypeNew().getDeclarantDetails().getTaxpayerId() != null) {

                //Гр. 14 «Декларант». В графе 14 декларации на товары указана страна «Беларусь» (<UnifiedCountryCode> =«BY»).
                if (dto.getGoodsDeclarationTypeNew().getDeclarantDetails().getSubjectAddressDetails() != null && !dto.getGoodsDeclarationTypeNew().getDeclarantDetails().getSubjectAddressDetails().isEmpty()) {
                    if (dto.getGoodsDeclarationTypeNew().getDeclarantDetails().getSubjectAddressDetails()
                            .stream()
                            .allMatch(address -> address.getUnifiedCountryCode().getValue().equals("BY"))
                    ) {

                        //Первые два символа кода товара в гр. 33 принадлежат промежутку [39;97]
                        if (dto.getGoodsDeclarationTypeNew().getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails().stream().allMatch(item -> {
                            String comparableSubStr = item.getCommodityCode().substring(0, 2);
                            int comparableInt = Integer.parseInt(comparableSubStr);
                            return comparableInt <= 97 && comparableInt >= 39;
                        })) {

                            LocalDateTime regDateOfDt = dto.getMessageLog().getDateReg();

                            for (by.btslogistics.xsdclasses.eec.v1_4_0.eec.m.ca.complexdataobjects.v1_5_8.DeclarationGoodsItemDetailsType detailsType : dto.getGoodsDeclarationTypeNew().getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails()) {

                                if (detailsType.getPresentedDocDetails() != null) {
                                    if (detailsType.getPresentedDocDetails().stream().anyMatch(prDoc -> prDoc.getDocKindCode().getValue().equals(PRESENTED_DOC_CODE_1))) {

                                        if (detailsType.getPresentedDocDetails().stream().anyMatch(prDoc -> prDoc.getDocKindCode().getValue().equals(PRESENTED_DOC_CODE_2))) {

                                            for (by.btslogistics.xsdclasses.eec.v1_4_0.eec.m.ca.complexdataobjects.v1_5_8.PresentedDocDetailsType presentedDoc : detailsType.getPresentedDocDetails()) {
                                                if (presentedDoc.getDocKindCode() != null) {

                                                    //Дата, указанная в графе 44 ДТ под кодом «10042» (по структуре ХХ.ХХ.ХХХХ – день, месяц, год), не превышает 3-х лет с даты регистрации ДТ.
                                                    if (presentedDoc.getDocKindCode().getValue().equals(PRESENTED_DOC_CODE_1)) {
                                                        if (presentedDoc.getEventDate() != null) {
                                                            LocalDateTime dateOfPresentDoc = presentedDoc.getEventDate().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
                                                            if (dateOfPresentDoc.isAfter(regDateOfDt.plusYears(3))) {
                                                                log.debug("Дата, указанная в графе 44 ДТ под кодом «10042» превышает 3 года с даты регистрации ДТ");
                                                                commonReportSaveService.addNewReport("44", "6.2", "Дата, указанная в графе 44 ДТ под кодом «10042» превышает 3 года с даты регистрации ДТ");
                                                                return dto;
                                                            }
                                                        }
                                                    }

                                                    //Номер включения таможенного склада в реестр таможенных складов, указанный в графе 44 ДТ под кодом «10021» совместно с кодом дополнительной информации о документах «030» или «031» по структуре ДД-ТТПП/ХХХХХХХ
                                                    // (где ДД – буквы «ТС», «СА» или «СБ», ТТ – цифры (код таможни), ПП – цифры (порядковый номер таможенного склада), ХХХХХХХ – цифры (номер в реестре), соответствует номеру включения в реестр таможенных складов, согласно реестру таможенных складов.
                                                    try {
                                                        if (presentedDoc.getDocKindCode().getValue().equals(PRESENTED_DOC_CODE_2)) {
                                                            if (presentedDoc.getDocAddInfoCode() != null) {
                                                                if (presentedDoc.getDocAddInfoCode().startsWith("030") || presentedDoc.getDocAddInfoCode().startsWith("031")) {
                                                                    NsiReestrTsDto tsRecord = helperRestProxy.getReestrTsRecordByNum(presentedDoc.getDocId());
                                                                    if (tsRecord == null) {
                                                                        commonReportSaveService.addNewReport("44", "7.2", "Номер таможенного склада в гр.44 не включен в реестр складов");
                                                                        log.debug("Номер склада в графе 44 под кодом 10021 не включен в реестр таможенных складов!");
                                                                        return dto;
                                                                    }
                                                                } else {
                                                                    commonReportSaveService.addNewReport("44", "7.2", "Код доп.информации не 030... и не 031...");
                                                                    log.debug("Неверный код доп информации! Код: {}", presentedDoc.getDocAddInfoCode());
                                                                    return dto;
                                                                }
                                                            }
                                                        }
                                                    } catch (RetryableException e) {
                                                        e.printStackTrace();
                                                        commonReportSaveService.addNewReport("44", "7.2", "Helper не отвечает!");
                                                        return dto;
                                                    }
                                                }
                                            }
                                        } else {
                                            commonReportSaveService.addNewReport("44", "7.1", "Отсутвует документ с кодом 10021");
                                            log.debug("Отсутвует документ с кодом 10021!");
                                            return dto;
                                        }
                                    } else {
                                        commonReportSaveService.addNewReport("44", "6.1", "Отсутвует документ с кодом 10042");
                                        log.debug("Отсутвует документ с кодом 10042!");
                                        return dto;
                                    }
                                }
                            }

                            log.debug("First checks block with criteria 5 have completed");
                            dto.setElementaryChecksPassed(true);
                            return nextStep(dto);
                        } else {
                            commonReportSaveService.addNewReport(null, "5.1", "Код Тн ВЭД не принадлежит промежутку 39-97");
                            log.debug("ТН ВЭДы товаров не принадлежат промежутку кодов (первые два символа) 39-97!");
                        }
                    } else {
                        commonReportSaveService.addNewReport("14", "4.1", "В графе 14 указана не Беларусь");
                        log.debug("В графе 14 указана не Беларусь!");
                    }
                }
            } else {
                commonReportSaveService.addNewReport("14", "4.2", "УНП в графе 14 не заполнен");
                log.debug("УНП в графе 14 не заполнен!");
            }
        } else {

            //Гр. 14 «Декларант». В правом верхнем углу графы после знака «N» указан УНП декларанта. Графа должна быть заполнена
            if (dto.getGoodsDeclarationTypeOld().getDeclarantDetails() != null && dto.getGoodsDeclarationTypeOld().getDeclarantDetails().getTaxpayerId() != null) {

                //Гр. 14 «Декларант». В графе 14 декларации на товары указана страна «Беларусь» (<UnifiedCountryCode> =«BY»).
                if (dto.getGoodsDeclarationTypeOld().getDeclarantDetails().getSubjectAddressDetails() != null && !dto.getGoodsDeclarationTypeOld().getDeclarantDetails().getSubjectAddressDetails().isEmpty()) {
                    if (dto.getGoodsDeclarationTypeOld().getDeclarantDetails().getSubjectAddressDetails()
                            .stream()
                            .allMatch(address -> address.getUnifiedCountryCode().getValue().equals("BY"))
                    ) {

                        //Первые два символа кода товара в гр. 33 принадлежат промежутку [39;97]
                        if (dto.getGoodsDeclarationTypeOld().getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails().stream().allMatch(item -> {
                            String comparableSubStr = item.getCommodityCode().substring(0, 2);
                            int comparableInt = Integer.parseInt(comparableSubStr);
                            return comparableInt <= 97 && comparableInt >= 39;
                        })) {

                            LocalDateTime regDateOfDt = dto.getMessageLog().getDateReg();

                            for (DeclarationGoodsItemDetailsType detailsType : dto.getGoodsDeclarationTypeOld().getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails()) {

                                if (detailsType.getPresentedDocDetails() != null) {
                                    if (detailsType.getPresentedDocDetails().stream().anyMatch(prDoc -> prDoc.getDocKindCode().getValue().equals(PRESENTED_DOC_CODE_1))) {

                                        if (detailsType.getPresentedDocDetails().stream().anyMatch(prDoc -> prDoc.getDocKindCode().getValue().equals(PRESENTED_DOC_CODE_2))) {

                                            for (PresentedDocDetailsType presentedDoc : detailsType.getPresentedDocDetails()) {
                                                if (presentedDoc.getDocKindCode() != null) {

                                                    //Дата, указанная в графе 44 ДТ под кодом «10042» (по структуре ХХ.ХХ.ХХХХ – день, месяц, год), не превышает 3-х лет с даты регистрации ДТ.
                                                    if (presentedDoc.getDocKindCode().getValue().equals(PRESENTED_DOC_CODE_1)) {
                                                        if (presentedDoc.getEventDate() != null) {
                                                            LocalDateTime dateOfPresentDoc = presentedDoc.getEventDate().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
                                                            if (dateOfPresentDoc.isAfter(regDateOfDt.plusYears(3))) {
                                                                log.debug("Дата, указанная в графе 44 ДТ под кодом «10042» превышает 3 года с даты регистрации ДТ");
                                                                commonReportSaveService.addNewReport("44", "6.2", "Дата, указанная в графе 44 ДТ под кодом «10042» превышает 3 года с даты регистрации ДТ");
                                                                return dto;
                                                            }
                                                        }
                                                    }

                                                    //Номер включения таможенного склада в реестр таможенных складов, указанный в графе 44 ДТ под кодом «10021» совместно с кодом дополнительной информации о документах «030» или «031» по структуре ДД-ТТПП/ХХХХХХХ
                                                    // (где ДД – буквы «ТС», «СА» или «СБ», ТТ – цифры (код таможни), ПП – цифры (порядковый номер таможенного склада), ХХХХХХХ – цифры (номер в реестре), соответствует номеру включения в реестр таможенных складов, согласно реестру таможенных складов.

                                                    try {
                                                        if (presentedDoc.getDocKindCode().getValue().equals(PRESENTED_DOC_CODE_2)) {
                                                            if (presentedDoc.getDocAddInfoCode() != null) {
                                                                if (presentedDoc.getDocAddInfoCode().startsWith("030") || presentedDoc.getDocAddInfoCode().startsWith("031")) {
                                                                    NsiReestrTsDto tsRecord = helperRestProxy.getReestrTsRecordByNum(presentedDoc.getDocId());
                                                                    if (tsRecord == null) {
                                                                        commonReportSaveService.addNewReport("44", "7.2", "Номер таможенного склада в гр.44 не включен в реестр складов");
                                                                        log.debug("Номер склада в графе 44 под кодом 10021 не включен в реестр таможенных складов!");
                                                                        return dto;
                                                                    }
                                                                } else {
                                                                    commonReportSaveService.addNewReport("44", "7.2", "Код доп.информации не 030... и не 031...");
                                                                    log.debug("Неверный код доп информации! Код: {}", presentedDoc.getDocAddInfoCode());
                                                                    return dto;
                                                                }
                                                            }
                                                        }
                                                    } catch (RetryableException e) {
                                                        e.printStackTrace();
                                                        commonReportSaveService.addNewReport("44", "7.2", "Helper не отвечает!");
                                                        return dto;
                                                    }
                                                }
                                            }
                                        } else {
                                            commonReportSaveService.addNewReport("44", "7.1", "Отсутвует документ с кодом 10021");
                                            log.debug("Отсутвует документ с кодом 10021!");
                                            return dto;
                                        }
                                    } else {
                                        commonReportSaveService.addNewReport("44", "6.1", "Отсутвует документ с кодом 10042");
                                        log.debug("Отсутвует документ с кодом 10042!");
                                        return dto;
                                    }
                                }
                            }

                            log.debug("First checks block with criteria 5 have completed");
                            dto.setElementaryChecksPassed(true);
                            return nextStep(dto);
                        } else {
                            commonReportSaveService.addNewReport(null, "5.1", "Код Тн ВЭД не принадлежит промежутку 39-97");
                            log.debug("ТН ВЭДы товаров не принадлежат промежутку кодов (первые два символа) 39-97!");
                        }
                    } else {
                        commonReportSaveService.addNewReport("14", "4.1", "В графе 14 указана не Беларусь");
                        log.debug("В графе 14 указана не Беларусь!");
                    }
                }
            } else {
                commonReportSaveService.addNewReport("14", "4.2", "УНП в графе 14 не заполнен");
                log.debug("УНП в графе 14 не заполнен!");
            }
        }
        return dto;
    }
}
