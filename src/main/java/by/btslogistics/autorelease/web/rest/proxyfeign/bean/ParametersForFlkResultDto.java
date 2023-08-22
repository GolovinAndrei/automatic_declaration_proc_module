package by.btslogistics.autorelease.web.rest.proxyfeign.bean;

import java.time.LocalDateTime;
import java.util.Objects;

public class ParametersForFlkResultDto {

    private String idDeclaration;
    private LocalDateTime dateStart;
    private String typeDocument;

    public ParametersForFlkResultDto() {
    }

    public String getIdDeclaration() {
        return idDeclaration;
    }

    public void setIdDeclaration(String idDeclaration) {
        this.idDeclaration = idDeclaration;
    }

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDateTime dateStart) {
        this.dateStart = dateStart;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParametersForFlkResultDto that = (ParametersForFlkResultDto) o;
        return Objects.equals(idDeclaration, that.idDeclaration) &&
                Objects.equals(dateStart, that.dateStart) &&
                Objects.equals(typeDocument, that.typeDocument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDeclaration, dateStart, typeDocument);
    }

    @Override
    public String toString() {
        return "ParametrsForFlkResultDto{" +
                "idDeclaration='" + idDeclaration + '\'' +
                ", dateStart=" + dateStart +
                ", typeDocument='" + typeDocument + '\'' +
                '}';
    }
}
