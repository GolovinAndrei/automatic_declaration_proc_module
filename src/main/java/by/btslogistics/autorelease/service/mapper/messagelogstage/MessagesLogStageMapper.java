package by.btslogistics.autorelease.service.mapper.messagelogstage;


import by.btslogistics.autorelease.dao.model.messageslogstage.MessagesLogStage;
import by.btslogistics.autorelease.service.dto.messageslogstage.MessagesLogStageDto;
import by.btslogistics.commons.service.mapper.CommonMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface MessagesLogStageMapper extends CommonMapper<MessagesLogStageDto, MessagesLogStage> {

    @Mapping(target = "messageLogId", source = "messageLog.id")
    MessagesLogStageDto toDto(MessagesLogStage messagesLogStage);

    @Mapping(target = "messageLog.id", source = "messageLogId")
    MessagesLogStage toEntity(MessagesLogStageDto messagesLogStageDto);

}
