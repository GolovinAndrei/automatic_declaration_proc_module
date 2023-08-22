package by.btslogistics.autorelease.web.rest.proxyfeign;

import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static by.btslogistics.autorelease.web.constant.AutoReleaseConstants.URL_SEND_TO_ARTD;


@FeignClient(name = "${artd.service.name}", url = "${artd.service.path}")
public interface ArtdRestProxy {


    @PostMapping(URL_SEND_TO_ARTD)
    void sendToArtd(@RequestBody MessageLogDto messageLogDto);
}
