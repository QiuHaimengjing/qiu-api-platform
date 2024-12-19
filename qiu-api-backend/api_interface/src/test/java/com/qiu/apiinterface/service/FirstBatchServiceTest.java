package com.qiu.apiinterface.service;

import com.qiu.apiclientsdk.model.firstBatch.RollCallRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @description: 第一批接口服务测试
 * @className: FirstBatchServiceTest.java
 * @author: qiu
 * @createTime: 2024/3/28 11:07
 */
@SpringBootTest
public class FirstBatchServiceTest {

    @Resource
    private FirstBatchService firstBatchService;

    @Test
    void rollCall() {
        RollCallRequest rollCallRequest = new RollCallRequest();
        rollCallRequest.setParticipants(Arrays.asList("a", "b", "c"));
        rollCallRequest.setNumber(1);
        List<String> list = firstBatchService.rollCall(rollCallRequest);
        System.out.println(list);
    }


}
