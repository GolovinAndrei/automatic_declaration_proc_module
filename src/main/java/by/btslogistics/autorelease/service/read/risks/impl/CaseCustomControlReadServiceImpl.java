package by.btslogistics.autorelease.service.read.risks.impl;

import by.btslogistics.autorelease.dao.model.risks.CaseCustomControl;
import by.btslogistics.autorelease.dao.repository.risks.CaseCustomControlRepository;
import by.btslogistics.autorelease.service.dto.risks.CaseCustomControlDto;
import by.btslogistics.autorelease.service.mapper.risks.CaseCustomControlMapper;
import by.btslogistics.autorelease.service.read.risks.CaseCustomControlReadService;
import by.btslogistics.commons.service.exception.common.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@PropertySources({
        @PropertySource("classpath:properties/app-exception-messages.properties")
})

@Service
@Slf4j
public class CaseCustomControlReadServiceImpl implements CaseCustomControlReadService {

    @Value("${messages.log.not.null}")
    private String paramNotNull;

    private CaseCustomControlRepository repository;

    private CaseCustomControlMapper mapper;

    @Autowired
    public CaseCustomControlReadServiceImpl(CaseCustomControlRepository repository, CaseCustomControlMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CaseCustomControlDto getByDtNumber (String dtNumber){
      if (dtNumber == null) throw new CommonException(paramNotNull);
      log.debug("Find record in table Case_Custom_Control with registration number {}", dtNumber);
        CaseCustomControl caseCustomControl = repository.findByDtRegNumber(dtNumber).orElse(null);
        log.debug("Record have been found: {}", caseCustomControl);
        if (caseCustomControl != null) {
            return mapper.toDto(caseCustomControl);
        }
        return null;
    }
}
