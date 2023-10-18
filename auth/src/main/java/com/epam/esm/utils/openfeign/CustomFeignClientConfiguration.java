package com.epam.esm.utils.openfeign;

import com.epam.esm.utils.exceptionhandler.exceptions.RestApiClientException;

import com.epam.esm.utils.exceptionhandler.exceptions.RestApiServerException;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.ContentType;
import feign.form.MultipartFormContentProcessor;
import feign.form.spring.SpringFormEncoder;
import feign.form.spring.SpringManyMultipartFilesWriter;
import feign.form.spring.SpringSingleMultipartFileWriter;
import org.springframework.cloud.openfeign.support.JsonFormWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import static com.epam.esm.utils.Constants.*;

@Import(JsonFormWriter.class)
public class CustomFeignClientConfiguration {

    @Bean
    Encoder feignEncoder(JsonFormWriter jsonFormWriter) {
        return new SpringFormEncoder() {{
            var processor = (MultipartFormContentProcessor) getContentProcessor(ContentType.MULTIPART);
            processor.addFirstWriter(jsonFormWriter);
            processor.addFirstWriter(new SpringSingleMultipartFileWriter());
            processor.addFirstWriter(new SpringManyMultipartFilesWriter());
        }};
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            String requestUrl = response.request().url();
            HttpStatus responseStatus = HttpStatus.valueOf(response.status());
            if (responseStatus.is5xxServerError()) {
                return new RestApiServerException(AN_INTERNAL_SERVER_ERROR_OCCURRED);
            } else if (responseStatus.is4xxClientError()) {
                return new RestApiClientException(ERROR_WHILE_MAKING_API_CALL_TO + requestUrl);
            } else {
                return new Exception(GENERIC_EXCEPTION);
            }
        };
    }
}