package by.btslogistics.autorelease.dao.model.messageslog;

import by.btslogistics.autorelease.dao.model.externalusers.ExternalUsers;
import by.btslogistics.commons.dao.model.messageslog.MessageLogDefault;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "MESSAGESLOG")
public class MessageLog extends MessageLogDefault {

    /**
     * EXTERNAL_USERS
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "EXTERNAL_USERS_ID", referencedColumnName = "ID") //insertable = false, updatable = false)
    private ExternalUsers externalUsers;

    public MessageLog() {
        super();
    }


    public ExternalUsers getExternalUsers() {
        return externalUsers;
    }

    public void setExternalUsers(ExternalUsers externalUsers) {
        this.externalUsers = externalUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MessageLog that = (MessageLog) o;
        return Objects.equals(externalUsers, that.externalUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), externalUsers);
    }
}
