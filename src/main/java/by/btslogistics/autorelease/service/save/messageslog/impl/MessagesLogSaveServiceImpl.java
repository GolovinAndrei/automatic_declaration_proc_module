package by.btslogistics.autorelease.service.save.messageslog.impl;

import by.btslogistics.autorelease.dao.model.messageslog.MessageLog;
import by.btslogistics.autorelease.dao.repository.messageslog.MessagesLogRepository;
import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import by.btslogistics.autorelease.service.mapper.messageslog.MessagesLogMapper;
import by.btslogistics.autorelease.service.save.messageslog.MessagesLogSaveService;
import by.btslogistics.commons.service.exception.common.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MessagesLogSaveServiceImpl implements MessagesLogSaveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesLogSaveServiceImpl.class);

    @Value("${messages.log.not.null}")
    private String paramNotNull;

    private final MessagesLogRepository repository;

    private final MessagesLogMapper mapper;

    @Autowired
    public MessagesLogSaveServiceImpl(MessagesLogRepository repository,
                                      MessagesLogMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public MessageLogDto setToMessageLog(MessageLogDto dto) {
        if (dto == null) throw new CommonException(paramNotNull);
        MessageLog messageLog = mapper.toEntity(dto);
        MessageLog entitySaved = this.repository.save(messageLog);
        LOGGER.debug("Saved messagesLog with id: {}", entitySaved.getId());
        return mapper.toDto(entitySaved);
    }
}
