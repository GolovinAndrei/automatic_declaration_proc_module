package by.btslogistics.autorelease.service.mapper.messageslog;

import by.btslogistics.autorelease.dao.model.messageslog.MessageLog;
import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import by.btslogistics.commons.service.mapper.CommonMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessagesLogMapper extends CommonMapper<MessageLogDto, MessageLog> {
}
