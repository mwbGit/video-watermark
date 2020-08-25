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
public class TouTiaoServiceImpl implements VideoService {
    private static Logger logger = LoggerFactory.getLogger(TouTiaoServiceImpl.class);

    public static final String PLAY_ADDR_API = "http://hotsoon.snssdk.com/hotsoon/item/video/_playback/?video_id=";

    @Override
    public VideoModel parseUrl(String strUrl) {
        VideoModel videoModel = new VideoModel();
        try {
            // 获取 短链接 URL
            String shortUrl = TextUtil.extractUrl(strUrl);
            // 获取 itemId
            String itemId = TextUtil.parseItemIdFromUrl(shortUrl);

            String result = HttpUtil.createGet("https://m.365yg.com/i" + itemId + "/info/?i=" + itemId)
                    .addHeaders(TextUtil.headers()).execute().body();

            String videoId = JsonUtil.getJsonValue(result, "data.video_id");

            videoModel.setName(JsonUtil.getJsonValue(result, "data.title"));
            videoModel.setPlayAddr(PLAY_ADDR_API + videoId);
            videoModel.setCover(JsonUtil.getJsonValue(result, "data.poster_url"));
            logger.info("解析地址：{},返回视频地址：{}", shortUrl, videoModel.getPlayAddr());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoModel;
    }

    public static void main(String[] args) {
        System.out.println(new TouTiaoServiceImpl().parseUrl("https://m.toutiaoimg.cn/group/6838410637660389896/?app=news_article&timestamp=1597992540&group_id=6838410637660389896"));
    }
}
