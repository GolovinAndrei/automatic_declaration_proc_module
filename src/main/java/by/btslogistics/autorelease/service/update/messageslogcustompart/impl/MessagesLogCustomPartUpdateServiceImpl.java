package by.btslogistics.autorelease.service.update.messageslogcustompart.impl;

import by.btslogistics.autorelease.dao.repository.messageslogcustompart.MessagesLogCustomPartRepository;
import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import by.btslogistics.autorelease.service.dto.messageslogcustompart.MessagesLogCustomPartDto;
import by.btslogistics.autorelease.service.mapper.messageslogcustompart.MessagesLogCustomPartMapper;
import by.btslogistics.autorelease.service.read.messageslogcustompart.MessagesLogCustomPartReadService;
import by.btslogistics.autorelease.service.update.messageslogcustompart.MessagesLogCustomPartUpdateService;
import by.btslogistics.commons.service.exception.common.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessagesLogCustomPartUpdateServiceImpl implements MessagesLogCustomPartUpdateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesLogCustomPartUpdateServiceImpl.class);

    @Value("${messages.log.not.null}")
    private String paramNotNull;

    private final MessagesLogCustomPartRepository messagesLogCustomPartRepository;

    private final MessagesLogCustomPartMapper messagesLogCustomPartMapper;

    private final MessagesLogCustomPartReadService messagesLogCustomPartReadService;

    @Autowired
    public MessagesLogCustomPartUpdateServiceImpl(MessagesLogCustomPartRepository messagesLogCustomPartRepository, MessagesLogCustomPartMapper messagesLogCustomPartMapper, MessagesLogCustomPartReadService messagesLogCustomPartReadService) {
        this.messagesLogCustomPartRepository = messagesLogCustomPartRepository;
        this.messagesLogCustomPartMapper = messagesLogCustomPartMapper;
        this.messagesLogCustomPartReadService = messagesLogCustomPartReadService;
    }


    @Override
    @Transactional
    public void setLNPAfterAutoReleaseByMessagesLogId(MessageLogDto messageLogDto, String lnp) {
        if (messageLogDto == null || lnp == null) throw new CommonException(paramNotNull);
        MessagesLogCustomPartDto messagesLogCustomPartDto = messagesLogCustomPartReadService.getMessagesLogCustomPartByMessagesLogId(messageLogDto.getId());
        if (messagesLogCustomPartDto != null) {
            messagesLogCustomPartDto.setDecLnpOfficer(lnp);
            LOGGER.debug("Updated MessagesLog Custom Part, for doc id {} with LNP: {}", messageLogDto.getId(), lnp);
            messagesLogCustomPartRepository.save(messagesLogCustomPartMapper.toEntity(messagesLogCustomPartDto));
        }
    }
}
