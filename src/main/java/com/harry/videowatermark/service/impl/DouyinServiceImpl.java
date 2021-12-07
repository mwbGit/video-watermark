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
            String playAddr = JsonUtil.getJsonValue(result, "item_list[0].video.play_addr.url_list[0]").replace("playwm", "play");
            //再次重定向  获取地址
            videoModel.setName(JsonUtil.getJsonValue(result, "item_list[0].desc"));
//            videoModel.setPlayAddr(TextUtil.redirectUrl(playAddr));
            videoModel.setPlayAddr(playAddr);
            videoModel.setCover(JsonUtil.getJsonValue(result, "item_list[0].video.cover.url_list[0]"));
            logger.info("解析地址：{},返回视频地址：{}", shortUrl, videoModel.getPlayAddr());
            return videoModel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new DouyinServiceImpl().parseUrl("https://v.douyin.com/RG4wgAn/"));
//        VideoModel videoModel = new DouyinServiceImpl().parseUrl("https://v.douyin.com/JMv1b5M/");
//        TextUtil.downloadVideo(videoModel, "/Users/honghh/Downloads/");

//        String url = "https://aweme.snssdk.com/aweme/v1/play/?video_id=v0200fee0000bsts1akkfl9eg4gi63dg&ratio=720p&line=0";
//        // 获取重定向Url
//        String redirectUrl = TextUtil.redirectUrl(url);
//        System.out.println(redirectUrl);
        String url = "https://v6-x.douyinvod.com/7b814b1274e9a5658c0d4d071d3d02cb/61a717c1/video/tos/cn/tos-cn-ve-15-alinc2/30c853d944c8416cb9b676cba87ea755/?a=1128&amp;br=1067&amp;bt=1067&amp;cd=0%7C0%7C0&amp;ch=96&amp;cr=0&amp;cs=0&amp;cv=1&amp;dr=0&amp;ds=6&amp;er=&amp;ft=5f4rKJmmnPEC2Th7ThWwkXAGfdH.CloxtBZc&amp;l=20211201133532010212145216100B1241&amp;lr=all&amp;mime_type=video_mp4&amp;net=0&amp;pl=0&amp;qs=0&amp;rc=anB1Omg6ZjV3OTMzNGkzM0ApZGg3PDc3PGRkN2ZpNTc5ZWcpaHV2fWVuZDFwekAvNmcycjRnY2JgLS1kLS9zcy8yMzMyLV9fMWAtY2AvMjQ6Y29zYlxmK2BtYmJeYA%3D%3D&amp;vl=&amp;vr=" ;

//        String playAddr = url.replace("playwm", "play");

//        System.out.println(TextUtil.redirectUrl(playAddr));
    }
}
