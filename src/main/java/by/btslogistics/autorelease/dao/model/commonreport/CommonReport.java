package by.btslogistics.autorelease.dao.model.commonreport;

import by.btslogistics.commons.dao.model.flk.commonreport.CommonReportDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table (schema = "TTS_FORMAT_LOGICAL_CONTROL", name = "COMMON_REPORT")
public class CommonReport extends CommonReportDefault {

    /**
     * Идентификатор записи декларации (в таблице MESSAGESLOG), в отношении которой проводился контроль.
     */
    @Column(name = "TO_MESSAGESLOG_ID")
    private String toMessageLogId;

    /**
     * Ссылка на идентификатор таблицы MESSAGESLOG_STAGE
     */
    @Column(name = "TO_MESS_LOG_STAGE_ID")
    private Long toMessageLogStageId;

    public CommonReport() {
    }

    public String getToMessageLogId() {
        return toMessageLogId;
    }

    public void setToMessageLogId(String toMessageLogId) {
        this.toMessageLogId = toMessageLogId;
    }

    public Long getToMessageLogStageId() {
        return toMessageLogStageId;
    }

    public void setToMessageLogStageId(Long toMessageLogStageId) {
        this.toMessageLogStageId = toMessageLogStageId;
    }
}


