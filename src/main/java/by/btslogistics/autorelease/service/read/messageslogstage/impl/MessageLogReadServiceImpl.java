package by.btslogistics.autorelease.service.read.messageslogstage.impl;

import by.btslogistics.autorelease.dao.model.messageslogstage.MessagesLogStage;
import by.btslogistics.autorelease.dao.repository.messagelogstage.MessagesLogStageRepository;
import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import by.btslogistics.autorelease.service.dto.messageslogstage.MessagesLogStageDto;
import by.btslogistics.autorelease.service.mapper.messagelogstage.MessagesLogStageMapper;
import by.btslogistics.autorelease.service.mapper.messageslog.MessagesLogMapper;
import by.btslogistics.autorelease.service.read.messageslogstage.MessagesLogStageReadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageLogReadServiceImpl implements MessagesLogStageReadService {

   private static final Logger LOGGER = LoggerFactory.getLogger(MessageLogReadServiceImpl.class);


    private MessagesLogStageMapper messagesLogStageMapper;

    private MessagesLogMapper messagesLogMapper;

    private MessagesLogStageRepository messagesLogStageRepository;

    @Autowired
    public MessageLogReadServiceImpl(MessagesLogStageMapper messagesLogStageMapper, MessagesLogMapper messagesLogMapper, MessagesLogStageRepository messagesLogStageRepository) {
        this.messagesLogStageMapper = messagesLogStageMapper;
        this.messagesLogMapper = messagesLogMapper;
        this.messagesLogStageRepository = messagesLogStageRepository;
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<MessagesLogStageDto> getStagesByMessageLogId(MessageLogDto messageLogDto) {
        LOGGER.debug("Get all Stages for MessageLog with id: {}", messageLogDto.getId());
        List<MessagesLogStage> messagesLogStageList = messagesLogStageRepository.findAllByMessageLog(messagesLogMapper.toEntity(messageLogDto));
        LOGGER.debug("Count of Stages for MessagesLog: {}", messagesLogStageList.size());
        return (List<MessagesLogStageDto>)messagesLogStageMapper.toListDto(messagesLogStageList);





    }
}
