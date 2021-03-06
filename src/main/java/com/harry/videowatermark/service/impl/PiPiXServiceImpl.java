package com.harry.videowatermark.service.impl;

import cn.hutool.http.HttpUtil;
import com.harry.videowatermark.common.JsonUtil;
import com.harry.videowatermark.common.TextUtil;
import com.harry.videowatermark.model.VideoModel;
import com.harry.videowatermark.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @author honghh
 * Date 2020/08/18 14:42
 * Copyright (C) Harry技术
 */
@Service
public class PiPiXServiceImpl implements VideoService {

    private static Logger logger = LoggerFactory.getLogger(PiPiXServiceImpl.class);
    public static final String API = "https://h5.pipix.com/bds/webapi/item/detail/?item_id=";

    @Override
    public VideoModel parseUrl(String strUrl) {
        VideoModel videoModel = new VideoModel();
        try {
            // 获取 短链接 URL
            String shortUrl = TextUtil.extractUrl(strUrl);
            // 获取重定向Url
            String redirectUrl = TextUtil.redirectUrl(shortUrl);
            // 获取 itemId
            String itemId = TextUtil.getSubString(redirectUrl, "/item/", "?app_id=");
            // 获取接口数据
            String result = HttpUtil.createGet(API + itemId).addHeaders(TextUtil.headers()).execute().body();

            videoModel.setName(JsonUtil.getJsonValue(result, "data.item.video.text"));
            videoModel.setPlayAddr(JsonUtil.getJsonValue(result, "data.item.video.video_fallback.url_list[0].url"));
            videoModel.setCover(JsonUtil.getJsonValue(result, "data.item.video.video_fallback.cover_image.url_list[0].url"));

            logger.info("解析地址：{},返回视频地址：{}", shortUrl, videoModel.getPlayAddr());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoModel;
    }

    public static void main(String[] args) {
        System.out.println(new PiPiXServiceImpl().parseUrl("https://h5.pipix.com/s/Jr9JxQK/"));
    }
}
