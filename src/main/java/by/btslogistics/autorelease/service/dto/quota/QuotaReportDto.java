package by.btslogistics.autorelease.service.dto.quota;




public class QuotaReportDto {

    private Long id;

    private String messageLogId;

    private int resultCode;

    private Integer goodsNumeric;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageLogId() {
        return messageLogId;
    }

    public void setMessageLogId(String messageLogId) {
        this.messageLogId = messageLogId;
    }

    public int getResultCode() {
        return resultCode;
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
