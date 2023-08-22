package by.btslogistics.autorelease.web.rest.proxyfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

import static by.btslogistics.autorelease.web.constant.AutoReleaseConstants.URL_GET_CERTIFICATE_REPORT;
import static by.btslogistics.autorelease.web.constant.AutoReleaseConstants.URL_TO_GET_ALL_OBJID_FROM_ZIO;


@FeignClient(name = "${zio.service.name}", url = "${zio.service.path}")
public interface ZioRestProxy {

    @GetMapping(URL_TO_GET_ALL_OBJID_FROM_ZIO)
    List<String> getAllObjIdByTnVed (@PathVariable(name = "goodNumber") int goodNumber,
                                     @PathVariable(name = "codeTnVed") String codeTnVed,
                                     @PathVariable(name = "dateTime") LocalDateTime dateTime);

    @GetMapping(URL_GET_CERTIFICATE_REPORT)
    Boolean getCertificateReport (@PathVariable (name = "messagesLogId") String messageLogId);


}