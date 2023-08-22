package by.btslogistics.autorelease.web.rest.autoreleaserest;

import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import by.btslogistics.autorelease.service.read.messageslog.MessagesLogReadService;
import by.btslogistics.autorelease.service.releasedocument.autorelease.AutoRelease;
import by.btslogistics.autorelease.web.constant.AutoReleaseConstants;
import by.btslogistics.commons.service.exception.common.CommonException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static by.btslogistics.autorelease.web.constant.AutoReleaseConstants.RECEIVE_DOC;

/**
 * Запуск автовыпуска
 */
@Tag(description = "Контроллер запуска автоматического выпуска", name = "Receive Dt")
@RestController
@RequestMapping(AutoReleaseConstants.ROOT_AUTO_RELEASE)
public class AutoReleaseRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoReleaseRest.class);

    private final AutoRelease autoRelease;
    private final MessagesLogReadService messagesLogReadService;

    @Autowired
    public AutoReleaseRest(AutoRelease autoRelease, MessagesLogReadService messagesLogReadService) {
        this.autoRelease = autoRelease;
        this.messagesLogReadService = messagesLogReadService;
    }

    /**
     * Запуск цепочки автовыпуска по id документа
     *
     * @param messageLogId - id документа
     * @return ok
     */
    @Operation(summary = "Передача документа в модуль для запуска процедуры его автоматического выпуска")
    @PostMapping(RECEIVE_DOC)
    public ResponseEntity<Object> checkAutoRelease(
            @Parameter(description = "ID MessageLog документа (ДТ), для которого запускается процедура автовыпуска")
            @RequestParam("id") String messageLogId) {

        LOGGER.debug("Receive DT's ID: {}", messageLogId);
        MessageLogDto messageLogDto = messagesLogReadService.getById(messageLogId);
        autoRelease.getRegistered(messageLogDto);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(CommonException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleCommonException(CommonException e) {
        return new ResponseEntity<>("Not valid id!", HttpStatus.BAD_REQUEST);
    }
}
