package by.btslogistics.autorelease.service.read.getregistrationnumber.impl;

import by.btslogistics.autorelease.dao.model.getregistrationnumber.GetRegistrationNumber;
import by.btslogistics.autorelease.dao.repository.getregistrationnumber.GetRegistrationNumberRepository;
import by.btslogistics.autorelease.service.dto.getregistrationnumber.GetRegistrationNumberDto;
import by.btslogistics.autorelease.service.mapper.getregistrationnumber.GetRegistrationNumberMapper;
import by.btslogistics.autorelease.service.read.getregistrationnumber.GetRegistrationNumberReadService;
import by.btslogistics.commons.service.exception.common.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@PropertySources({
        @PropertySource("classpath:properties/app-exception-messages.properties")
})
@Service
public class GetRegistrationNumberReadServiceImpl implements GetRegistrationNumberReadService {

    @Value("${messages.log.not.null}")
    private String paramNotNull;

    private final GetRegistrationNumberRepository repository;

    private final GetRegistrationNumberMapper mapper;


    @Autowired
    public GetRegistrationNumberReadServiceImpl(GetRegistrationNumberRepository repository, GetRegistrationNumberMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public GetRegistrationNumberDto getNextNumber(Long isIn, String reviewType, Integer year, Long house, Long point, String logType) {
        if (isIn == null || reviewType == null || year == null || house == null || point == null || logType == null)
            throw new CommonException(paramNotNull);

        GetRegistrationNumber aps2020Num = repository.getNumber(logType, isIn, reviewType, year, house, point);

        if (aps2020Num == null) {
            aps2020Num = new GetRegistrationNumber();
            aps2020Num.setIsIn(isIn);
            aps2020Num.setHouse(house);
            aps2020Num.setReviewType(reviewType);
            aps2020Num.setPoint(point);
            aps2020Num.setYear(year);
            aps2020Num.setMaxNum(1L);
            aps2020Num.setVersion(LocalDateTime.now());
            aps2020Num.setLogType(logType);
        } else {
            aps2020Num.setMaxNum(aps2020Num.getMaxNum() + 1);
        }

        repository.save(aps2020Num);

        return mapper.toDto(aps2020Num);
    }
}
