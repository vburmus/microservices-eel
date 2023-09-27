package com.epam.esm.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.epam.esm.exceptionhandler.exceptions.FileUploadException;
import com.epam.esm.utils.Validation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.epam.esm.utils.Constants.DIRECTORY_SWAGGER_DESCRIPTION;

@Service
public class AwsUtilsService {
    @Value("${aws.access}")
    private String accessKey;
    @Value("${aws.secret}")
    private String secretKey;
    @Value("${aws.content.bucket.name}")
    private String bucketName;

    public String loadImage(@Parameter(description = DIRECTORY_SWAGGER_DESCRIPTION) String directory,
                            MultipartFile image) {
        String extension = Validation.validateImage(image);
        try (InputStream imageInputStream = image.getInputStream()) {
            Regions region = Regions.EU_NORTH_1;
            AmazonS3 s3Client = createS3Client(region);
            ObjectMetadata metadata = createS3ObjectMetadata(image);
            String objectKey = createS3ObjectKey(directory, extension);
            s3Client.putObject(new PutObjectRequest(bucketName, objectKey, imageInputStream, metadata));
            return createS3ObjectUrl(region, objectKey);
        } catch (AmazonS3Exception e) {
            throw new FileUploadException(e.getMessage(), e.getStatusCode());
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private String createS3ObjectUrl(Regions region, String objectKey) {
        return "https://" + bucketName + ".s3." + region.getName() + ".amazonaws.com/" + objectKey;
    }

    private AmazonS3 createS3Client(Regions region) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    private ObjectMetadata createS3ObjectMetadata(MultipartFile image) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());
        return metadata;
    }

    private String createS3ObjectKey(String directory, String extension) {
        String imageId = UUID.randomUUID().toString();
        return directory + "/" + imageId + "." + extension;
    }
}