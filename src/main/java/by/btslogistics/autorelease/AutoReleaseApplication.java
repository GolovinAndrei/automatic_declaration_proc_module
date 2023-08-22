package by.btslogistics.autorelease;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.WebApplicationInitializer;

import java.time.LocalDateTime;


@SpringBootApplication
@EnableFeignClients("**.************.autorelease.web.rest.proxyfeign")
@EnableEurekaClient
public class AutoReleaseApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoReleaseApplication.class);

    public static void main(String[] args) {

        LOGGER.debug("({}) Запуск модуля АвтоВыпуск ... ", LocalDateTime.now());

        SpringApplication.run(AutoReleaseApplication.class, args);
    }
}
