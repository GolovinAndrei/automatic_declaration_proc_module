package by.btslogistics.autorelease.dao.repository.quota;

import by.btslogistics.autorelease.dao.model.quota.QuotaReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotaReportRepository extends CrudRepository<QuotaReport, Long> {

    List<QuotaReport> findByMessageLogId(String messageLogId);
}
