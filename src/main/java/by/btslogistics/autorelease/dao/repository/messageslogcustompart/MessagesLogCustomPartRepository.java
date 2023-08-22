package by.btslogistics.autorelease.dao.repository.messageslogcustompart;

import by.btslogistics.autorelease.dao.model.messageslogcustompart.MessagesLogCustomPart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessagesLogCustomPartRepository extends CrudRepository<MessagesLogCustomPart, String> {

    Optional<MessagesLogCustomPart> findByToMessagesLogId (String toMessagesLogId);
}
