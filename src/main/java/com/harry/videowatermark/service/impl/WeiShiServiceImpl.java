package com.harry.videowatermark.service.impl;

import com.harry.videowatermark.common.JsonUtil;
import com.harry.videowatermark.common.TextUtil;
import com.harry.videowatermark.model.VideoModel;
import com.harry.videowatermark.service.VideoService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @author honghh
 * Date 2020/08/18 14:42
 * Copyright (C) Harry技术
 */
@Service
public class WeiShiServiceImpl implements VideoService {

    @Override
    public VideoModel parseUrl(String strUrl) {
        VideoModel videoModel = new VideoModel();

        try {
            // 获取 短链接 URL
            String url = TextUtil.extractUrl(strUrl);
            Matcher matcher = Pattern.compile("\\w{17}").matcher(url);
            if (matcher.find()) {
                String feedId = matcher.group(0);
                url = "https://h5.qzone.qq.com/webapp/json/weishi/WSH5GetPlayPage?feedid=" + feedId;
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                Response response = okHttpClient.newCall(request).execute();
                String result = response.body().string();
                System.out.println(result);

                videoModel.setName(JsonUtil.getJsonValue(result, "data.feeds[0].material_desc"));
                videoModel.setPlayAddr(JsonUtil.getJsonValue(result, "data.feeds[0].video_url"));
                videoModel.setCover(JsonUtil.getJsonValue(result, "data.feeds[0].images[0].url"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoModel;
    }

    public static void main(String[] args) {
//        System.out.println(new WeiShiServiceImpl().parseUrl("https://h5.weishi.qq.com/weishi/feed/7jhhxy88J1Kb72PIt/wsfeed?wxplay=1&id=7jhhxy88J1Kb72PIt&spid=1596273090628829&qua=v1_and_weishi_8.1.0_588_212011431_d&chid=100081014&pkg=3670&attach=cp_reserves3_1000370011"));
        System.out.println(new WeiShiServiceImpl().parseUrl("论当代大龄女青年的枯燥生活…>>https://h5.weishi.qq.com/weishi/feed/7dPuvopGI1KitOMwL/wsfeed?wxplay=1&id=7dPuvopGI1KitOMwL&spid=9028756019097772032&qua=v1_iph_weishi_8.3.1_397_app_a&chid=100081014&pkg=3670&attach=cp_reserves3_1000370011"));
    }

}
