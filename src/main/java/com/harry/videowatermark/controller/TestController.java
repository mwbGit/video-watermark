package com.harry.videowatermark.controller;

import com.harry.videowatermark.mapper.WaterOrderMapper;
import com.harry.videowatermark.mapper.WaterOrderReportMapper;
import com.harry.videowatermark.model.ApiResult;
import com.harry.videowatermark.service.WaterOrderService;
import com.harry.videowatermark.service.WaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2020/12/5
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private WaterOrderService waterOrderService;

    @Autowired
    private WaterOrderMapper mapper;

    @Autowired
    private WaterService waterService;

    @RequestMapping("/a")
    public ApiResult getInfo(String url, String deviceId) {
        Integer type = waterService.vipUrlType(url, deviceId);

        return ApiResult.success(type);
    }

    @RequestMapping("/b")
    public ApiResult getInfo1(String url) {
        waterService.addParseRecord("aa", "bb", "cc", true);

        return ApiResult.success(111);
    }
}
