package by.btslogistics.autorelease.dao.repository.ois;

import by.btslogistics.autorelease.dao.model.ois.OisReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OisReportRepository extends CrudRepository<OisReport, String> {

    List<OisReport> findAllByDtIdAndGoodsNumber(String dtId, Integer goodsNumber);

}
