package com.qiu.apiinterface.controller;

import com.qiu.apiclientsdk.model.firstBatch.RollCallRequest;
import com.qiu.apiinterface.service.FirstBatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 第一批开放接口
 * @className: FirstBatchController.java
 * @author: qiu
 * @createTime: 2024/3/28 10:38
 */
@RestController
@Api(tags = "第一批开放接口")
@RequestMapping("/first")
public class FirstBatchController {

    @Resource
    private FirstBatchService firstBatchService;

    @ApiOperation("随机点名")
    @PostMapping("/roll-call")
    public List<String> rollCall(@ApiParam("点名请求参数") @RequestBody RollCallRequest rollCallRequest) {
        return firstBatchService.rollCall(rollCallRequest);
    }

    @ApiOperation("判断闰年")
    @GetMapping("/leap-year")
    public String isLeapYear(@ApiParam("年份") @RequestParam Long year) {
        return firstBatchService.isLeapYear(year);
    }

    @ApiOperation("判断两年间隔时间")
    @GetMapping("/date-interval")
    public String dateInterval(@ApiParam("起始年") @RequestParam String startDate,
                               @ApiParam("结束年") @RequestParam String endDate) {
        return firstBatchService.dateInterval(startDate, endDate);
    }

}
