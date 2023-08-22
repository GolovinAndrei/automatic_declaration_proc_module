package by.btslogistics.autorelease.web.rest.proxyfeign;

import by.btslogistics.commons.dao.dto.subsequent.SubsequentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static by.btslogistics.autorelease.web.constant.AutoReleaseConstants.URL_FOR_SVX_WRITEOFF;

@FeignClient(name = "${svx.service.name}", url = "${svx.service.path}")
public interface RegistrationSvxRestProxy {

    @PostMapping(URL_FOR_SVX_WRITEOFF)
    ResponseEntity<Object> autoWriteOffGoods(@RequestBody SubsequentDto subsequentDto);

}
