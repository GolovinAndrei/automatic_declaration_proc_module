package by.btslogistics.autorelease.service.read.customs;

import by.btslogistics.autorelease.service.dto.customs.CustomsOperatIniDto;

public interface CustomsOperatIniReadService {

    /**
     *  Поиск в таблице CUSTOMS_OPERAT_INI записи по полям DOCUM_TYPE и CUST_OPER
     * @param docType - строковое значение типа документа(SD, DT, KDT и т.п.)
     * @param operation - строковое значение типа операци(NEW или REG)
     * @return - возвращает объект
     */
    CustomsOperatIniDto getEntityByDocType(String docType, String operation);

}
