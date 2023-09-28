package com.epam.esm.exceptionhandler.controller;

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
                                   @RequestParam MultipartFile image) {
        return awsUtilsService.loadImage(directory, image);
    }
}