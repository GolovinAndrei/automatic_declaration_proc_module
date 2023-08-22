package by.btslogistics.autorelease.service.read.getregistrationnumber;

import by.btslogistics.autorelease.service.dto.getregistrationnumber.GetRegistrationNumberDto;

public interface GetRegistrationNumberReadService {

    /**
     * Поиск в таблице GET_REGISTRATION_NUMBER по полям ISIN, REVIEWTYPE, YEAR, HOUSE и POINT
     *
     * @param isIn       - Признак въезд - 1, выезд - 0
     * @param reviewType - Тип операции (NEW или REG)
     * @param year       - текущий годм
     * @param house      - Номер таможни(06, 20, 12 и т.п.)
     * @param point      - Номер ПТО (555, 122)
     * @param logType    - Номер регистрационного журнала
     * @return - возвращается объект, который был сохранен
     */
    GetRegistrationNumberDto getNextNumber(Long isIn, String reviewType, Integer year, Long house, Long point, String logType);

}
