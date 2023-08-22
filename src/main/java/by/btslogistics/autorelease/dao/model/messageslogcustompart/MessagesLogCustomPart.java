package by.btslogistics.autorelease.dao.model.messageslogcustompart;

import by.btslogistics.commons.dao.model.messageslogcustompart.MessagesLogCustomPartDefault;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "MESSAGESLOG_CUSTOM_PART", schema = "TTS_RECEIVE")
@EqualsAndHashCode(callSuper = false)
public class MessagesLogCustomPart extends MessagesLogCustomPartDefault {

    @Column(name = "TO_MESSAGESLOG_ID")
    private String toMessagesLogId;

    public String getToMessagesLog() {
        return toMessagesLogId;
    }

    public void setToMessagesLog(String toMessagesLogId) {
        this.toMessagesLogId = toMessagesLogId;
    }
}
