package com.harry.videowatermark.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harry.videowatermark.mapper.WaterOrderMapper;
import com.harry.videowatermark.mapper.WaterOrderReportMapper;
import com.harry.videowatermark.model.WaterOrder;
import com.harry.videowatermark.model.WaterOrderDetail;
import com.harry.videowatermark.model.WaterOrderReport;
import com.harry.videowatermark.service.WaterOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2020/12/3
 */
@Slf4j
@Service
public class WaterOrderServiceImpl extends BaseServiceImpl<WaterOrder> implements WaterOrderService {

    @Autowired
    private WaterOrderMapper waterOrderMapper;
    @Autowired
    private WaterOrderReportMapper reportMapper;

    @Override
    public void addOrder(WaterOrder waterOrder, String json) {
        try {
            Long id = waterOrderMapper.selectIdByDevice_id(waterOrder.getDevice_id());
            if (id != null) {
                waterOrder.setId(id);
                updateNotNull(waterOrder);
            } else {
                saveNotNull(waterOrder);
            }
            waterOrderMapper.insertDetail(waterOrder.getId(), json);
            log.info("WaterServiceImpl.addOrder ok waterOrder={},", waterOrder);
        } catch (Exception e) {
            log.error("WaterServiceImpl.addOrder err waterOrder={},json={}", waterOrder, json, e);
        }
    }

    @Override
    public WaterOrder getByDeviceId(String deviceId) {
        return waterOrderMapper.selectByDevice_id(deviceId);
    }

    @Override
    public boolean report(String payload, String device_id) {
        WaterOrderReport report = new WaterOrderReport();
        report.setReceipt_data(payload);
        report.setDevice_id(device_id);
        reportMapper.insertSelective(report);
        try {
            //线上环境验证
            report.setStatus(1);
            String verifyResult = buyAppVerify(payload, 0);
            if (verifyResult == null) {
                log.error("WaterOrderServiceImpl.report war verifyResult is null id={}", report.getId());
            } else {
                JSONObject appleReturn = JSONObject.parseObject(verifyResult);
                String states = appleReturn.getString("status");
                //无数据则沙箱环境验证
                if ("21007".equals(states)) {
                    verifyResult = buyAppVerify(payload, 1);
                    appleReturn = JSONObject.parseObject(verifyResult);
                    states = appleReturn.getString("status");
                }
                log.info("WaterOrderServiceImpl.report：appleReturn={}", appleReturn);
                // 前端所提供的收据是有效的    验证成功
                if (states.equals("0")) {
                    JSONArray array = appleReturn.getJSONArray("latest_receipt_info");
                    if (array != null && !array.isEmpty()) {
                        String receipt = appleReturn.getString("receipt");
                        JSONObject returnJson = JSONObject.parseObject(receipt);
                        String version = returnJson.getString("application_version");

                        WaterOrderDetail detail = array.getObject(0, WaterOrderDetail.class);
                        WaterOrder waterOrder = new WaterOrder();
                        waterOrder.setReceipt_data(appleReturn.getString("latest_receipt"));
                        waterOrder.setDevice_id(device_id);
                        waterOrder.setApp_version(version);
                        waterOrder.setProduct_id(detail.getProduct_id());
                        waterOrder.setIs_trial_period(detail.getIs_trial_period());
                        waterOrder.setExpires_date_ms(new Date(Long.parseLong(detail.getExpires_date_ms())));
                        waterOrder.setPurchase_date_ms(new Date(Long.parseLong(detail.getPurchase_date_ms())));

                        //添加订单
                        addOrder(waterOrder, JSONObject.toJSONString(detail));
                        report.setStatus(2);
                        return waterOrder.getExpires_date_ms().after(new Date());
                    } else {
                        log.info("WaterOrderServiceImpl.report order is null");
                    }
                } else {
                    log.error("WaterOrderServiceImpl.report fail appleReturn states={}", states);
                }
            }
        } catch (Exception e) {
            log.error("WaterOrderServiceImpl.report err id={}", report.getId());
        }
        reportMapper.updateByPrimaryKeySelective(report);
        log.info("WaterOrderServiceImpl.report order ok");

        return false;
    }

    private static final String PWD = "b67f1cf086ff4b569f9ffc2102e3c5df";
    private static final String SANDBOX = "https://sandbox.itunes.apple.com/verifyReceipt";
    private static final String ONLINE = "https://buy.itunes.apple.com/verifyReceipt";

    private String buyAppVerify(String receipt, int type) {
        try {
            return HttpUtil.post(type == 0 ? ONLINE : SANDBOX, "{\"receipt-data\":\"" + receipt + "\", \"password\":\"" + PWD + "\"}");
        } catch (Exception e) {
            log.error("WaterOrderServiceImpl.buyAppVerify err receipt={} type={}", receipt, type, e);
        }
        return null;
    }
}
