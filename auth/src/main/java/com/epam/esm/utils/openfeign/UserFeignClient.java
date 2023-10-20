package com.epam.esm.utils.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "user-service", configuration = CustomFeignClientConfiguration.class)
public interface UserFeignClient {
    @PostMapping(value = "api/v1/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    User create(@RequestPart("user") CreateUserRequest createUserRequest,
                @RequestPart("image") MultipartFile image);

    @GetMapping("api/v1/users/by-email")
    User getByEmail(@RequestParam("email") String email);
}