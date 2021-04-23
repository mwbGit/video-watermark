package com.harry.videowatermark.service;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2020/12/7
 */
public interface WaterService {

    Integer vipUrlType(String url, String deviceId);

    int incrementParseTimes(String deviceId, String appVersion,int times);

    void addParseRecord(String deviceId, String appVersion, String url, boolean ok);
}
