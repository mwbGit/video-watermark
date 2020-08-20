package com.harry.videowatermark.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import com.harry.videowatermark.common.JsonUtil;
import com.harry.videowatermark.common.TextUtil;
import com.harry.videowatermark.model.VideoModel;
import com.harry.videowatermark.service.VideoService;
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
            // 获取 短链接 URL
            String shortUrl = TextUtil.extractUrl(strUrl);

            // 获取重定向Url
            String redirectUrl = TextUtil.redirectUrl(shortUrl);

            // 获取数据  这里返回的数据是一个界面 数据在 script window.pageData= 中  无水印的视频字段 srcNoMark
            String body = HttpUtil.createGet(redirectUrl).addHeaders(TextUtil.headers()).execute().body();
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
