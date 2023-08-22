package by.btslogistics.autorelease.service.releasedocument.steps;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.releasedocument.steps.elementarychecksbyproc.*;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;
import by.btslogistics.autorelease.web.rest.proxyfeign.HelperRestProxy;
import lombok.extern.slf4j.Slf4j;

/**
 * Цепочка первичных проверок, не требующих результатов работы сторонних модулей, их протоколов
 */
@Slf4j
public class ElementaryChecks extends RegistrationDocument {

    private final HelperRestProxy helperRestProxy;

    private final CommonReportSaveService commonReportSaveService;

    public static final String CODE_CUSTOM_PROCEDURE_1 = "10";

    public static final String CODE_CUSTOM_PROCEDURE_2 = "40";

    public static final String CODE_CUSTOM_PROCEDURE_3 = "96";

    public static final String CODE_CUSTOM_PROCEDURE_4 = "23";

    public static final String CODE_CUSTOM_PROCEDURE_5 = "70";

    public static final String CODE_CUSTOM_PROCEDURE_6 = "78";

    public static final String EXPORT_DECL = "ЭК";

    public static final String IMPORT_DECL = "ИМ";

    public static final String PREVIOUS_CUSTOM_PROC = "00";

    public static final String VDT = "ВДТ";


    public ElementaryChecks(HelperRestProxy helperRestProxy, CommonReportSaveService commonReportSaveService) {
        this.helperRestProxy = helperRestProxy;
        this.commonReportSaveService = commonReportSaveService;
    }

    /**
     * Выбор ветки проверок в зависимости от кода процедуры
     */
    @Override
    public RegistrationDto doStep(RegistrationDto dto) {
        log.debug("Start checks");

        //Проверка подана ли декларация электронным документом
        if (dto.getMessageLog().getSignDoc().equals("ЭД")) {
            String codeProc = dto.getMessageLog().getCodeProc();
            log.debug("Code proc. - {}", codeProc);
            switch (codeProc) {

                case  CODE_CUSTOM_PROCEDURE_1 :
                    if (procAndTypeDeclControl(dto, EXPORT_DECL,CODE_CUSTOM_PROCEDURE_1, null, PREVIOUS_CUSTOM_PROC)) {
                        new ElementaryChecksForProc10(commonReportSaveService).doStep(dto);
                    }
                    break;
                case CODE_CUSTOM_PROCEDURE_2 :
                    if (procAndTypeDeclControl(dto, IMPORT_DECL, CODE_CUSTOM_PROCEDURE_2, VDT, PREVIOUS_CUSTOM_PROC)) {
                        new ElementaryChecksForProc40(helperRestProxy, commonReportSaveService).doStep(dto);
                    }
                    break;
                case CODE_CUSTOM_PROCEDURE_3 :
                    if (procAndTypeDeclControl(dto, EXPORT_DECL, CODE_CUSTOM_PROCEDURE_3, null, PREVIOUS_CUSTOM_PROC)) {
                        new ElementaryChecksForProc96(helperRestProxy, commonReportSaveService).doStep(dto);
                    }
                    break;
                case CODE_CUSTOM_PROCEDURE_4 :
                    if (procAndTypeDeclControl(dto, EXPORT_DECL, CODE_CUSTOM_PROCEDURE_4, null, PREVIOUS_CUSTOM_PROC)) {
                        new ElementaryChecksForProc23(helperRestProxy, commonReportSaveService).doStep(dto);
                    }
                    break;
                case CODE_CUSTOM_PROCEDURE_5 :
                    if (procAndTypeDeclControl(dto, IMPORT_DECL, CODE_CUSTOM_PROCEDURE_5, null, PREVIOUS_CUSTOM_PROC)) {
                        new ElementaryChecksForProc70(helperRestProxy, commonReportSaveService).doStep(dto);
                    }
                    break;
                case CODE_CUSTOM_PROCEDURE_6 :
                    if (procAndTypeDeclControl(dto, EXPORT_DECL, CODE_CUSTOM_PROCEDURE_6, null, PREVIOUS_CUSTOM_PROC)) {
                        new ElementaryChecksForProc78(helperRestProxy, commonReportSaveService).doStep(dto);
                    }
                    break;
            }
        } else {
            log.debug("Документ не является электронным!");
            commonReportSaveService.addNewReport("1", "1.1", "Документ не является электронным");
            return dto;
        }
        if (dto.isElementaryChecksPassed()) {
            return nextStep(dto);
        }
        return dto;
    }

    /**
     * Отдельный метод проверки типа декларации (ИМ/ЭК), кода таможенной процедуры, кода предшествующей процедуры.
     *
     * @param dto         - транзитный объект цепочки автовыпуска
     * @param typeDecl    - тип декларации
     * @param procNum     - код таможенной процедуры
     * @param refNumber
     * @param prevElement - код предшествующей таможенной процедуры
     * @return если все соответствует - true и цепочка проверок идет дальше
     */
    private boolean procAndTypeDeclControl(RegistrationDto dto, String typeDecl, String procNum, String refNumber, String prevElement) {

        if (dto.getGoodsDeclarationTypeNew() == null) {
            return procAndTypeDeclControlForOld(dto, typeDecl, procNum, refNumber, prevElement);
        }

        if (typeDecl.equals(dto.getMessageLog().getTypeDecl())) {

            if (dto.getGoodsDeclarationTypeNew().getDeclarationGoodsShipmentDetails()
                    .getDeclarationGoodsItemDetails() != null && !dto.getGoodsDeclarationTypeNew().getDeclarationGoodsShipmentDetails()
                    .getDeclarationGoodsItemDetails().isEmpty()) {

                if (dto.getGoodsDeclarationTypeNew().getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails()
                        .stream()
                        .allMatch(item -> {
                                    if (item.getCustomsProcedureDetails()!=null && item.getCustomsProcedureDetails().getCustomsProcedureCode() != null && item.getCustomsProcedureDetails().getCustomsProcedureCode().getValue() != null &&
                                            item.getCustomsProcedureDetails().getPreviousCustomsProcedureModeCode() != null && item.getCustomsProcedureDetails().getPreviousCustomsProcedureModeCode().getValue() != null) {
                                        return item.getCustomsProcedureDetails().getCustomsProcedureCode().getValue().equals(procNum) &&
                                                item.getCustomsProcedureDetails().getPreviousCustomsProcedureModeCode().getValue().equals(prevElement);
                                    }
                                    return false;
                                }
                        )) {

                    if (refNumber == null && dto.getMessageLog().getCodeFeature() == null || dto.getMessageLog().getCodeFeature() != null && dto.getMessageLog().getCodeFeature().equals(VDT)) {
                        return true;
                    } else {
                        log.debug("Гр.7 заполнена или имеет неверное значение!");
                        commonReportSaveService.addNewReport("7", "2.4", "Присутствуют особенности декларирования или неверный код");
                    }
                } else {
                    log.debug("В гр.37 Указан не верные коды! Автовыпуск прерван.");
                    commonReportSaveService.addNewReport("37", "2.3", "В гр.37 не верно указана процедура");
                }
            }
        } else {
            log.debug("Не соответствует тип декларации");
            commonReportSaveService.addNewReport("1", "2.1", "Не соответствует тип декларации");
        }

        return false;
    }

    private boolean procAndTypeDeclControlForOld(RegistrationDto dto, String typeDecl, String procNum, String refNumber, String prevElement) {

        if (dto.getGoodsDeclarationTypeOld() != null) {

            if (typeDecl.equals(dto.getMessageLog().getTypeDecl())) {

                if (dto.getGoodsDeclarationTypeOld().getDeclarationGoodsShipmentDetails()
                        .getDeclarationGoodsItemDetails() != null && !dto.getGoodsDeclarationTypeOld().getDeclarationGoodsShipmentDetails()
                        .getDeclarationGoodsItemDetails().isEmpty()) {

                    if (dto.getGoodsDeclarationTypeOld().getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails()
                            .stream()
                            .allMatch(item -> {
                                        if (item.getCustomsProcedureDetails().getCustomsProcedureCode() != null && item.getCustomsProcedureDetails().getCustomsProcedureCode().getValue() != null &&
                                                item.getCustomsProcedureDetails().getPreviousCustomsProcedureModeCode() != null && item.getCustomsProcedureDetails().getPreviousCustomsProcedureModeCode().getValue() != null) {
                                            return item.getCustomsProcedureDetails().getCustomsProcedureCode().getValue().equals(procNum) &&
                                                    item.getCustomsProcedureDetails().getPreviousCustomsProcedureModeCode().getValue().equals(prevElement);
                                        }
                                        return false;
                                    }
                            )) {

                        if (refNumber == null && dto.getMessageLog().getCodeFeature() == null || dto.getMessageLog().getCodeFeature() != null && dto.getMessageLog().getCodeFeature().equals(VDT)) {
                            return true;
                        } else {
                            log.debug("Гр.7 заполнена или имеет неверное значение!");
                            commonReportSaveService.addNewReport("7", "2.4", "Присутствуют особенности декларирования или неверный код");
                        }
                    } else {
                        log.debug("В гр.37 Указан не верные коды! Автовыпуск прерван.");
                        commonReportSaveService.addNewReport("37", "2.3", "В гр.37 не верно указана процедура");
                    }
                }
            } else {
                log.debug("Не соответствует тип декларации");
                commonReportSaveService.addNewReport("1", "2.1", "Не соответствует тип декларации");
            }
        } else {
            log.debug("Товары отсутствуют! Автовыпуск прерван.");
            commonReportSaveService.addNewReport("32", null, "Товары отсутствуют!");
        }
        return false;
    }
}