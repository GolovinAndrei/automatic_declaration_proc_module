package by.btslogistics.autorelease.service.save.commonreport;


import by.btslogistics.autorelease.service.dto.commonreport.CommonReportDto;


public interface CommonReportSaveService {

    CommonReportDto addNewReport(String numberGrafa, String codeError, String description);

    void setDocumentId(String documentId);
}
