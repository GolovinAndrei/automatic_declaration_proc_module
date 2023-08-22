package by.btslogistics.autorelease.service.mapper.messagesout;

import by.btslogistics.autorelease.dao.model.messagesout.MessageOut;
import by.btslogistics.autorelease.service.dto.messagesout.MessageOutDto;
import by.btslogistics.commons.service.mapper.CommonMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessagesOutMapper extends CommonMapper<MessageOutDto, MessageOut> {
}