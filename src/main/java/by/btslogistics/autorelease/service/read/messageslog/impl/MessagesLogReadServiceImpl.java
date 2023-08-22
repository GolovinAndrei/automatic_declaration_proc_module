package by.btslogistics.autorelease.service.read.messageslog.impl;

import by.btslogistics.autorelease.dao.model.messageslog.MessageLog;
import by.btslogistics.autorelease.dao.repository.messageslog.MessagesLogRepository;
import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import by.btslogistics.autorelease.service.mapper.messageslog.MessagesLogMapper;
import by.btslogistics.autorelease.service.read.messageslog.MessagesLogReadService;
import by.btslogistics.commons.service.exception.common.CommonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@PropertySources({
        @PropertySource("classpath:properties/app-exception-messages.properties")
})
@Service
public class MessagesLogReadServiceImpl implements MessagesLogReadService {

    @Value("${messages.log.not.null}")
    private String paramNotNull;

    private final MessagesLogRepository repository;

    private final MessagesLogMapper mapper;


    public MessagesLogReadServiceImpl(MessagesLogRepository repository, MessagesLogMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public MessageLogDto getById(String id) {

        if (id == null) throw new CommonException(paramNotNull);
        MessageLog entity = repository.findById(id).orElse(null);
        if (entity != null) {
            return mapper.toDto(entity);
        } else throw new CommonException(paramNotNull);
    }
}
