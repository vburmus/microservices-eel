package com.epam.esm.utils.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@FeignClient(name = "aws-utils", configuration = CustomFeignClientConfiguration.class)
public interface AwsUtilsFeignClient {
    @PostMapping(value = "api/v1/aws/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadImage(@RequestPart("directory") String directory,
                       @RequestPart("image") MultipartFile image);
}