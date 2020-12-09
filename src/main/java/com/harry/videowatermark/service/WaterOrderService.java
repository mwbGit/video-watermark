package com.harry.videowatermark.service;

import com.harry.videowatermark.model.WaterOrder;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2020/12/3
 */
public interface WaterOrderService extends BaseService<WaterOrder> {

    void addOrder(WaterOrder waterOrder, String json);

    boolean report(String payload, String device_id);

    WaterOrder getByDeviceId(String deviceId);

}
