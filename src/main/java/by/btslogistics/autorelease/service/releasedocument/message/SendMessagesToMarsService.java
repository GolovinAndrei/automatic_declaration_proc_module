package by.btslogistics.autorelease.service.releasedocument.message;

import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;

public interface SendMessagesToMarsService {

    void sendCSTM0007Message(MessageLogDto messageLogDto);

    void sendCSTM0008Message(MessageLogDto messageLogDto, String reason);

}
