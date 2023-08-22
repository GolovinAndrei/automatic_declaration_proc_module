package by.btslogistics.autorelease.service.save.commonreport.impl;

import by.btslogistics.autorelease.dao.model.commonreport.CommonReport;
import by.btslogistics.autorelease.dao.repository.commonreport.CommonReportRepository;
import by.btslogistics.autorelease.service.dto.commonreport.CommonReportDto;
import by.btslogistics.autorelease.service.mapper.commonreport.CommonReportMapper;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;
import by.btslogistics.commons.dao.enums.audit.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class CommonReportSaveServiceImpl implements CommonReportSaveService {

    private final CommonReportRepository commonReportRepository;

    private final CommonReportMapper commonReportMapper;

    private String documentId;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonReportSaveServiceImpl.class);

    @Autowired
    public CommonReportSaveServiceImpl(CommonReportRepository commonReportRepository, CommonReportMapper commonReportMapper) {
        this.commonReportRepository = commonReportRepository;
        this.commonReportMapper = commonReportMapper;
    }

    @Override
    @Transactional (isolation = Isolation.READ_COMMITTED)
    public CommonReportDto addNewReport(String numberGrafa, String codeError, String description) {
        LOGGER.debug("Record in CommonReport for messageLogId {}", documentId);
        CommonReport commonReportDb = commonReportRepository.save(commonReportMapper.toEntity(createCommonReport(numberGrafa, codeError, description)));
        LOGGER.debug("commonReport from db : {}", commonReportDb);
        return commonReportMapper.toDto(commonReportDb);
    }

    private CommonReportDto createCommonReport (String numberGrafa, String codeError, String description){

        CommonReportDto commonReportDto = new CommonReportDto();
        commonReportDto.setCodeModule(Module.AUTO_RELEASE.getCodeModule());
        commonReportDto.setCreationDate(LocalDateTime.now());
        commonReportDto.setNumberGrafa(numberGrafa);
        commonReportDto.setCodeCategory(1);
        commonReportDto.setToMessageLogId(documentId);
        commonReportDto.setCodeError(codeError);
        commonReportDto.setDescriptionError(description);

        return  commonReportDto;
    }
    @Override
    public void setDocumentId(String documentId){
        this.documentId = documentId;
    }
}
