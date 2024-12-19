package com.qiu.api.utils;

import cn.hutool.core.date.DateTime;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @description: OSS客户端
 * @className: OssClient.java
 * @author: qiu
 * @createTime: 2024/3/25 21:27
 */
public class OssClient {

    @Value("${aliyun.oss.endpoint}")
    private static String ENDPOINT;

    @Value("${aliyun.oss.access-key}")
    private static String ACCESS_KEY;

    @Value("${aliyun.oss.secret-key}")
    private static String SECRET_KEY;

    @Value("${aliyun.oss.bucket-name}")
    private static String BUCKET_NAME;

    private static final String USERCENTER = "usercenter";

    public static String uploadAvatar(MultipartFile file, Long userId) throws IOException {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY, SECRET_KEY);
        // 获取上传文件输入流
        InputStream inputStream = file.getInputStream();
        // 获取文件名称
        String fileName = file.getOriginalFilename();
        // 文件名UUID
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        fileName = uuid + fileName;
        // 把文件按照项目，用户id，日期进行分类
        String datePath = new DateTime().toString("yyyy/MM/dd");
        // 拼接 usercenter/用户id/日期/uuidName.jpg
        fileName = USERCENTER + "/" + userId + "/" + datePath + "/" + fileName;
        String url = null;
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, fileName, inputStream);
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 拼接文件路径
            // https://invictusqiu.oss-cn-beijing.aliyuncs.com/2023/10/06/b2378d8267114075b921594f32b8ceb2file.png
            url = "https://" + BUCKET_NAME + "." + ENDPOINT + "/" + fileName;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return url;
    }

}
