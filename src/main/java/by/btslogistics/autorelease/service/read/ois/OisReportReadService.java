package by.btslogistics.autorelease.service.read.ois;

import by.btslogistics.autorelease.service.dto.ois.OisReportDto;

import java.util.List;

public interface OisReportReadService {

    List<OisReportDto> getAllOisRecordsForGoods (String dtId, Integer goodsNumber);
}
