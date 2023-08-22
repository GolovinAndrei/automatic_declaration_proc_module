package by.btslogistics.autorelease.service.dto.prohibitrestrictreport;


public class ProhibitRestrictReportDto {

    private Long id;

    private String toMessagesLogId;

    private Integer intermediateResult;

    private String nsiCatalogId;

    private Integer codeResult;

    private String codeTnVed;

    private Integer goodsNumeric;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToMessagesLogId() {
        return toMessagesLogId;
    }

    public void setToMessagesLogId(String toMessagesLogId) {
        this.toMessagesLogId = toMessagesLogId;
    }

    public Integer getIntermediateResult() {
        return intermediateResult;
    }

    public void setIntermediateResult(Integer intermediateResult) {
        this.intermediateResult = intermediateResult;
    }

    public String getNsiCatalogId() {
        return nsiCatalogId;
    }

    public void setNsiCatalogId(String nsiCatalogId) {
        this.nsiCatalogId = nsiCatalogId;
    }

    public Integer getCodeResult() {
        return codeResult;
    }

    public void setCodeResult(Integer codeResult) {
        this.codeResult = codeResult;
    }

    public String getCodeTnVed() {
        return codeTnVed;
    }

    public void setCodeTnVed(String codeTnVed) {
        this.codeTnVed = codeTnVed;
    }

    public Integer getGoodsNumeric() {
        return goodsNumeric;
    }

    public void setGoodsNumeric(Integer goodsNumeric) {
        this.goodsNumeric = goodsNumeric;
    }
}
