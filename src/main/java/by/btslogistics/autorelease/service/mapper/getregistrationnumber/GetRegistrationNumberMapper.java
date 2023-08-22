package by.btslogistics.autorelease.service.mapper.getregistrationnumber;


import by.btslogistics.autorelease.dao.model.getregistrationnumber.GetRegistrationNumber;
import by.btslogistics.autorelease.service.dto.getregistrationnumber.GetRegistrationNumberDto;
import by.btslogistics.commons.service.mapper.CommonMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GetRegistrationNumberMapper extends CommonMapper<GetRegistrationNumberDto, GetRegistrationNumber> {
}
