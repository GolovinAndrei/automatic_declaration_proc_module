package by.btslogistics.autorelease.service.save.messageslogstage.impl;

import by.btslogistics.autorelease.dao.model.messageslogstage.MessagesLogStage;
import by.btslogistics.autorelease.dao.repository.messagelogstage.MessagesLogStageRepository;
import by.btslogistics.autorelease.service.dto.messageslogstage.MessagesLogStageDto;
import by.btslogistics.autorelease.service.mapper.messagelogstage.MessagesLogStageMapper;
import by.btslogistics.autorelease.service.save.messageslogstage.MessagesLogStageSaveService;
import by.btslogistics.commons.dao.enums.Stage;
import by.btslogistics.commons.service.exception.common.CommonException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@PropertySources({
        @PropertySource("classpath:properties/app-exception-messages.properties")
})

@Service
public class MessagesLogStageSaveServiceImpl implements MessagesLogStageSaveService {

    @Value("${messages.log.not.null}")
    private String paramNotNull;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesLogStageSaveServiceImpl.class);

    private final MessagesLogStageRepository repository;

    private final MessagesLogStageMapper mapper;


    @Autowired
    public MessagesLogStageSaveServiceImpl(MessagesLogStageRepository repository, MessagesLogStageMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public MessagesLogStageDto addStageRec(Stage stage, String messageLogId) {
        if (stage == null || messageLogId == null) throw new CommonException(paramNotNull);

        LOGGER.debug("addStageRec(Stage stage : {}, String messageLogId) : {}", stage, messageLogId);

        MessagesLogStageDto messagesLogStageDto = new MessagesLogStageDto();

        messagesLogStageDto.setYearReg(LocalDateTime.now().getYear());
        messagesLogStageDto.setDateStage(LocalDateTime.now());
        messagesLogStageDto.setStageDoc(stage.getStatus());
        messagesLogStageDto.setDescriptionDoc(stage.getDescription());
        messagesLogStageDto.setMessageLogId(messageLogId);

        MessagesLogStage messageLogStageFromDb = repository.save(mapper.toEntity(messagesLogStageDto));

        LOGGER.debug("messageLogStageFromDb : {}", messageLogStageFromDb);

        return mapper.toDto(messageLogStageFromDb);
    }
}
