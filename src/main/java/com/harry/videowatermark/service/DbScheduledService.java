package com.harry.videowatermark.service;

import com.alibaba.fastjson.JSONObject;
import com.harry.videowatermark.mapper.WaterOrderMapper;
import com.harry.videowatermark.model.WaterOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2021/4/23
 */
@Slf4j
@Service
public class DbScheduledService {

    @Autowired
    private WaterOrderMapper waterOrderMapper;

    @Scheduled(cron = "0 0 0/6 * * ? ")
    public void dbScheduled() {
        try {
            List<WaterOrder> orders =  waterOrderMapper.selectAll();
            log.warn("orders={}", JSONObject.toJSONString(orders));
        } catch (Exception e) {
            log.error("DbScheduledService.dbScheduled err" , e);
        }
    }

    @PostConstruct
    private void init() {
        dbScheduled();
    }
}
