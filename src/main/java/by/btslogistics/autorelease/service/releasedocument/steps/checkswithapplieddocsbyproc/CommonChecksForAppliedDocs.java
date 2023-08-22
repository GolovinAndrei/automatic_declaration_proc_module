package by.btslogistics.autorelease.service.releasedocument.steps.checkswithapplieddocsbyproc;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.dto.paymentreport.PaymentReportDto;
import by.btslogistics.autorelease.service.dto.risks.CaseCustomControlDto;
import by.btslogistics.autorelease.service.read.risks.CaseCustomControlReadService;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;
import by.btslogistics.autorelease.web.rest.proxyfeign.FlkResultRestProxy;
import by.btslogistics.autorelease.web.rest.proxyfeign.PaymentResultRestProxy;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class CommonChecksForAppliedDocs {

    private FlkResultRestProxy flkResultRestProxy;

    private PaymentResultRestProxy paymentResultRestProxy;

    private CaseCustomControlReadService riskReadService;

    private CommonReportSaveService commonReportSaveService;

    private static final String PAYMENT_NAME_ERROR = "Ошибка";

    private static final String RED_RISK_LEVEL = "RED";

    private static final String CLOSED_RISK_STATE = "CLOSED";

    private static final String SIGN_OF_CANCELLATION_OF_PROHIBITIONS = "С";

    public CommonChecksForAppliedDocs(FlkResultRestProxy flkResultRestProxy, PaymentResultRestProxy paymentResultRestProxy, CaseCustomControlReadService riskReadService,
                                      CommonReportSaveService commonReportSaveService) {
        this.flkResultRestProxy = flkResultRestProxy;
        this.paymentResultRestProxy = paymentResultRestProxy;
        this.riskReadService = riskReadService;
        this.commonReportSaveService = commonReportSaveService;
    }

    public CommonChecksForAppliedDocs(FlkResultRestProxy flkResultRestProxy, PaymentResultRestProxy paymentResultRestProxy, CommonReportSaveService commonReportSaveService) {
        this.flkResultRestProxy = flkResultRestProxy;
        this.paymentResultRestProxy = paymentResultRestProxy;
        this.commonReportSaveService = commonReportSaveService;
    }

    /**
     * Проверка протокола ФЛК на наличие ошибок Er2
     *
     * @return true/false
     */
    public boolean getFlkResult(RegistrationDto dto) {
        log.debug("FLK report is being analyzed");
        boolean res = false;
        try {
            Map<Integer, Integer> flkResult = flkResultRestProxy.getFlkResult(dto.getMessageLog().getId());
            if (flkResult != null) {
                if (flkResult.get(2) == 0 && flkResult.get(1)==0) {
                        log.debug("FLK - ok");
                        res = true;
                } else {
                    commonReportSaveService.addNewReport(null, "ФЛК", "Модуль ФЛК не овечает!");
                    log.debug("No FLK report!");
                }
            }
        } catch (RetryableException e) {
            e.printStackTrace();
            commonReportSaveService.addNewReport(null, "ФЛК", "Не пройден ФЛК");
            log.debug("FLK report have errors. Autorelease have canceled!");
        }
        return res;
    }

    /**
     * Проверка протокола платежей.
     *
     * @return true/false
     */
    public boolean getPaymentResult(RegistrationDto dto) {
        boolean res = false;
        log.debug("Payment report is being analyzed");
        try {
            List<PaymentReportDto> paymentReports = (List<PaymentReportDto>) paymentResultRestProxy.getPaymentReport(dto.getMessageLog().getId());
            if (paymentReports != null && !paymentReports.isEmpty()) {
                if (paymentReports.stream().noneMatch(p -> p.getNameError().startsWith(PAYMENT_NAME_ERROR))) {
                    log.debug("Payment - ok");
                    res = true;
                } else {
                    commonReportSaveService.addNewReport(null, "Платежи", "Контроль платежей не пройден!");
                    log.debug("There are not all necessary payments. Autorelease have canceled!" );
                }
            }
        } catch (RetryableException e) {
            e.printStackTrace();
            commonReportSaveService.addNewReport(null, "Платежи", "Отсутствует протокол платежей");
            log.debug("No payment report!");
        }
        return res;
    }

    /**
     * Контроль рисков ("красного" уровня)
     *
     * @return true/false
     */
    public boolean getRisksResult(RegistrationDto dto) {
        log.debug("Risks are being analyzed");
        CaseCustomControlDto riskRecord = riskReadService.getByDtNumber(dto.getMessageLog().getRegNumber());
        if (riskRecord != null) {
            if (riskRecord.getLevel().equals(RED_RISK_LEVEL) && !riskRecord.getState().equals(CLOSED_RISK_STATE)) {
                commonReportSaveService.addNewReport(null, "риски", "Риски красного уровня");
                log.debug("There are risks!");
                return false;
            }
        }
        log.debug("Risks - ok");
        return true;
    }

    /**
     * Имеет ли в ДТ товар под указанным номером признак товара, свободного от применения запретов и ограничений (признак "С")
     *
     * @param goodsNumeric - номер товара в ДТ
     * @return true/false
     */
    public boolean doesHaveItemProhibitionFreeCodeC(RegistrationDto dto, Integer goodsNumeric) {

        if (dto.getGoodsDeclarationTypeNew() != null) {
            by.btslogistics.xsdclasses.eec.v1_4_0.eec.r._036.goodsdeclaration.v1_4_0.GoodsDeclarationType newDoc = dto.getGoodsDeclarationTypeNew();

            return newDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails()
                    .stream()
                    .filter(goods -> goods.getConsignmentItemOrdinal().intValue() == goodsNumeric)
                    .anyMatch(goods -> {
                        if (goods.getGoodsProhibitionFreeCode() != null) {
                            return goods.getGoodsProhibitionFreeCode().equalsIgnoreCase(SIGN_OF_CANCELLATION_OF_PROHIBITIONS);
                        }
                        return false;
                    });
        } else {
            eec.r._036.goodsdeclaration.v1_3_1.GoodsDeclarationType oldDoc = dto.getGoodsDeclarationTypeOld();

            return oldDoc.getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails()
                    .stream()
                    .filter(goods -> goods.getConsignmentItemOrdinal().intValue() == goodsNumeric)
                    .anyMatch(goods -> {
                        if (goods.getGoodsProhibitionFreeCode() != null) {
                            return goods.getGoodsProhibitionFreeCode().equalsIgnoreCase(SIGN_OF_CANCELLATION_OF_PROHIBITIONS);
                        }
                        return false;
                    });
        }
    }
}
