package by.btslogistics.autorelease.service.releasedocument.createregistrationnumber.impl;


import by.btslogistics.autorelease.service.dto.customs.CustomsOperatIniDto;
import by.btslogistics.autorelease.service.dto.getregistrationnumber.GetRegistrationNumberDto;
import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import by.btslogistics.autorelease.service.read.customs.CustomsOperatIniReadService;
import by.btslogistics.autorelease.service.read.getregistrationnumber.GetRegistrationNumberReadService;
import by.btslogistics.autorelease.service.releasedocument.createregistrationnumber.CreateRegistrationNumberService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static by.btslogistics.commons.service.xml.utils.date.DateUtils.getCurrentYear;
import static by.btslogistics.commons.service.xml.utils.string.StringUtils.transliterate;
import static by.btslogistics.commons.service.xml.utils.transforming.TransformingStrToNumberViseVersa.*;

/**
 * генерация номера выпуска (короткого номера)
 */
@Service
public class CreateRegistrationNumberServiceImpl implements CreateRegistrationNumberService {

    private final CustomsOperatIniReadService customsOperatIniReadService;

    private final GetRegistrationNumberReadService getRegistrationNumberReadService;

    public CreateRegistrationNumberServiceImpl(CustomsOperatIniReadService customsOperatIniReadService,
                                               GetRegistrationNumberReadService getRegistrationNumberReadService) {
        this.customsOperatIniReadService = customsOperatIniReadService;
        this.getRegistrationNumberReadService = getRegistrationNumberReadService;
    }

    @Override
    public String getRegistrationNumberShort(MessageLogDto dto, String code) {
        CustomsOperatIniDto settings = customsOperatIniReadService.getEntityByDocType(dto.getTypeDoc(), "REG");
        GetRegistrationNumberDto getRegistrationNumberDto = getRegistrationNumberReadService.getNextNumber(
                stringToLong(settings.getUsl2()),
                settings.getUsl3(),
                getCurrentYear(LocalDateTime.now()),
                stringToLong(dto.getCustom()),
                stringToLong(dto.getPto())
                , settings.getUsl1());

        return transliterate(
                formatTwoDigits(getRegistrationNumberDto.getHouse())
                + getRegistrationNumberDto.getPoint().toString()
                + "/"
                + Math.abs(getRegistrationNumberDto.getYear() % 10)
                + ("".equals(code) ? getRegistrationNumberDto.getLogType() : code)
                + formatSixDigits(getRegistrationNumberDto.getMaxNum()));
    }

}
