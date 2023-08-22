package by.btslogistics.autorelease.web.rest.proxyfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static by.btslogistics.autorelease.web.constant.AutoReleaseConstants.URL_TO_REMOVED_CONTROL_KSTP;

@FeignClient(name = "${kstp.service.name}", url = "${kstp.service.path}")
public interface KstpRestProxy {

    @PostMapping(URL_TO_REMOVED_CONTROL_KSTP)
    void startRemovedControlKstp(@RequestParam String messageLogId);
}
