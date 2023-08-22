package by.btslogistics.autorelease.service.read.messageslogstage;

import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import by.btslogistics.autorelease.service.dto.messageslogstage.MessagesLogStageDto;

import java.util.List;

public interface MessagesLogStageReadService {

    List<MessagesLogStageDto> getStagesByMessageLogId (MessageLogDto messageLogDto);

}
