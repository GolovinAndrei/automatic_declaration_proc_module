package by.btslogistics.autorelease.service.save.messageslogcustompart.impl;

import by.btslogistics.autorelease.dao.model.messageslogcustompart.MessagesLogCustomPart;
import by.btslogistics.autorelease.dao.repository.messageslogcustompart.MessagesLogCustomPartRepository;
import by.btslogistics.autorelease.service.dto.messageslogcustompart.MessagesLogCustomPartDto;
import by.btslogistics.autorelease.service.mapper.messageslogcustompart.MessagesLogCustomPartMapper;
import by.btslogistics.autorelease.service.save.messageslogcustompart.MessagesLogCustomPartSaveService;
import by.btslogistics.commons.service.exception.common.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MessagesLogCustomPartSaveServiceImpl implements MessagesLogCustomPartSaveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesLogCustomPartSaveServiceImpl.class);

    @Value("${messages.log.not.null}")
    private String paramNotNull;

    private final MessagesLogCustomPartRepository repository;

    private final MessagesLogCustomPartMapper mapper;

    @Autowired
    public MessagesLogCustomPartSaveServiceImpl(MessagesLogCustomPartRepository repository, MessagesLogCustomPartMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public MessagesLogCustomPartDto setToMessageLogCustomPart (MessagesLogCustomPartDto dto) {
        if (dto == null) throw new CommonException(paramNotNull);
        MessagesLogCustomPart entitySaved = repository.save(mapper.toEntity(dto));
        LOGGER.debug("Saved messagesLogCustomPart with id: {}", entitySaved.getId());
        return mapper.toDto(entitySaved);
    }
}
