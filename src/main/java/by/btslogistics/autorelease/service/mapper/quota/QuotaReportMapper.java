package by.btslogistics.autorelease.service.mapper.quota;

import by.btslogistics.autorelease.dao.model.quota.QuotaReport;
import by.btslogistics.autorelease.service.dto.quota.QuotaReportDto;
import by.btslogistics.commons.service.mapper.CommonMapper;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface QuotaReportMapper extends CommonMapper<QuotaReportDto, QuotaReport> {

}


