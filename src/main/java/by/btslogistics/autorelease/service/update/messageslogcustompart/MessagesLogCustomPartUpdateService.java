package by.btslogistics.autorelease.service.update.messageslogcustompart;

import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;

public interface MessagesLogCustomPartUpdateService {

    void setLNPAfterAutoReleaseByMessagesLogId (MessageLogDto messageLogDto, String lnp);
}
