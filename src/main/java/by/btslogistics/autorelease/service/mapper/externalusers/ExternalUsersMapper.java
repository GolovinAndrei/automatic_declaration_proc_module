package by.btslogistics.autorelease.service.mapper.externalusers;

import by.btslogistics.autorelease.dao.model.externalusers.ExternalUsers;
import by.btslogistics.autorelease.service.dto.externalusers.ExternalUsersDto;
import by.btslogistics.commons.service.mapper.CommonMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExternalUsersMapper extends CommonMapper<ExternalUsersDto, ExternalUsers> {
}
