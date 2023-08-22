package by.btslogistics.autorelease.service.mapper.prohibitrestrictreport;

import by.btslogistics.autorelease.dao.model.prohibitrestrictreport.ProhibitRestrictReport;
import by.btslogistics.autorelease.service.dto.prohibitrestrictreport.ProhibitRestrictReportDto;
import by.btslogistics.commons.service.mapper.CommonMapper;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ProhibitRestrictReportMapper extends CommonMapper<ProhibitRestrictReportDto, ProhibitRestrictReport> {
}
