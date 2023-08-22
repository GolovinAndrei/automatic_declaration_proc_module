package by.btslogistics.autorelease.dao.model.ois;

import by.btslogistics.commons.dao.model.IdMainForEntities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "OIS_CONTROL_REPORT", schema = "TTS_FORMAT_LOGICAL_CONTROL")
public class OisReport extends IdMainForEntities {

    @Column(name = "TO_MESSAGESLOG_ID")
    private String dtId;

    @Column(name = "GOODS_NUMERIC")
    private Integer goodsNumber;

    @Column(name = "GOODS_TN_VED_CODE")
    private String tnVed;

    @Column(name = "OIS_ID_RESH")
    private Integer idResh;

    @Column(name = "OIS_YEAR_RESH")
    private Short yearResh;

    @Column(name = "OIS_TYPE_RESH")
    private Short typeResh;

    @Column(name = "REPORT_RESULT_CODE")
    private int resultCode;

    public String getDtId() {
        return dtId;
    }

    public void setDtId(String dtId) {
        this.dtId = dtId;
    }

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getTnVed() {
        return tnVed;
    }

    public void setTnVed(String tnVed) {
        this.tnVed = tnVed;
    }

    public Integer getIdResh() {
        return idResh;
    }

    public void setIdResh(Integer idResh) {
        this.idResh = idResh;
    }

    public Short getYearResh() {
        return yearResh;
    }

    public void setYearResh(Short yearResh) {
        this.yearResh = yearResh;
    }

    public Short getTypeResh() {
        return typeResh;
    }

    public void setTypeResh(Short typeResh) {
        this.typeResh = typeResh;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OisReport oisReport = (OisReport) o;
        return Objects.equals(dtId, oisReport.dtId) &&
                Objects.equals(goodsNumber, oisReport.goodsNumber) &&
                Objects.equals(tnVed, oisReport.tnVed) &&
                Objects.equals(idResh, oisReport.idResh) &&
                Objects.equals(yearResh, oisReport.yearResh) &&
                Objects.equals(typeResh, oisReport.typeResh) &&
                Objects.equals(resultCode, oisReport.resultCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dtId, goodsNumber, tnVed, idResh, yearResh, typeResh, resultCode);
    }
}
