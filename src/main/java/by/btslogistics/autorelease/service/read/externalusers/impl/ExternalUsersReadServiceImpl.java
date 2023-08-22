package by.btslogistics.autorelease.service.read.externalusers.impl;

import by.btslogistics.autorelease.dao.model.externalusers.ExternalUsers;
import by.btslogistics.autorelease.dao.repository.externalusers.ExternalUsersRepository;
import by.btslogistics.autorelease.service.dto.externalusers.ExternalUsersDto;
import by.btslogistics.autorelease.service.mapper.externalusers.ExternalUsersMapper;
import by.btslogistics.autorelease.service.read.externalusers.ExternalUsersReadService;
import by.btslogistics.commons.service.exception.common.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;

@PropertySources({
        @PropertySource("classpath:properties/app-exception-messages.properties")
})
@Service
public class ExternalUsersReadServiceImpl implements ExternalUsersReadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalUsersReadServiceImpl.class);

    @Value("${messages.log.not.null}")
    private String paramNotNull;

    private final ExternalUsersRepository repository;

    private final ExternalUsersMapper mapper;

    @Autowired
    public ExternalUsersReadServiceImpl(ExternalUsersRepository repository,
                                        ExternalUsersMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public Iterable<ExternalUsersDto> getByCertSerialNumber(String certSerialNumber) {

        if (certSerialNumber == null) throw new CommonException(paramNotNull);

        Iterable<ExternalUsers> byCertSerialNumber = this.repository.findByCertSerialNumber(certSerialNumber);

        return mapper.toListDto(byCertSerialNumber);
    }
}
