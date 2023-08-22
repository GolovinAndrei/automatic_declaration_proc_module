package by.btslogistics.autorelease.service.releasedocument.steps;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import by.btslogistics.autorelease.service.releasedocument.createregistrationnumber.CreateRegistrationNumberService;
import by.btslogistics.autorelease.service.releasedocument.message.SendMessagesToMarsService;
import by.btslogistics.autorelease.service.update.messageLog.MessageLogUpdateService;
import by.btslogistics.autorelease.service.update.messageslogcustompart.MessagesLogCustomPartUpdateService;
import by.btslogistics.autorelease.service.update.messlogcustpartuploadstatus.MessagesLogCustomPartUploadStatusSaveService;
import by.btslogistics.commons.dao.model.messageslog.StatusDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Автоматическое принятие решения об автовыпуске
 * <p>
 * 1 - расчет номера выпуска ДТ
 * 2 - запись  № в MESSAGESLOG.RELEASE
 * 3 - запись в MESSAGESLOG_CUSTOM_PART
 * 4 - отправка CSTM0007
 */
public class AutoReleaseStep extends RegistrationDocument {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoReleaseStep.class);

    private CreateRegistrationNumberService createRegistrationNumberService;

    private SendMessagesToMarsService sendMessagesToMarsService;

    private MessageLogUpdateService updateService;

    private MessagesLogCustomPartUpdateService messagesLogCustomPartUpdateService;

    private MessagesLogCustomPartUploadStatusSaveService messagesLogCustomPartUploadStatusSaveService;
    private static final String LNP_FOR_AUTORELEASE = "AUTO";

    public AutoReleaseStep(CreateRegistrationNumberService createRegistrationNumberService,
                           SendMessagesToMarsService sendMessagesToMarsService,
                           MessageLogUpdateService updateService,
                           MessagesLogCustomPartUpdateService messagesLogCustomPartUpdateService,
                           MessagesLogCustomPartUploadStatusSaveService messagesLogCustomPartUploadStatusSaveService) {
        this.createRegistrationNumberService = createRegistrationNumberService;
        this.sendMessagesToMarsService = sendMessagesToMarsService;
        this.updateService = updateService;
        this.messagesLogCustomPartUpdateService = messagesLogCustomPartUpdateService;
        this.messagesLogCustomPartUploadStatusSaveService = messagesLogCustomPartUploadStatusSaveService;
    }

    @Override
    public RegistrationDto doStep(RegistrationDto dto) {
        String code = "";
        if (dto.getGoodsDeclarationTypeNew() != null) {
            if (dto.getGoodsDeclarationTypeNew() != null) {
                String codeValue = dto.getGoodsDeclarationTypeNew().getDeclarationKindCode();
                if ("ЭК".equals(codeValue))
                    code = "1";
                if ("ИМ".equals(codeValue))
                    code = "2";
            }
        } else {
            if (dto.getGoodsDeclarationTypeOld() != null) {
                String codeValue = dto.getGoodsDeclarationTypeOld().getDeclarationKindCode();
                if ("ЭК".equals(codeValue))
                    code = "1";
                if ("ИМ".equals(codeValue))
                    code = "2";
            }
        }
        MessageLogDto messageLogDto = dto.getMessageLog();
        messageLogDto.setRelease(createRegistrationNumberService.getRegistrationNumberShort(dto.getMessageLog(), code));
        messageLogDto.setDateRelease(LocalDateTime.now());
        messageLogDto.setStatusDoc(StatusDoc.REGISTERED_WITH_POSITIVE.getCode());
        dto.setMessageLog(updateService.updateMessageLog(messageLogDto));
        LOGGER.debug("MessagesLog has been updated with release number ( {} ) and status ( {} )", messageLogDto.getRelease(), messageLogDto.getStatusDoc());
        messagesLogCustomPartUpdateService.setLNPAfterAutoReleaseByMessagesLogId(dto.getMessageLog(), LNP_FOR_AUTORELEASE);
        messagesLogCustomPartUploadStatusSaveService.createAndSave(messageLogDto);
        sendMessagesToMarsService.sendCSTM0007Message(dto.getMessageLog());

        return nextStep(dto);
    }
}
