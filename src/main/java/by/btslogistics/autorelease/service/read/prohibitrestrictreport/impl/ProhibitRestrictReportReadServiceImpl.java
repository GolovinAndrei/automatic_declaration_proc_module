package by.btslogistics.autorelease.service.read.prohibitrestrictreport.impl;

import by.btslogistics.autorelease.dao.model.prohibitrestrictreport.ProhibitRestrictReport;
import by.btslogistics.autorelease.dao.repository.prohibitrestrictreport.ProhibitRestrictReportRepository;
import by.btslogistics.autorelease.service.dto.prohibitrestrictreport.ProhibitRestrictReportDto;
import by.btslogistics.autorelease.service.mapper.prohibitrestrictreport.ProhibitRestrictReportMapper;
import by.btslogistics.autorelease.service.read.prohibitrestrictreport.ProhibitRestrictReportReadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProhibitRestrictReportReadServiceImpl implements ProhibitRestrictReportReadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProhibitRestrictReportReadServiceImpl.class);

    @Autowired
    private ProhibitRestrictReportRepository prohibitRestrictReportRepository;

    @Autowired
    private ProhibitRestrictReportMapper mapper;


    @Override
    @Transactional
    public List<ProhibitRestrictReportDto> getProhibitRestrictReportByMessageLogId(String toMessageLogId) {
        if (toMessageLogId != null) {
            LOGGER.debug("Get prohibit and restrict report for MessageLog with id: {}", toMessageLogId);
            List<ProhibitRestrictReport> reportsDto = prohibitRestrictReportRepository.getAllByToMessagesLogId(toMessageLogId);
            if (reportsDto != null && !reportsDto.isEmpty()) {
                LOGGER.debug("Count of records in report for MessagesLog: {}", reportsDto.size());
                return (List<ProhibitRestrictReportDto>) mapper.toListDto(reportsDto);
            }
        }
        return null;
    }
}

