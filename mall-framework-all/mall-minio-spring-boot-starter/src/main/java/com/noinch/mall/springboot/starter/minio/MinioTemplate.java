package com.noinch.mall.springboot.starter.minio;

import com.noinch.mall.springboot.starter.minio.config.MinioProperties;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@AllArgsConstructor
public class MinioTemplate implements MinioOperations {

    private final MinioClient minioClient;

    private final MinioProperties minioProperties;

    @SneakyThrows
    @Override
    public ObjectWriteResponse upload(PutObjectArgs args) {
        return minioClient.putObject(args);
    }

    @SneakyThrows
    @Override
    public String upload(MultipartFile multipartFile) {
        // 获得原始文件名
        String fileName = multipartFile.getOriginalFilename();

        // 处理文件名重复问题，添加时间戳
        String[] fileNameSplit = fileName.split("\\.");
        fileName = fileNameSplit.length > 1 ? fileNameSplit[0] + "_" + System.currentTimeMillis() + "." + fileNameSplit[1] : fileName + "_" + System.currentTimeMillis();

        // 使用 try-with-resources 确保流关闭
        // 打开流是为了上传文件
        try (InputStream inputStream = multipartFile.getInputStream()) {
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(fileName)
                    .stream(inputStream, inputStream.available(), -1L)
                    .contentType(multipartFile.getContentType())
                    .build();
            minioClient.putObject(objectArgs);
        }
        return fileName;
    }
}
