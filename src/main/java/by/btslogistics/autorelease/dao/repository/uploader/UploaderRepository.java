package by.btslogistics.autorelease.dao.repository.uploader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

@Slf4j
@Repository
public class UploaderRepository {

    private static final String UPLOADER_PACKAGE = "TTS_UPLOADER.PKG_SEND_XML_TO_ISESD.PUT_XML_IN_FOLDERS_BY_MES_ID";

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public String getXmlFromUploaderDbProcedure(String messageLogId) {

        log.debug("Start of stored procedure PUT_XML_IN_FOLDERS_BY_MES_ID from package for ID {}", messageLogId);
        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery(UPLOADER_PACKAGE);
        storedProcedureQuery.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter(3, Integer.class, ParameterMode.OUT);
        storedProcedureQuery.registerStoredProcedureParameter(4, String.class, ParameterMode.OUT);
        storedProcedureQuery.setParameter(1,messageLogId);
        storedProcedureQuery.execute();
        log.debug("Result of procedure: {}", storedProcedureQuery.getOutputParameterValue(3).toString());
        return (String) storedProcedureQuery.getOutputParameterValue(4);
    }
}
