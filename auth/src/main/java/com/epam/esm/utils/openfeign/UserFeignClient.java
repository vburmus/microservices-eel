package com.epam.esm.utils.openfeign;

import com.epam.esm.model.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "user-service", configuration = CustomFeignClientConfiguration.class)
public interface UserFeignClient {
    @PostMapping(value = "api/v1/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    UserDTO create(@RequestPart("user") CreateUserRequest createUserRequest,
                   @RequestPart("image") MultipartFile image);

    @GetMapping("api/v1/users/by-email")
    UserDTO getByEmail(@RequestParam("email") String email);
}