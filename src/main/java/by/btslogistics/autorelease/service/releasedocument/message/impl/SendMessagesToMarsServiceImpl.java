package by.btslogistics.autorelease.service.releasedocument.message.impl;

import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import by.btslogistics.autorelease.service.releasedocument.message.SendMessagesToMarsService;
import by.btslogistics.autorelease.service.save.messagesout.MessagesOutSaveService;
import by.btslogistics.commons.dao.enums.typeobjects.XMLMessageTypes;
import by.btslogistics.commons.service.soap.client.WSClientService;
import by.btslogistics.commons.service.soap.client.impl.WSClientServiceImpl;
import by.btslogistics.commons.service.soap.client.message.CSTM0007Message;
import by.btslogistics.commons.service.soap.client.message.CSTM0008Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SendMessagesToMarsServiceImpl implements SendMessagesToMarsService {

    private static final Logger LOGGER  = LoggerFactory.getLogger( SendMessagesToMarsService.class );

    private final MessagesOutSaveService service;

    @Autowired
    public SendMessagesToMarsServiceImpl(MessagesOutSaveService service) {
        this.service = service;
    }

    @Override
    public void sendCSTM0007Message(MessageLogDto messageLogDto) {
        try {
            final CSTM0007Message msg = new CSTM0007Message(messageLogDto);
            WSClientService wsClientService = new WSClientServiceImpl();
            service.setToMessagesOut(messageLogDto, XMLMessageTypes.MessageType.CSTM0007.code, wsClientService.sendToMars(msg)?"1":"0", msg.toXMLString());
        } catch (Exception e) {
            LOGGER.error("Error CSTM0007Message: " + e);

        }
    }

    @Override
    public void sendCSTM0008Message(MessageLogDto messageLogDto, String reason) {
        try {
            final CSTM0008Message msg = new CSTM0008Message(messageLogDto, reason);
            WSClientService wsClientService = new WSClientServiceImpl();
            service.setToMessagesOut(messageLogDto, XMLMessageTypes.MessageType.CSTM0008.code, wsClientService.sendToMars(msg)?"1":"0", msg.toXMLString());
        } catch (Exception e) {
            LOGGER.error("Error CSTM0008Message: " + e);
        }
    }



}
