package by.btslogistics.autorelease.service.dto.messageslogcustompart;

import by.btslogistics.commons.dao.dto.messageslogcustompart.MessagesLogCustomPartDefaultDto;


public class MessagesLogCustomPartDto extends MessagesLogCustomPartDefaultDto {

    private String toMessagesLogId;

    public String getToMessagesLog() {
        return toMessagesLogId;
    }

    public void setToMessagesLog(String toMessagesLog) {
        this.toMessagesLogId = toMessagesLog;
    }
}
