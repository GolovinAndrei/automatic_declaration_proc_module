package by.btslogistics.autorelease.service.dto.messageslogstage;


import by.btslogistics.commons.dao.dto.messageslogstage.MessagesLogStageDefaultDto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MessagesLogStageDto extends MessagesLogStageDefaultDto {

    private String messageLogId;

}
