package by.btslogistics.autorelease.service.mapper.risks;

import by.btslogistics.autorelease.dao.model.risks.CaseCustomControl;
import by.btslogistics.autorelease.service.dto.risks.CaseCustomControlDto;
import by.btslogistics.commons.service.mapper.CommonMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CaseCustomControlMapper extends CommonMapper<CaseCustomControlDto, CaseCustomControl> {
}
