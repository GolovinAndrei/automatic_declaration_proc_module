package by.btslogistics.autorelease.service.read.quota;

import by.btslogistics.autorelease.service.dto.quota.QuotaReportDto;

import java.util.List;

public interface QuotaReportReadService {

    List<QuotaReportDto> getReportByMessageLogId (String messageLogId);
}
