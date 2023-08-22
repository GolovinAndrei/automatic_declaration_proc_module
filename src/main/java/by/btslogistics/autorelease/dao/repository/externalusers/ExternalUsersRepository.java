package by.btslogistics.autorelease.dao.repository.externalusers;

import by.btslogistics.autorelease.dao.model.externalusers.ExternalUsers;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ExternalUsersRepository
        extends CrudRepository<ExternalUsers, String>, JpaSpecificationExecutor<ExternalUsers> {

    /**
     *  Поиск записи по полю CERT_SERIAL_NUMBER
     * @param certSerialNumber - Серийный номер
     * @return - возвращает список объектов
     */
    Iterable<ExternalUsers> findByCertSerialNumber(String certSerialNumber);

}
