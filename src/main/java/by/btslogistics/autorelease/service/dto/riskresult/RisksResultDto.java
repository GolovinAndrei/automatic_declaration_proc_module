package by.btslogistics.autorelease.service.dto.riskresult;



import java.util.List;


public class RisksResultDto {

    private String numPos;

    private Boolean result;

    private List<String> profiles;

    private String dtkNumber;

    public String getNumPos() {
        return numPos;
    }

    public void setNumPos(String numPos) {
        this.numPos = numPos;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public List<String> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<String> profiles) {
        this.profiles = profiles;
    }

    public String getDtkNumber() {
        return dtkNumber;
    }

    public void setDtkNumber(String dtkNumber) {
        this.dtkNumber = dtkNumber;
    }
}
