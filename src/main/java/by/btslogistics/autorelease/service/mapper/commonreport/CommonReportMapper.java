package by.btslogistics.autorelease.service.mapper.commonreport;

import by.btslogistics.autorelease.dao.model.commonreport.CommonReport;
import by.btslogistics.autorelease.service.dto.commonreport.CommonReportDto;
import by.btslogistics.commons.service.mapper.CommonMapper;
import org.mapstruct.Mapper;


@Mapper (componentModel = "spring")
public interface CommonReportMapper extends CommonMapper<CommonReportDto, CommonReport> {
}
