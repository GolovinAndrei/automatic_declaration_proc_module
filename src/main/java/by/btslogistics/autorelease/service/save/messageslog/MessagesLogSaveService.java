package by.btslogistics.autorelease.service.save.messageslog;

import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;


public interface MessagesLogSaveService {

    /**
     *
     *   Сохранение записи в таблицу  MessagesLog
     *
     * @param dto - объект для сохранения
     * @return - возвращается объект, который был сохранен
     */
        MessageLogDto setToMessageLog(MessageLogDto dto);
}
