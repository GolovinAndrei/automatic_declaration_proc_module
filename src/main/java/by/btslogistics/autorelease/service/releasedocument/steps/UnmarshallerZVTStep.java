package by.btslogistics.autorelease.service.releasedocument.steps;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.unmarshaller.UnmarshallerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static by.btslogistics.commons.service.xml.utils.xml.XMLUtils.getBodyAsString;

public class UnmarshallerZVTStep extends RegistrationDocument {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnmarshallerDtStep.class);

    private final UnmarshallerService unmarshallerService;

    public UnmarshallerZVTStep(UnmarshallerService unmarshallerService) {
        this.unmarshallerService = unmarshallerService;
    }

    @Override
    public RegistrationDto doStep(RegistrationDto dto) {
/*
        if (BCM_01.ZVT.name().equals(dto.getMessageLog().getTypeDoc())) {
            String body = getBodyAsString(dto.getMessageLog().getMsgXml(), TypeDocXSDSchema.ZVT.getStartTag(), TypeDocXSDSchema.ZVT.getEndTag());
            GoodsReleaseApplicationType goodsReleaseApplicationType = unmarshallerService.unmarshal(body, ObjectFactory.class);
            dto.setGoodsReleaseApplicationType(goodsReleaseApplicationType);

            LOGGER.debug("Unmarshall ZVT : {}", goodsReleaseApplicationType);
        }*/
        return nextStep(dto);
    }

}
