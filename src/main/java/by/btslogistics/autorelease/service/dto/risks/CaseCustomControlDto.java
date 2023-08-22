package by.btslogistics.autorelease.service.dto.risks;


import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
public class CaseCustomControlDto {

    private String id;

    private String dtRegNumber;

    private String state;

    private String level;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDtRegNumber() {
        return dtRegNumber;
    }

    public void setDtRegNumber(String dtRegNumber) {
        this.dtRegNumber = dtRegNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
