package com.harry.videowatermark.service.impl;

import cn.hutool.http.HttpUtil;
import com.harry.videowatermark.common.JsonUtil;
import com.harry.videowatermark.common.TextUtil;
import com.harry.videowatermark.model.VideoModel;
import com.harry.videowatermark.service.VideoService;
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
public class DouyinServiceImpl implements VideoService {

    private static Logger logger = LoggerFactory.getLogger(DouyinServiceImpl.class);
    public static final String API = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=";

    @Override
    public VideoModel parseUrl(String strUrl) {
        VideoModel videoModel = new VideoModel();

        try {
            // 获取 短链接 URL
            String shortUrl = TextUtil.extractUrl(strUrl);

            // 获取重定向Url
            String redirectUrl = TextUtil.redirectUrl(shortUrl);

            // 获取 itemId
            String itemId = TextUtil.parseItemIdFromUrl(redirectUrl);

            // 获取接口数据
            String result = HttpUtil.createGet(API + itemId).addHeaders(TextUtil.headers()).execute().body();

            //此处的视频地址为带水印的地址，需要将地址中的playwm替换为play 在浏览器中打开将请求的User-Agent修改为手机
            String playAddr = JsonUtil.getJsonValue(result, "item_list[0].video.play_addr.url_list[0]");
            videoModel.setName(JsonUtil.getJsonValue(result, "item_list[0].desc"));
            videoModel.setPlayAddr(playAddr.replace("playwm", "play"));
            videoModel.setCover(JsonUtil.getJsonValue(result, "item_list[0].video.cover.url_list[0]"));
            logger.info("解析地址：{},返回视频地址：{}", shortUrl, videoModel.getPlayAddr());
            return videoModel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new DouyinServiceImpl().parseUrl("https://v.douyin.com/JMv1b5M/"));
//        VideoModel videoModel = new DouyinServiceImpl().parseUrl("https://v.douyin.com/JMv1b5M/");
//        TextUtil.downloadVideo(videoModel, "/Users/honghh/Downloads/");

    }
}
