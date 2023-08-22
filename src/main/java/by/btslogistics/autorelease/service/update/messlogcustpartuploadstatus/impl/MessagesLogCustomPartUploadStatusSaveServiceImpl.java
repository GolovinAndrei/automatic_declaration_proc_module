package by.btslogistics.autorelease.service.update.messlogcustpartuploadstatus.impl;

import by.btslogistics.autorelease.dao.model.messlogcustprtuploadstatus.MessagesLogCustomPartUploadStatus;
import by.btslogistics.autorelease.dao.repository.messlogcustprtuploadstatus.MessagesLogCustomPartUploadStatusRepository;
import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import by.btslogistics.autorelease.service.mapper.messlogcustprtuploadstatus.MessagesLogCustomPartUploadStatusMapper;
import by.btslogistics.autorelease.service.update.messlogcustpartuploadstatus.MessagesLogCustomPartUploadStatusSaveService;
import by.btslogistics.commons.dao.dto.messlogcustprtuploadstatus.MessagesLogCustomPartUploadStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessagesLogCustomPartUploadStatusSaveServiceImpl implements MessagesLogCustomPartUploadStatusSaveService {
    private final MessagesLogCustomPartUploadStatusRepository messagesLogCustomPartUploadStatusRepository;
    private final MessagesLogCustomPartUploadStatusMapper messagesLogCustomPartUploadStatusMapper;


    @Override
    public void createAndSave(MessageLogDto messageLogDto) {
        MessagesLogCustomPartUploadStatusDto customPartUploadStatusDto = new MessagesLogCustomPartUploadStatusDto();
        customPartUploadStatusDto.setToMessageLogId(messageLogDto.getId());
        customPartUploadStatusDto.setStatusDoc(messageLogDto.getStatusDoc());
        customPartUploadStatusDto.setStatusDate(messageLogDto.getStatusDate());
        customPartUploadStatusDto.setCustoms(messageLogDto.getCustom());
        customPartUploadStatusDto.setPto(messageLogDto.getPto());
        customPartUploadStatusDto.setSignIsesdUnload(0);
        customPartUploadStatusDto.setSignBdPfvtUnload(0);
        MessagesLogCustomPartUploadStatus entity =
                messagesLogCustomPartUploadStatusMapper.toEntity(customPartUploadStatusDto);
        messagesLogCustomPartUploadStatusRepository.save(entity);
    }
}
