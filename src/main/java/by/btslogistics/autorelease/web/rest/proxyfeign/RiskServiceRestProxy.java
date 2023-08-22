package by.btslogistics.autorelease.web.rest.proxyfeign;


import by.btslogistics.autorelease.service.dto.riskresult.RisksResultDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static by.btslogistics.autorelease.web.constant.AutoReleaseConstants.URL_TO_GET_RISK_PROTOCOL;

@FeignClient(name = "${risks.service.name}", url = "${risks.service.path}")
public interface RiskServiceRestProxy {

    @PostMapping(URL_TO_GET_RISK_PROTOCOL)
    ResponseEntity<List<RisksResultDto>> checkDocumentOnProfile(@RequestParam String messageLogId,
                                                          @RequestParam String codeProc,
                                                          @RequestParam String typeDoc,
                                                          @RequestBody(required = false) String xml);
}
