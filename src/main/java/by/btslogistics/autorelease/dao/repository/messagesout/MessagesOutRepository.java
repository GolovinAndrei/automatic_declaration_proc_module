package by.btslogistics.autorelease.dao.repository.messagesout;

import by.btslogistics.autorelease.dao.model.messagesout.MessageOut;
import org.springframework.data.repository.CrudRepository;

public interface MessagesOutRepository extends CrudRepository<MessageOut, String> {

    /**
     *  Поиск в таблице MessagesOut записи по полю MESSAGELOG_ID
     * @param messageLogId - строковое значение идентификатора
     * @return - возвращает объект
     */
    Iterable <MessageOut> findByMessageLogId(String messageLogId);

    /**
     * поиск записи по полю MESSOUT_STATE
     * @param messOutState - Статусное состояние сообщения
     * @return - возвращает объект
     */
    Iterable <MessageOut> findByMessOutState(String messOutState);

    /**
     * Код исходящего сообщения (TECH.0001, TECH.0002, CSTM.0001 и др.) MSG_CODE
     * @param msgCode -Код статуса ЭКД по результатам его обработки модулями АПС ТТС
     * @return - список записей
     */
    Iterable<MessageOut> findByMsgCode(String msgCode);

    /**
     * Код электронного документа или ЭКД (SD, DT и т.д. согласно внутреннего списка BCM_01 TYPE_DOC
     * @param type -Тип документа
     * @return - список записей
     */
    Iterable<MessageOut> findByTypeDoc(String type);
}
