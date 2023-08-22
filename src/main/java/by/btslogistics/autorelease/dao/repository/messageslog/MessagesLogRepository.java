package by.btslogistics.autorelease.dao.repository.messageslog;

import by.btslogistics.autorelease.dao.model.messageslog.MessageLog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesLogRepository extends CrudRepository<MessageLog, String>, JpaSpecificationExecutor<MessageLog> {

}
