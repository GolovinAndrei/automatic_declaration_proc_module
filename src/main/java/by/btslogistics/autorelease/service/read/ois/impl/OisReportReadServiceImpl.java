package by.btslogistics.autorelease.service.read.ois.impl;

import by.btslogistics.autorelease.dao.model.ois.OisReport;
import by.btslogistics.autorelease.dao.repository.ois.OisReportRepository;
import by.btslogistics.autorelease.service.dto.ois.OisReportDto;
import by.btslogistics.autorelease.service.mapper.ois.OisReportMapper;
import by.btslogistics.autorelease.service.read.ois.OisReportReadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OisReportReadServiceImpl implements OisReportReadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OisReportReadServiceImpl.class);

    @Autowired
    private OisReportMapper mapper;

    @Autowired
    private OisReportRepository oisReportRepository;


    @Override
    @Transactional
    public List<OisReportDto> getAllOisRecordsForGoods (String dtId, Integer goodsNumber) {
        if (dtId != null && goodsNumber != null) {
            LOGGER.debug("Get all records from OIS report for goods number {}" , goodsNumber);
            List<OisReport> oisReport = oisReportRepository.findAllByDtIdAndGoodsNumber(dtId, goodsNumber);
            if (oisReport!=null && !oisReport.isEmpty()) {
                LOGGER.debug("Count of records: {}", oisReport.size());
                return (List<OisReportDto>) mapper.toListDto(oisReport);
            }
        }
        return null;
    }
}
