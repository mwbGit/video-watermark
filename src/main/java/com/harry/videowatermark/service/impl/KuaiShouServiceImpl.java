package com.harry.videowatermark.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import com.harry.videowatermark.common.JsonUtil;
import com.harry.videowatermark.common.TextUtil;
import com.harry.videowatermark.model.VideoModel;
import com.harry.videowatermark.service.VideoService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Description:
 *
 * @author honghh
 * Date 2020/08/18 14:42
 * Copyright (C) Harry技术
 */
@Service
public class KuaiShouServiceImpl implements VideoService {

    @Override
    public VideoModel parseUrl(String strUrl) {
        VideoModel videoModel = new VideoModel();
        try {

            String shortUrl = TextUtil.extractUrl(strUrl);

            HashMap<String, String> headers = MapUtil.newHashMap();
            headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Mobile Safari/537.36");
            String redirectUrl = HttpUtil.createGet(shortUrl).addHeaders(headers).execute().header("Location");
            String body = HttpUtil.createGet(redirectUrl).addHeaders(headers).execute().body();
            Document doc = Jsoup.parse(body);

            Elements scripts = doc.select("script");
            for (Element script : scripts) {
                if (script.html().contains("window.pageData=")) {
                    //这里是为了解决 无法多行匹配的问题
                    String str = script.html().replace("\n", "");
                    String result = str.replace("window.pageData= ", "");
                    videoModel.setName(JsonUtil.getJsonValue(result, "video.caption"));
                    videoModel.setPlayAddr(JsonUtil.getJsonValue(result, "video.srcNoMark"));
                    videoModel.setCover(JsonUtil.getJsonValue(result, "video.shareCover"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoModel;
    }

    public static void main(String[] args) {
        System.out.println(new KuaiShouServiceImpl().parseUrl("https://v.kuaishou.com/8lXgzr"));
    }
}
