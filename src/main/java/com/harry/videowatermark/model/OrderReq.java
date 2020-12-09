package com.harry.videowatermark.model;

import cn.hutool.http.HttpUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2020/12/5
 */
@Data
public class OrderReq {

    private String sign;
    private String source;
    private String deviceId;
    private String receiptData;

    public static void main(String[] args) {
        String body = "deviceId=AB65BAD4-F3C7-417C-A61A-81E174B5094E&receiptData=nil&sign=f2ae62046f56c43da0bf99acb6c7d8c1&source=watermark&version1=Optional%28%221.1.1%22%29\n";
        Map<String, Object> map = new HashMap<>();
        map.put("deviceId", "aa");
        String ss = HttpUtil.post("http://localhost:8080/report", body);
        System.out.println(ss);
    }
}
