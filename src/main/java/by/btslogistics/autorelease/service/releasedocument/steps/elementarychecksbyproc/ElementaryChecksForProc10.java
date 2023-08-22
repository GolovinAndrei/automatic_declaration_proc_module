package by.btslogistics.autorelease.service.releasedocument.steps.elementarychecksbyproc;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.releasedocument.steps.RegistrationDocument;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * Первичные проверки для декларации с кодом процедуры 10
 */
@Slf4j
public class ElementaryChecksForProc10 extends RegistrationDocument {

    private final CommonReportSaveService commonReportSaveService;

    public static List<String> tzCountries = Arrays.asList("BY", "RU", "KZ", "AM", "KG");

    public ElementaryChecksForProc10(CommonReportSaveService commonReportSaveService) {
        this.commonReportSaveService = commonReportSaveService;
    }

    @Override
    public RegistrationDto doStep(RegistrationDto dto) {

        if (dto.getGoodsDeclarationTypeNew() != null){
            by.btslogistics.xsdclasses.eec.v1_4_0.eec.r._036.goodsdeclaration.v1_4_0.GoodsDeclarationType newDoc = dto.getGoodsDeclarationTypeNew();

        if (newDoc.getDeclarationGoodsShipmentDetails().getDestinationCountryDetails() != null) {
            if (!tzCountries.contains(newDoc.getDeclarationGoodsShipmentDetails().getDestinationCountryDetails().getCACountryCode().getValue())) {
                log.debug("First checks block with criteria 1 have completed");
                dto.setElementaryChecksPassed(true);
                return nextStep(dto);
            } else {
                log.debug("Товар не вывозится за пределы ЕАЭС!");
                commonReportSaveService.addNewReport("17", "9.1", "Товар не вывозится за пределы ЕАЭС");
            }
        }
    } else {
            eec.r._036.goodsdeclaration.v1_3_1.GoodsDeclarationType oldDoc = dto.getGoodsDeclarationTypeOld();

            if (oldDoc.getDeclarationGoodsShipmentDetails().getDestinationCountryDetails() != null) {
                if (!tzCountries.contains(oldDoc.getDeclarationGoodsShipmentDetails().getDestinationCountryDetails().getCACountryCode().getValue())) {
                    log.debug("First checks block with criteria 1 have completed");
                    dto.setElementaryChecksPassed(true);
                    return nextStep(dto);
                } else {
                    log.debug("Товар не вывозится за пределы ЕАЭС!");
                    commonReportSaveService.addNewReport("17", "9.1", "Товар не вывозится за пределы ЕАЭС");
                }
            }

    }
        commonReportSaveService.addNewReport("17", "9.1", "Контроллируемый тег отсутствует!");
        log.debug("The tag destinationCountryDetails is absent!");
        return dto;
    }
}
