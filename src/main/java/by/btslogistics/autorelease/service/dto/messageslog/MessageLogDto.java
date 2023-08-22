package by.btslogistics.autorelease.service.dto.messageslog;

import by.btslogistics.commons.dao.dto.messageslog.MessageLogDefaultDto;
import by.btslogistics.autorelease.service.dto.externalusers.ExternalUsersDto;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.Objects;

@PropertySources({
        @PropertySource("classpath:properties/app-exception-messages.properties")
})
public class MessageLogDto extends MessageLogDefaultDto {

    /**
     * EXTERNAL_USERS
     */
    private ExternalUsersDto externalUsers;

    private String msgXml;

    public String getMsgXml() {
        return msgXml;
    }

    public void setMsgXml(String msgXml) {
        this.msgXml = msgXml;
    }

    public ExternalUsersDto getExternalUsers() {
        return externalUsers;
    }

    public void setExternalUsers(ExternalUsersDto externalUsers) {
        this.externalUsers = externalUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MessageLogDto that = (MessageLogDto) o;
        return Objects.equals(externalUsers, that.externalUsers) &&
                Objects.equals(msgXml, that.msgXml);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), externalUsers, msgXml);
    }
}
