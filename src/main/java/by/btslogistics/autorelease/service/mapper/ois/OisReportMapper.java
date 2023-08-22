package by.btslogistics.autorelease.service.mapper.ois;

import by.btslogistics.autorelease.dao.model.ois.OisReport;
import by.btslogistics.autorelease.service.dto.ois.OisReportDto;
import by.btslogistics.commons.service.mapper.CommonMapper;
import org.mapstruct.Mapper;

    @Mapper(componentModel = "spring")
    public interface OisReportMapper extends CommonMapper<OisReportDto, OisReport> {
}
