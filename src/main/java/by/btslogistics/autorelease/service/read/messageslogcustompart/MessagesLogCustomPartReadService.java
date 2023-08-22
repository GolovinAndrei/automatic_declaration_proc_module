package by.btslogistics.autorelease.service.read.messageslogcustompart;

import by.btslogistics.autorelease.service.dto.messageslogcustompart.MessagesLogCustomPartDto;

public interface MessagesLogCustomPartReadService {

    MessagesLogCustomPartDto getMessagesLogCustomPartByMessagesLogId(String messagesLogId);
}
