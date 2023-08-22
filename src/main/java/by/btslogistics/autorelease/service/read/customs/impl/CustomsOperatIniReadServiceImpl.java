package by.btslogistics.autorelease.service.read.customs.impl;

import by.btslogistics.autorelease.dao.model.customs.CustomsOperatIni;
import by.btslogistics.autorelease.dao.repository.customs.CustomsOperatIniRepository;
import by.btslogistics.autorelease.service.dto.customs.CustomsOperatIniDto;
import by.btslogistics.autorelease.service.mapper.customs.CustomsOperatIniMapper;
import by.btslogistics.autorelease.service.read.customs.CustomsOperatIniReadService;
import by.btslogistics.commons.service.exception.common.CommonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@PropertySources({
        @PropertySource("classpath:properties/app-exception-messages.properties")
})
@Service
public class CustomsOperatIniReadServiceImpl implements CustomsOperatIniReadService {

    @Value("${messages.log.not.null}")
    private String paramNotNull;

    private final CustomsOperatIniRepository repository;

    private final CustomsOperatIniMapper mapper;

    public CustomsOperatIniReadServiceImpl(CustomsOperatIniRepository repository, CustomsOperatIniMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CustomsOperatIniDto getEntityByDocType(String docType, String operation) {
        if (docType == null || operation == null) throw new CommonException(paramNotNull);
        CustomsOperatIni customsOperatIni = repository.getCustomsOperatIniEntitiesByDocumTypeAndDate(docType, LocalDateTime.now(), operation);
        return mapper.toDto(customsOperatIni);
    }
}
