package com.epam.esm.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.epam.esm.utils.ampq.ImageUploadRequest;
import com.epam.esm.utils.ampq.ImageUploadResponse;
import com.epam.esm.utils.ampq.MessagePublisher;
import com.epam.esm.utils.exceptionhandler.exceptions.FileUploadException;
import com.epam.esm.utils.exceptionhandler.exceptions.NullableFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

import static com.epam.esm.utils.Constants.EXTENSION;
import static com.epam.esm.utils.Constants.FILE_CAN_T_BE_NULL;

@Service
@RequiredArgsConstructor
public class AwsUtilsService {
    private final MessagePublisher messagePublisher;
    @Value("${aws.access}")
    private String accessKey;
    @Value("${aws.secret}")
    private String secretKey;
    @Value("${aws.content.bucket.name}")
    private String bucketName;

    public void loadByteImage(ImageUploadRequest imageUploadRequest, String directory) {
        byte[] imageBytes = imageUploadRequest.imageBytes();
        if (imageBytes == null || imageBytes.length == 0) {
            throw new NullableFileException(FILE_CAN_T_BE_NULL);
        }
        try (ByteArrayInputStream imageInputStream = new ByteArrayInputStream(imageBytes)) {
            Regions region = Regions.EU_NORTH_1;
            AmazonS3 s3Client = createS3Client(region);
            ObjectMetadata metadata = createS3ObjectMetadata(imageBytes);
            String objectKey = createS3ObjectKey(directory);
            s3Client.putObject(new PutObjectRequest(bucketName, objectKey, imageInputStream, metadata));
            String imageURI = createS3ObjectUrl(region, objectKey);

            ImageUploadResponse imageUploadResponse = new ImageUploadResponse(imageUploadRequest.userId(), imageURI);
            messagePublisher.publishLoadedImageResponse(imageUploadResponse);
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

    private ObjectMetadata createS3ObjectMetadata(byte[] imageBytes) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageBytes.length);
        return metadata;
    }

    private String createS3ObjectKey(String directory) {
        String imageId = UUID.randomUUID().toString();
        return directory + "/" + imageId + "." + EXTENSION;
    }
}