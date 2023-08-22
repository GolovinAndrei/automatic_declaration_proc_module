package by.btslogistics.autorelease.service.mapper.messageslogcustompart;

import by.btslogistics.autorelease.dao.model.messageslogcustompart.MessagesLogCustomPart;
import by.btslogistics.autorelease.service.dto.messageslogcustompart.MessagesLogCustomPartDto;
import by.btslogistics.commons.service.mapper.CommonMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessagesLogCustomPartMapper extends CommonMapper<MessagesLogCustomPartDto, MessagesLogCustomPart> {

}
