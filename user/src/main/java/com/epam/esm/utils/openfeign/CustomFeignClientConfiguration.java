package com.epam.esm.utils.openfeign;

import com.epam.esm.utils.exceptionhandler.exceptions.RestApiClientException;
import com.epam.esm.utils.exceptionhandler.exceptions.RestApiServerException;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import static com.epam.esm.utils.Constants.*;

public class CustomFeignClientConfiguration {

    @Bean
    public Encoder multipartFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(() -> new HttpMessageConverters(new RestTemplate().getMessageConverters())));
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            String requestUrl = response.request().url();
            HttpStatus responseStatus = HttpStatus.valueOf(response.status());
            if (responseStatus.is5xxServerError()) {
                return new RestApiServerException(AN_INTERNAL_SERVER_ERROR_OCCURRED_WHILE_PROCESSING_THE_REQUEST);
            } else if (responseStatus.is4xxClientError()) {
                return new RestApiClientException(ERROR_WHILE_MAKING_API_CALL_TO + requestUrl);
            } else {
                return new Exception(GENERIC_EXCEPTION);
            }
        };
    }
}