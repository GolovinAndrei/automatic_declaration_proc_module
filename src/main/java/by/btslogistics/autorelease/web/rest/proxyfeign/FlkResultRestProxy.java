package by.btslogistics.autorelease.web.rest.proxyfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import static by.btslogistics.autorelease.web.constant.AutoReleaseConstants.URL_TO_READ_FLK_RESULT_TO_RECEIVE_SERVICE;

@FeignClient(name = "${flk.service.name}", url = "${flk.service.path}")
public interface FlkResultRestProxy {

    @GetMapping(URL_TO_READ_FLK_RESULT_TO_RECEIVE_SERVICE)
    Map<Integer, Integer> getFlkResult(@RequestParam String idDeclaration);
}
