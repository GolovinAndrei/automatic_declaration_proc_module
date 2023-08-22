package by.btslogistics.autorelease.service.read.messageslog;

import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;

/**
 * Обрабатываются запросы к таблице MESSAGESLOG
 */
public interface MessagesLogReadService {

    /**
     * Поиск по ID в таблице MESSAGESLOG
     * @param id  32-символьный (GUID) идентификатор записи в таблице MESSAGESLOG
     * @return DTO запись
     */
    MessageLogDto getById(String id);

}
