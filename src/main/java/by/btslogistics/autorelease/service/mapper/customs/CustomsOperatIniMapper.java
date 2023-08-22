package by.btslogistics.autorelease.service.mapper.customs;

import by.btslogistics.autorelease.dao.model.customs.CustomsOperatIni;
import by.btslogistics.autorelease.service.dto.customs.CustomsOperatIniDto;
import by.btslogistics.commons.service.mapper.CommonMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomsOperatIniMapper extends CommonMapper<CustomsOperatIniDto, CustomsOperatIni> {
}
