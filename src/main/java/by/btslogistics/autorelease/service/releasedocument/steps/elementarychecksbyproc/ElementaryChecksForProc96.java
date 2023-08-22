package by.btslogistics.autorelease.service.releasedocument.steps.elementarychecksbyproc;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.releasedocument.steps.RegistrationDocument;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;
import by.btslogistics.autorelease.web.rest.proxyfeign.HelperRestProxy;
import by.btslogistics.commons.dto.nsi.NsiReestrMagDto;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Первичные проверки для декларации с кодом процедуры 96
 */
@Slf4j
public class ElementaryChecksForProc96 extends RegistrationDocument {

    private final HelperRestProxy helperRestProxy;

    private final CommonReportSaveService commonReportSaveService;

    public ElementaryChecksForProc96(HelperRestProxy helperRestProxy, CommonReportSaveService commonReportSaveService) {
        this.helperRestProxy = helperRestProxy;
        this.commonReportSaveService = commonReportSaveService;
    }

    @Override
    public RegistrationDto doStep(RegistrationDto dto) {

        if (dto.getGoodsDeclarationTypeNew() != null) {
            by.btslogistics.xsdclasses.eec.v1_4_0.eec.r._036.goodsdeclaration.v1_4_0.GoodsDeclarationType newDoc = dto.getGoodsDeclarationTypeNew();

            //3.1. Гр. 14 «Декларант». В правом верхнем углу графы после знака «№» указан УНП декларанта, имеющийся в таблице REESTR_MAG схемы NSI
            try {
                List<NsiReestrMagDto> allMags = (List<NsiReestrMagDto>) helperRestProxy.getAllMags();
                if (allMags.stream().noneMatch(mag -> mag.getUnp().equals(newDoc.getDeclarantDetails().getTaxpayerId()) && mag.getDOff().isAfter(LocalDateTime.now()))) {
                    log.debug("Юридического лица с УНП {} нет в реестре владельцев магазинов беспошлинной торговли", newDoc.getDeclarantDetails().getTaxpayerId());
                    commonReportSaveService.addNewReport("14", "3.1", "Декларирующее лицо не включено в реестр владельцев магазинов беспошлинной торговли");
                    return dto;
                }
            } catch (RetryableException e) {
                e.printStackTrace();
                commonReportSaveService.addNewReport("14", "3.1", "Helper не отвечает!");
                return dto;
            }

            log.debug("Common checks for criteria 3 have been passed");
            dto.setElementaryChecksPassed(true);
            return nextStep(dto);
        } else {

            eec.r._036.goodsdeclaration.v1_3_1.GoodsDeclarationType oldDoc = dto.getGoodsDeclarationTypeOld();

            //3.1. Гр. 14 «Декларант». В правом верхнем углу графы после знака «№» указан УНП декларанта, имеющийся в таблице REESTR_MAG схемы NSI
            try {
                List<NsiReestrMagDto> allMags = (List<NsiReestrMagDto>) helperRestProxy.getAllMags();
                if (allMags.stream().noneMatch(mag -> mag.getUnp().equals(oldDoc.getDeclarantDetails().getTaxpayerId()) && mag.getDOff().isAfter(LocalDateTime.now()))) {
                    log.debug("Юридического лица с УНП {} нет в реестре владельцев магазинов беспошлинной торговли", oldDoc.getDeclarantDetails().getTaxpayerId());
                    commonReportSaveService.addNewReport("14", "3.1", "Декларирующее лицо не включено в реестр владельцев магазинов беспошлинной торговли");
                    return dto;
                }
            } catch (RetryableException e) {
                e.printStackTrace();
                commonReportSaveService.addNewReport("14", "3.1", "Helper не отвечает!");
                return dto;
            }

            log.debug("Common checks for criteria 3 have been passed");
            dto.setElementaryChecksPassed(true);
            return nextStep(dto);
        }
    }
}
