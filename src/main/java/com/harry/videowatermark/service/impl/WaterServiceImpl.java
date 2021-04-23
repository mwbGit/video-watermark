package com.harry.videowatermark.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.harry.videowatermark.mapper.WaterOrderMapper;
import com.harry.videowatermark.mapper.WaterParseRecordMapper;
import com.harry.videowatermark.mapper.WaterParseTimesMapper;
import com.harry.videowatermark.mapper.WaterVipUrlMapper;
import com.harry.videowatermark.model.WaterOrder;
import com.harry.videowatermark.model.WaterParseRecord;
import com.harry.videowatermark.model.WaterParseTimes;
import com.harry.videowatermark.model.WaterVipUrl;
import com.harry.videowatermark.service.WaterService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2020/12/7
 */
@Slf4j
@Service
public class WaterServiceImpl implements WaterService {

    private static Map<String, WaterVipUrl> URL_TYPE_MAP = MapUtil.newHashMap(0);

    @Autowired
    private WaterVipUrlMapper vipUrlMapper;

    @Autowired
    private WaterParseTimesMapper timesMapper;

    @Autowired
    private WaterOrderMapper waterOrderMapper;
    @Autowired
    private WaterParseRecordMapper waterParseRecordMapper;

    @PostConstruct
    public void init() {
        List<WaterVipUrl> waterVipUrls = vipUrlMapper.selectAll();
        if (CollUtil.isNotEmpty(waterVipUrls)) {
            URL_TYPE_MAP = waterVipUrls.stream().filter(item -> item.getStatus() > 0).collect(Collectors.toMap(WaterVipUrl::getUrl, o -> o));
        }
    }

    @Override
    public Integer vipUrlType(String url, String deviceId) {
        try {
            if (URL_TYPE_MAP.containsKey(url)) {
                WaterVipUrl waterVipUrl = URL_TYPE_MAP.get(url);
                DateTime now = new DateTime();
                WaterOrder waterOrder = new WaterOrder();
                waterOrder.setProduct_id(waterVipUrl.getType() == 0 ? "105" : "106");
                waterOrder.setExpires_date_ms(waterVipUrl.getType() == 0 ? now.plusMonths(1).toDate() : now.plusYears(1).toDate());
                waterOrder.setPurchase_date_ms(now.toDate());
                waterOrder.setApp_version("0");
                Long id = waterOrderMapper.selectIdByDevice_id(deviceId);
                if (id != null) {
                    waterOrder.setId(id);
                    waterOrderMapper.updateByPrimaryKeySelective(waterOrder);
                } else {
                    waterOrder.setReceipt_data("");
                    waterOrder.setDevice_id(deviceId);
                    waterOrder.setIs_trial_period("false");
                    waterOrderMapper.insertSelective(waterOrder);
                }
                waterVipUrl.setStatus(waterVipUrl.getStatus() - 1);
                vipUrlMapper.updateByPrimaryKeySelective(waterVipUrl);
                if (waterVipUrl.getStatus() <= 0) {
                    URL_TYPE_MAP.remove(waterVipUrl.getUrl());
                }
                log.info("WaterServiceImpl.isVipUrl ok url={}, deviceId={}", url, deviceId);
                return waterVipUrl.getType();
            }
        } catch (Exception e) {
            log.error("WaterServiceImpl.isVipUrl err url={}, deviceId={}", url, deviceId);
        }
        return null;
    }

    @Override
    public int incrementParseTimes(String deviceId, String appVersion, int times) {
        WaterParseTimes waterParseTimes = timesMapper.selectByDevice_id(deviceId);
        if (waterParseTimes == null) {
            waterParseTimes = new WaterParseTimes();
            waterParseTimes.setTimes(times);
            waterParseTimes.setApp_version(appVersion);
            waterParseTimes.setDevice_id(deviceId);
            timesMapper.insertSelective(waterParseTimes);
        } else {
            waterParseTimes.setTimes(waterParseTimes.getTimes() + times);
            waterParseTimes.setUpdate_time(new Date());
            timesMapper.updateByPrimaryKeySelective(waterParseTimes);
        }
        return waterParseTimes.getTimes();
    }

    @Async
    @Override
    public void addParseRecord(String deviceId, String appVersion, String url, boolean ok) {
        WaterParseRecord waterParseRecord = new WaterParseRecord();
        try {
            waterParseRecord.setDevice_id(deviceId);
            waterParseRecord.setApp_version(appVersion);
            waterParseRecord.setUrl(url);
            waterParseRecord.setStatus(ok ? 1 : 0);
            waterParseRecordMapper.insertSelective(waterParseRecord);
        } catch (Exception e) {
            log.error("addParseRecord err record={}", waterParseRecord);
        }
    }
}
