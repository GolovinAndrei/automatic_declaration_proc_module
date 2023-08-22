package by.btslogistics.autorelease.service.releasedocument.steps;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.releasedocument.unloaddocument.SendToAisUtpService;
import by.btslogistics.autorelease.web.rest.proxyfeign.KstpRestProxy;
import feign.RetryableException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class AfterAutoreleaseActions extends RegistrationDocument{

    private final KstpRestProxy kstpRestProxy;

    private final SendToAisUtpService sendToAisUtpService;

    @Override
    public RegistrationDto doStep(RegistrationDto dto) {
        try {
            log.info ("Start kstp");
            kstpRestProxy.startRemovedControlKstp(dto.getMessageLog().getId());
        } catch (RetryableException e) {
            e.printStackTrace();
        }

        if (dto.getGoodsDeclarationTypeNew()!=null && dto.getGoodsDeclarationTypeNew().getDeclarationGoodsShipmentDetails().getFactPaymentDetails() != null
            || dto.getGoodsDeclarationTypeOld()!=null && dto.getGoodsDeclarationTypeOld().getDeclarationGoodsShipmentDetails().getFactPaymentDetails() != null) {
            //sendToAisUtpService.sendXmlToAisUtp(dto);
        }

        return nextStep(dto);
    }
}
