package by.btslogistics.autorelease.service.releasedocument.createregistrationnumber;

import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;

public interface CreateRegistrationNumberService {

    /**
     * Создание номера регистрации (короткий)
     */
    String getRegistrationNumberShort(MessageLogDto dto, String code);

}
