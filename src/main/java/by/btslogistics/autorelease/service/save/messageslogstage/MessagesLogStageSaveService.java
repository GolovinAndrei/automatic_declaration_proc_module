package by.btslogistics.autorelease.service.save.messageslogstage;

import by.btslogistics.autorelease.service.dto.messageslogstage.MessagesLogStageDto;
import by.btslogistics.commons.dao.enums.Stage;


public interface MessagesLogStageSaveService {

   MessagesLogStageDto addStageRec (Stage stage, String messageLogId);
}
