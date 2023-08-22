package by.btslogistics.autorelease.service.save.messagesout;

import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import by.btslogistics.autorelease.service.dto.messagesout.MessageOutDto;

/**
 * бизнес-логика для таблицы MessagesOut
 */
public interface MessagesOutSaveService {

    /**
     *  Сохранение записи в таблицу  MessagesOut
     * @param dto - объект для сохранения
     * @return - возвращается объект, который был сохранен
     */
    MessageOutDto setToMessagesOut(MessageOutDto dto);

    MessageOutDto setToMessagesOut(MessageLogDto messageLogDto, String messageType, String state, String xmlMsg);

    MessageOutDto setToMessagesOut(MessageLogDto messageLogDto, String messageType, String state, String msgText, String xmlMsg);

    MessageOutDto setToMessagesOut(String typeDoc, String msgCode, String msgText,
                                   String docId, String messOutState, String messageLogId, String xmlMsg);

}
