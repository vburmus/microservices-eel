package com.epam.esm.controller;

import com.epam.esm.service.AwsUtilsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/aws")
@RequiredArgsConstructor
public class AwsUtilsController {
    private final AwsUtilsService awsUtilsService;

    @PostMapping("/upload-image")
    public String handleFileUpload(@RequestPart String directory,
                                   @RequestPart MultipartFile image) {
        return awsUtilsService.loadImage(directory, image);
    }
}