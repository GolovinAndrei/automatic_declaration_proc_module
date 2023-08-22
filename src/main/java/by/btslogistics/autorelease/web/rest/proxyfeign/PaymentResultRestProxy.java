package by.btslogistics.autorelease.web.rest.proxyfeign;

import by.btslogistics.autorelease.service.dto.paymentreport.PaymentReportDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static by.btslogistics.autorelease.web.constant.AutoReleaseConstants.URL_TO_GET_PAYMENT_REPORT;

@FeignClient(name = "${payment.service.name}", url = "${payment.service.path}")
public interface PaymentResultRestProxy {

    @GetMapping(URL_TO_GET_PAYMENT_REPORT)
    Iterable<PaymentReportDto> getPaymentReport(@RequestParam("idDeclaration") String id);

}
