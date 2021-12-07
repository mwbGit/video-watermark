package com.harry.videowatermark.service.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.harry.videowatermark.common.JsonUtil;
import com.harry.videowatermark.common.TextUtil;
import com.harry.videowatermark.model.VideoModel;
import com.harry.videowatermark.service.VideoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Description:
 *
 * @author honghh
 * Date 2020/08/18 14:42
 * Copyright (C) Harry技术
 */
@Service
public class HaokanServiceImpl implements VideoService {

    private static Logger logger = LoggerFactory.getLogger(HaokanServiceImpl.class);

    @Override
    public VideoModel parseUrl(String strUrl) {
        VideoModel videoModel = new VideoModel();
        try {
//            // 获取 短链接 URL
//            String shortUrl = TextUtil.extractUrl(strUrl);
//
//            // 获取重定向Url
//            String redirectUrl = TextUtil.redirectUrl(shortUrl);

            // 获取数据  这里返回的数据是一个界面 数据在 script window.pageData= 中  无水印的视频字段 srcNoMark
            String body = HttpUtil.createGet(strUrl).addHeaders(TextUtil.headers()).execute().body();
            Document doc = Jsoup.parse(body);
//            Elements scripts = doc.select("script");
            Elements scripts = doc.select("script[id=_page_data]");
            for (Element script : scripts) {
                if (script.html().contains("window.__PRELOADED_STATE__")) {
                    //这里是为了解决 无法多行匹配的问题
                    String str = script.html().replace("\n", "");
                    str = str.replace(";         document.querySelector('body').removeChild(document.querySelector('#_page_data'));", "");
                    String result = str.replace("window.__PRELOADED_STATE__ = ", "");
//                    videoModel.setName(JsonUtil.getJsonValue(result, "curVideoMeta.videoInfoExt.1080p.url"));
//                    String jsonObject = JsonUtil.getJsonValue(result, "curVideoMeta.videoInfoExt");
                    videoModel.setPlayAddr(JsonUtil.getJsonValue(result, "curVideoMeta.videoInfoExt.1080p.1080pUrlHttp"));
//                    videoModel.setPlayAddr(JsonUtil.getJsonValue(result, "curVideoMeta.playurl"));
//                    videoModel.setCover(JsonUtil.getJsonValue(result, "video.shareCover"));
                }
            }
            logger.info("解析地址：{},返回视频地址：{}", strUrl, videoModel.getPlayAddr());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoModel;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new HaokanServiceImpl().parseUrl("https://haokan.hao123.com/v?vid=17477013805703694859&pd=haokan_share&context=%7B%22cuid%22%3A%22lavfu0iuvigwaBuk0a2ri_uG28YAuHaO_uvS8guwHujca-iqjiSkaYipWup1iSRHHI8mA%22%7D"));

        System.out.println();

    }

}
