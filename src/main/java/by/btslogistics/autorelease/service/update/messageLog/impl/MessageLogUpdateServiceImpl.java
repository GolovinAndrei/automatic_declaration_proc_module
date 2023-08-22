package by.btslogistics.autorelease.service.update.messageLog.impl;

import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import by.btslogistics.autorelease.service.save.messageslog.MessagesLogSaveService;
import by.btslogistics.autorelease.service.update.messageLog.MessageLogUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class MessageLogUpdateServiceImpl implements MessageLogUpdateService {

    private final MessagesLogSaveService saveService;

    @Autowired
    public MessageLogUpdateServiceImpl(MessagesLogSaveService saveService) {
        this.saveService = saveService;
    }

    @Override
    public MessageLogDto updateMessageLogByStatusDoc(MessageLogDto messageLog, String statusDoc) {
        if (messageLog != null) {
            if (statusDoc != null) {
                messageLog.setStatusDoc(statusDoc);
                messageLog.setStatusDate(LocalDateTime.now());
            }
            return saveService.setToMessageLog(messageLog);
        }
        return null;
    }


    @Override
    public MessageLogDto updateMessageLog (MessageLogDto messageLog) {
        if (messageLog != null) {
            return saveService.setToMessageLog(messageLog);
        }
        return null;
    }
}
