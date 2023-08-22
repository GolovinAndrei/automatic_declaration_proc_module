package by.btslogistics.autorelease.dao.repository.risks;

import by.btslogistics.autorelease.dao.model.risks.CaseCustomControl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaseCustomControlRepository extends CrudRepository<CaseCustomControl, String> {

    Optional<CaseCustomControl> findByDtRegNumber(String dtRegNumber);

}
