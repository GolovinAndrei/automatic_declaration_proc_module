package by.btslogistics.autorelease.dao.repository.messagelogstage;

import by.btslogistics.autorelease.dao.model.messageslog.MessageLog;
import by.btslogistics.autorelease.dao.model.messageslogstage.MessagesLogStage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessagesLogStageRepository extends CrudRepository<MessagesLogStage, Long> {

List<MessagesLogStage> findAllByMessageLog (MessageLog messageLog);

}
