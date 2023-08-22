package by.btslogistics.autorelease.service.dto.paymentreport;


import java.time.LocalDateTime;

public class PaymentReportDto {

    private String id;
    private String idDeclaration;
    private String numberGrafa;
    private Long goodsnumeric;
    private String nameError;
    private String descriptionError;
    private LocalDateTime creationDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdDeclaration() {
        return idDeclaration;
    }

    public void setIdDeclaration(String idDeclaration) {
        this.idDeclaration = idDeclaration;
    }

    public String getNumberGrafa() {
        return numberGrafa;
    }

    public void setNumberGrafa(String numberGrafa) {
        this.numberGrafa = numberGrafa;
    }

    public Long getGoodsnumeric() {
        return goodsnumeric;
    }

    public void setGoodsnumeric(Long goodsnumeric) {
        this.goodsnumeric = goodsnumeric;
    }

    public String getNameError() {
        return nameError;
    }

    public void setNameError(String nameError) {
        this.nameError = nameError;
    }

    public String getDescriptionError() {
        return descriptionError;
    }

    public void setDescriptionError(String descriptionError) {
        this.descriptionError = descriptionError;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
