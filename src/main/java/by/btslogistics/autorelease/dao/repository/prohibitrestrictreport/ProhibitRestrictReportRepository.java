package by.btslogistics.autorelease.dao.repository.prohibitrestrictreport;

import by.btslogistics.autorelease.dao.model.prohibitrestrictreport.ProhibitRestrictReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProhibitRestrictReportRepository extends CrudRepository<ProhibitRestrictReport,Long> {

    List<ProhibitRestrictReport> getAllByToMessagesLogId (String toMessagesLogId);
}
