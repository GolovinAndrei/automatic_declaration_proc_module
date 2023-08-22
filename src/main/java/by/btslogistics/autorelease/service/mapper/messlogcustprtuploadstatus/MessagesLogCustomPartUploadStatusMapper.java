package by.btslogistics.autorelease.service.mapper.messlogcustprtuploadstatus;

import by.btslogistics.autorelease.dao.model.messlogcustprtuploadstatus.MessagesLogCustomPartUploadStatus;
import by.btslogistics.commons.dao.dto.messlogcustprtuploadstatus.MessagesLogCustomPartUploadStatusDto;
import by.btslogistics.commons.service.mapper.CommonMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessagesLogCustomPartUploadStatusMapper
        extends CommonMapper<MessagesLogCustomPartUploadStatusDto, MessagesLogCustomPartUploadStatus> {

}
