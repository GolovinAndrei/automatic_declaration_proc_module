package by.btslogistics.autorelease.service.save.messagesout.impl;

import by.btslogistics.autorelease.dao.model.messagesout.MessageOut;
import by.btslogistics.autorelease.dao.repository.messagesout.MessagesOutRepository;
import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import by.btslogistics.autorelease.service.dto.messagesout.MessageOutDto;
import by.btslogistics.autorelease.service.mapper.messagesout.MessagesOutMapper;
import by.btslogistics.autorelease.service.save.messagesout.MessagesOutSaveService;
import by.btslogistics.commons.service.exception.common.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class MessagesOutSaveServiceImpl implements MessagesOutSaveService {

    @Value("${messages.log.not.null}")
    private String paramNotNull;

    private final MessagesOutRepository repository;

    private final MessagesOutMapper mapper;

    @Autowired
    public MessagesOutSaveServiceImpl(MessagesOutRepository repository,
                                      MessagesOutMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Transactional
    @Override
    public MessageOutDto setToMessagesOut(MessageOutDto dto) {

        if(dto == null) throw new CommonException(paramNotNull);

        MessageOut messageOut = mapper.toEntity(dto);

        MessageOut save = this.repository.save(messageOut);

        return mapper.toDto(save);
    }

    @Transactional
    @Override
    public MessageOutDto setToMessagesOut(MessageLogDto messageLogDto, String messageType, String state, String xmlMsg) {
        return setToMessagesOut(messageLogDto, messageType, state,null, xmlMsg);
    }

    @Transactional
    @Override
    public MessageOutDto setToMessagesOut(MessageLogDto messageLogDto, String messageType, String state, String msgText, String xmlMsg) {

        if(messageLogDto.getYearReg() == null || messageLogDto.getTypeDoc() == null) throw new CommonException(paramNotNull);

        MessageOutDto dto = new MessageOutDto();
        dto.setMsgCode(messageType);
        dto.setDateSend(LocalDateTime.now());
        dto.setYearReg(messageLogDto.getYearReg() );
        dto.setTypeDoc(messageLogDto.getTypeDoc());
        dto.setMsgText(msgText);
        dto.setMessOutState(state);
        dto.setMessageLogId(messageLogDto.getId());
        dto.setMsgXml(xmlMsg);

        MessageOut messageOut = mapper.toEntity(dto);

        MessageOut save = this.repository.save(messageOut);

        return mapper.toDto(save);
    }

    @Transactional
    @Override
    public MessageOutDto setToMessagesOut(String typeDoc, String msgCode, String msgText,
                                          String docId, String messOutState, String messageLogId, String xmlMsg) {

        MessageOutDto dto = new MessageOutDto();
        dto.setDateSend(LocalDateTime.now());
        dto.setYearReg(dto.getDateSend().getYear());
        dto.setTypeDoc(typeDoc);
        dto.setMsgCode(msgCode);
        dto.setMsgText(msgText);
        dto.setDocId(docId);
        dto.setMessOutState(messOutState);
        dto.setMessageLogId(messageLogId);
        dto.setMsgXml(xmlMsg);

        MessageOut messageOut = mapper.toEntity(dto);

        MessageOut save = this.repository.save(messageOut);

        return mapper.toDto(save);
    }

}
