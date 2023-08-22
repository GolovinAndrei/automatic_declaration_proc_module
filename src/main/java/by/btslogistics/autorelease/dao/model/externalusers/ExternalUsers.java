package by.btslogistics.autorelease.dao.model.externalusers;

import by.btslogistics.commons.dao.model.externalusers.ExternalUsersDefault;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXTERNAL_USERS", schema = "TTS_RECEIVE")
public class ExternalUsers extends ExternalUsersDefault {
}
