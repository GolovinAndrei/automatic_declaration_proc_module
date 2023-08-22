package by.btslogistics.autorelease.service.read.messageslogcustompart.impl;

import by.btslogistics.autorelease.dao.model.messageslogcustompart.MessagesLogCustomPart;
import by.btslogistics.autorelease.dao.repository.messageslogcustompart.MessagesLogCustomPartRepository;
import by.btslogistics.autorelease.service.dto.messageslogcustompart.MessagesLogCustomPartDto;
import by.btslogistics.autorelease.service.mapper.messageslogcustompart.MessagesLogCustomPartMapper;
import by.btslogistics.autorelease.service.read.messageslogcustompart.MessagesLogCustomPartReadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MessagesLogCustomPartReadServiceImpl implements MessagesLogCustomPartReadService {

    private final MessagesLogCustomPartRepository messagesLogCustomPartRepository;

    private final MessagesLogCustomPartMapper mapper;

    @Autowired
    public MessagesLogCustomPartReadServiceImpl(MessagesLogCustomPartRepository messagesLogCustomPartRepository, MessagesLogCustomPartMapper mapper) {
        this.messagesLogCustomPartRepository = messagesLogCustomPartRepository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public MessagesLogCustomPartDto getMessagesLogCustomPartByMessagesLogId(String messagesLogId) {
        log.debug("Find messagesLogCustomPart for messagesLog ID: {}", messagesLogId);
        MessagesLogCustomPart messagesLogCustomPart = messagesLogCustomPartRepository.findByToMessagesLogId(messagesLogId).orElse(null);
        if (messagesLogCustomPart != null) {
            log.debug("MessagesLogCustomPart has been found {}", messagesLogCustomPart);
            return mapper.toDto(messagesLogCustomPart);
        }
        return null;
    }
}
