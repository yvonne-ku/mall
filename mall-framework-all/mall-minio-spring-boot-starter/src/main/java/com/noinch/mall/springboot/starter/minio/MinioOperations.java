package com.noinch.mall.springboot.starter.minio;

import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import org.springframework.web.multipart.MultipartFile;

/**
 * 抽象一组基本 Minio 操作的接口，由 {@link MinioTemplate} 实现
 *
 */
public interface MinioOperations {

    /**
     * 上传文件
     */
    ObjectWriteResponse upload(PutObjectArgs args);

    /**
     * 上传文件
     */
    String upload(MultipartFile multipartFile);
}
