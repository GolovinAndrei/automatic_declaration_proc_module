package by.btslogistics.autorelease.dao.repository.commonreport;

import by.btslogistics.autorelease.dao.model.commonreport.CommonReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonReportRepository extends CrudRepository<CommonReport, String> {

    List<CommonReport> findAllByToMessageLogId (String toMessageLogId);
}
