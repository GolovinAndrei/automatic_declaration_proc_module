package by.btslogistics.autorelease.dao.model.quota;


import javax.persistence.*;

@Entity
@Table(name = "QUOTA_CONTROL_REPORT", schema = "TTS_FORMAT_LOGICAL_CONTROL")
public class QuotaReport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jpaSequence.QuotaControlReport")
    @SequenceGenerator(name = "jpaSequence.QuotaControlReport", sequenceName = "TTS_FORMAT_LOGICAL_CONTROL.SEQ_QUOTA_CONTROL_REPORT", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TO_MESSAGESLOG_ID")
    private String messageLogId;
    @Column(name = "RESULT_CODE")
    private int resultCode;
    @Column(name = "GOODSNUMERIC")
    private Integer goodsNumeric;

    public Long getId() {
        return id;
    }

    public String getMessageLogId() {
        return messageLogId;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessageLogId(String messageLogId) {
        this.messageLogId = messageLogId;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Integer getGoodsNumeric() {
        return goodsNumeric;
    }

    public void setGoodsNumeric(Integer goodsNumeric) {
        this.goodsNumeric = goodsNumeric;
    }
}
