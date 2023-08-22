package by.btslogistics.autorelease.service.releasedocument.steps;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.unmarshaller.UnmarshallerService;
import by.btslogistics.commons.dao.enums.typeobjects.TypeDocForXsdSchemaVersion;
import by.btslogistics.commons.dao.enums.typeobjects.TypeDocXSDSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static by.btslogistics.commons.service.xml.utils.xml.XMLUtils.getBodyAsString;

public class UnmarshallerDtStep extends RegistrationDocument{

    private static final Logger LOGGER = LoggerFactory.getLogger(UnmarshallerDtStep.class);

    private final UnmarshallerService unmarshallerService;

    public UnmarshallerDtStep(UnmarshallerService unmarshallerService) {
        this.unmarshallerService = unmarshallerService;
    }


    @Override
    public RegistrationDto doStep (RegistrationDto dto) {

        String version = TypeDocForXsdSchemaVersion.findByType(dto.getMessageLog().getTypeDoc(), dto.getMessageLog().getMsgXml());
            String body ="";
            Class<?> factory = null;

            switch (version) {

                case "DT_v140":
                    LOGGER.debug("Parsing of document v1.4.0");
                    body = getBodyAsString(dto.getMessageLog().getMsgXml(), TypeDocXSDSchema.DT_V1_4_0.getStartTag(), TypeDocXSDSchema.DT_V1_4_0.getEndTag());
                    factory = by.btslogistics.xsdclasses.eec.v1_4_0.eec.r._036.goodsdeclaration.v1_4_0.ObjectFactory.class;
                    dto.setGoodsDeclarationTypeNew(unmarshallerService.unmarshal(body, factory));
                    break;

                case "DT_v131":
                    LOGGER.debug("Parsing of document v1.3.1");
                    body = getBodyAsString(dto.getMessageLog().getMsgXml(), TypeDocXSDSchema.DT_V1_3_1.getStartTag(), TypeDocXSDSchema.DT_V1_3_1.getEndTag());
                    factory = eec.r._036.goodsdeclaration.v1_3_1.ObjectFactory.class;
                    dto.setGoodsDeclarationTypeOld(unmarshallerService.unmarshal(body, factory));
                  break;
            }
        return nextStep(dto);
    }
}
