package by.btslogistics.autorelease.service.dto.ois;



public class OisReportDto {

    private String dtId;

    private Integer goodsNumber;

    private String tnVed;

    private Integer idResh;

    private Short yearResh;

    private Short typeResh;

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
}
