package by.btslogistics.autorelease.service.read.risks;

import by.btslogistics.autorelease.service.dto.risks.CaseCustomControlDto;

public interface CaseCustomControlReadService {

    CaseCustomControlDto getByDtNumber (String dtNumber);
}
