package by.btslogistics.autorelease.dao.repository.messlogcustprtuploadstatus;

import by.btslogistics.autorelease.dao.model.messlogcustprtuploadstatus.MessagesLogCustomPartUploadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesLogCustomPartUploadStatusRepository extends JpaRepository<MessagesLogCustomPartUploadStatus, Long> {
}