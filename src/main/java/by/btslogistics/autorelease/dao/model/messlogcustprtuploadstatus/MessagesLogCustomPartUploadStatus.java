package by.btslogistics.autorelease.dao.model.messlogcustprtuploadstatus;

import by.btslogistics.commons.dao.model.messlogcustprtuploadstatus.MessagesLogCustomPartUploadStatusDefault;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@Builder
@Entity
@Table(name = "MESSLOG_CUST_PRT_UPLOAD_STATUS", schema = "TTS_RECEIVE")
public class MessagesLogCustomPartUploadStatus extends MessagesLogCustomPartUploadStatusDefault {
}
