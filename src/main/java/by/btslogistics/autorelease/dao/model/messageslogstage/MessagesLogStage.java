package by.btslogistics.autorelease.dao.model.messageslogstage;

import by.btslogistics.autorelease.dao.model.messageslog.MessageLog;
import by.btslogistics.commons.dao.model.messageslogstage.MessagesLogStageDefault;


import javax.persistence.*;


@Entity
@Table (name = "MESSAGESLOG_STAGE")
public class MessagesLogStage extends MessagesLogStageDefault {

    @ManyToOne
    @JoinColumn (name = "TO_MESSAGESLOG_ID")
    private MessageLog messageLog;

    public MessageLog getMessageLog() {
        return messageLog;
    }

    public void setMessageLog(MessageLog messageLog) {
        this.messageLog = messageLog;
    }
}
