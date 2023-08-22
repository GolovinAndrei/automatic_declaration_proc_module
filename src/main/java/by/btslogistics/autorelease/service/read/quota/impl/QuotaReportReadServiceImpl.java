package by.btslogistics.autorelease.service.read.quota.impl;

import by.btslogistics.autorelease.dao.model.quota.QuotaReport;
import by.btslogistics.autorelease.dao.repository.quota.QuotaReportRepository;
import by.btslogistics.autorelease.service.dto.quota.QuotaReportDto;
import by.btslogistics.autorelease.service.mapper.quota.QuotaReportMapper;
import by.btslogistics.autorelease.service.read.messageslogstage.impl.MessageLogReadServiceImpl;
import by.btslogistics.autorelease.service.read.quota.QuotaReportReadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuotaReportReadServiceImpl implements QuotaReportReadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageLogReadServiceImpl.class);

    @Autowired
    private QuotaReportRepository quotaReportRepository;

    @Autowired
    private QuotaReportMapper mapper;


    @Override
    @Transactional
    public List<QuotaReportDto> getReportByMessageLogId(String messageLogId) {
        if (messageLogId!=null) {
            LOGGER.debug("Get quota report for MessageLog with id: {}", messageLogId);
            List<QuotaReport> quotaReportList = quotaReportRepository.findByMessageLogId(messageLogId);
            if (quotaReportList!=null && !quotaReportList.isEmpty()) {
                LOGGER.debug("Count of records in report for MessagesLog: {}", quotaReportList.size());
                return (List<QuotaReportDto>) mapper.toListDto(quotaReportList);
            }
        }
        return null;
    }
}
