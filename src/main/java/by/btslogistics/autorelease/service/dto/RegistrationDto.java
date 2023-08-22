package by.btslogistics.autorelease.service.dto;

import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class RegistrationDto {

    private MessageLogDto messageLog;

    private eec.r._036.goodsdeclaration.v1_3_1.GoodsDeclarationType goodsDeclarationTypeOld;

    private by.btslogistics.xsdclasses.eec.v1_4_0.eec.r._036.goodsdeclaration.v1_4_0.GoodsDeclarationType goodsDeclarationTypeNew;

    /**
     * Отметка о прохождении первой части контроля (без протоколов)
     */
    private boolean isElementaryChecksPassed = false;

    /**
     * Отметка о прохождении второй части контроля (с протоколами)
     */
    private boolean isChecksWithAppliedDocsPassed = false;

    public RegistrationDto(MessageLogDto messageLog) {
        this.messageLog = messageLog;
    }

}
