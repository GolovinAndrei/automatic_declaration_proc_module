package by.btslogistics.autorelease.service.update.messlogcustpartuploadstatus;

import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;

public interface MessagesLogCustomPartUploadStatusSaveService {
    void createAndSave(MessageLogDto messageLogDto);
}
