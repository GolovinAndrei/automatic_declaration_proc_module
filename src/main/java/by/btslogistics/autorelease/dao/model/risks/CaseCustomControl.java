package by.btslogistics.autorelease.dao.model.risks;


import by.btslogistics.commons.dao.model.IdMainForEntities;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(schema = "TTS_RISK_MANAGEMENT", name = "CASE_CUSTOM_CONTROL")
public class CaseCustomControl extends IdMainForEntities {

    @Column (name = "RISK_OBJECT_NUMBER")
    private String dtRegNumber;

    @Column (name = "STATE")
    private String state;

    @Column (name = "CONTROL_LEVEL")
    private String level;


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
