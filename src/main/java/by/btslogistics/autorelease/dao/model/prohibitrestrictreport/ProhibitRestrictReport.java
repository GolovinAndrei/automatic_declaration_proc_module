package by.btslogistics.autorelease.dao.model.prohibitrestrictreport;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "ZIO_PROHIBIT_RESTRICT_REPORT", schema = "TTS_FORMAT_LOGICAL_CONTROL")
public class ProhibitRestrictReport {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prohibitRestrictGenerator")
    @SequenceGenerator(name = "prohibitRestrictGenerator", sequenceName = "TTS_FORMAT_LOGICAL_CONTROL.SEQ_PROHIBIT_RESTRICT_REPORT", allocationSize = 1)
    private Long id;

    @Column(name = "TO_MESSAGESLOG_ID")
    private String toMessagesLogId;

    @Column(name = "INTERMEDIATE_RESULT")
    private Integer intermediateResult;

    @Column(name = "NSI_CATALOG_ID")
    private String nsiCatalogId;

    @Column(name = "CODE_RESULT")
    private Integer codeResult;

    @Column(name = "GOODSTNVEDCODE")
    private String codeTnVed;

    @Column(name = "GOODSNUMERIC")
    private Integer goodsNumeric;

    public ProhibitRestrictReport() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProhibitRestrictReport that = (ProhibitRestrictReport) o;
        return Objects.equals(id, that.id) && Objects.equals(toMessagesLogId, that.toMessagesLogId) && Objects.equals(intermediateResult, that.intermediateResult) && Objects.equals(nsiCatalogId, that.nsiCatalogId) && Objects.equals(codeResult, that.codeResult) && Objects.equals(codeTnVed, that.codeTnVed) && Objects.equals(goodsNumeric, that.goodsNumeric);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, toMessagesLogId, intermediateResult, nsiCatalogId, codeResult, codeTnVed, goodsNumeric);
    }
}
