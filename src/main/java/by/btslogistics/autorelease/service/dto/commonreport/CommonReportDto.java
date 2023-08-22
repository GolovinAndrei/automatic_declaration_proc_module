package by.btslogistics.autorelease.service.dto.commonreport;

import by.btslogistics.commons.dao.dto.flk.commonreport.CommonReportDefaultDto;

public class CommonReportDto extends CommonReportDefaultDto {


    private String toMessageLogId;

    private Long toMessageLogStageId;

    public CommonReportDto() {
    }

    public String getToMessageLogId() {
        return toMessageLogId;
    }

    public void setToMessageLogId(String toMessageLogId) {
        this.toMessageLogId = toMessageLogId;
    }

    public Long getToMessageLogStageId() {
        return toMessageLogStageId;
    }

    public void setToMessageLogStageId(Long toMessageLogStageId) {
        this.toMessageLogStageId = toMessageLogStageId;
    }
}
