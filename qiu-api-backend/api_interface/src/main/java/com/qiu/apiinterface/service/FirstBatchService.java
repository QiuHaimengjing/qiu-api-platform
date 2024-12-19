package com.qiu.apiinterface.service;

import com.qiu.apiclientsdk.model.firstBatch.RollCallRequest;

import java.util.List;

/**
 * @description: 第一批接口服务
 * @className: FirstBatchService.java
 * @author: qiu
 * @createTime: 2024/3/28 10:53
 */
public interface FirstBatchService {

    List<String> rollCall(RollCallRequest rollCallRequest);

    String isLeapYear(Long year);

    String dateInterval(String startDate, String endDate);

}
