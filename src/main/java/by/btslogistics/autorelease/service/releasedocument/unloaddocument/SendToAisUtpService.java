package by.btslogistics.autorelease.service.releasedocument.unloaddocument;

import by.btslogistics.autorelease.service.dto.RegistrationDto;

public interface SendToAisUtpService {

    String createXml (RegistrationDto dto);

    void sendXmlToAisUtp (RegistrationDto dto);
}
