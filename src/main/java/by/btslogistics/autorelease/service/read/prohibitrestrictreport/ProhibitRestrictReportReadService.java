package by.btslogistics.autorelease.service.read.prohibitrestrictreport;

import by.btslogistics.autorelease.service.dto.prohibitrestrictreport.ProhibitRestrictReportDto;

import java.util.List;

public interface ProhibitRestrictReportReadService {

    List<ProhibitRestrictReportDto> getProhibitRestrictReportByMessageLogId (String toMessageLogId);
}
