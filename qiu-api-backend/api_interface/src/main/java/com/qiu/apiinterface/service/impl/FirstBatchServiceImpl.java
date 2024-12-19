package com.qiu.apiinterface.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.qiu.apiclientsdk.model.firstBatch.RollCallRequest;
import com.qiu.apiinterface.service.FirstBatchService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @description: 第一批接口服务实现类
 * @className: FirstBatchServiceImpl.java
 * @author: qiu
 * @createTime: 2024/3/28 10:55
 */
@Service
public class FirstBatchServiceImpl implements FirstBatchService {

    @Override
    public List<String> rollCall(RollCallRequest rollCallRequest) {
        List<String> participants = rollCallRequest.getParticipants();
        Integer number = rollCallRequest.getNumber();
        // 1.校验请求参数
        if (CollUtil.isEmpty(participants)) {
            return Collections.emptyList();
        }
        if (BeanUtil.isEmpty(number) || number <= 0) {
            return Collections.emptyList();
        }
        // 2.随机点名
        List<String> result = new ArrayList<>();
        Random random = new Random();
        int totalParticipants = participants.size();

        // 确保不重复点名，所以需要一个临时列表
        List<Integer> chosenIndices = new ArrayList<>();
        while (result.size() < number) {
            int randomIndex = random.nextInt(totalParticipants);
            if (!chosenIndices.contains(randomIndex)) {
                result.add(participants.get(randomIndex));
                chosenIndices.add(randomIndex);
            }
        }
        return result;
    }

    @Override
    public String isLeapYear(Long year) {
        if (BeanUtil.isEmpty(year)) {
            return "年份不能为空";
        }
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
            return String.format("%s是闰年", year);
        }
        return String.format("%s是平年", year);
    }

    @Override
    public String dateInterval(String startDateStr, String endDateStr) {
        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        long between = ChronoUnit.DAYS.between(startDate, endDate);
        return String.format("两个日期间隔%s天", between);
    }
}
