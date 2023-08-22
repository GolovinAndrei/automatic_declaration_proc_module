package by.btslogistics.autorelease.dao.repository.customs;

import by.btslogistics.autorelease.dao.model.customs.CustomsOperatIni;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface CustomsOperatIniRepository extends CrudRepository<CustomsOperatIni, String> {

    @Query("select c from CustomsOperatIni c where c.documType = :doc and c.dOn <= :date and c.dOff>=:date and c.custOper = :operation")
    CustomsOperatIni getCustomsOperatIniEntitiesByDocumTypeAndDate(@Param("doc") String documType, @Param("date") LocalDateTime date, @Param("operation") String operation);
}
