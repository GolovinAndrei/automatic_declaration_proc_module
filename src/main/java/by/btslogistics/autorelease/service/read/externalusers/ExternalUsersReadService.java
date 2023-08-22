package by.btslogistics.autorelease.service.read.externalusers;

import by.btslogistics.autorelease.service.dto.externalusers.ExternalUsersDto;

/**
 * Обрабатываются запросы к таблице EXTERNAL_USERS
 */
public interface ExternalUsersReadService {

    Iterable<ExternalUsersDto> getByCertSerialNumber(String certSerialNumber);

}
