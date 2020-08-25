package com.harry.videowatermark;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import com.harry.videowatermark.common.JsonUtil;
import com.harry.videowatermark.common.TextUtil;
import com.harry.videowatermark.model.VideoModel;

import java.io.IOException;
import java.util.HashMap;

/**
 * ClassName: XiguaTest
 * Description:
 *
 * @author honghh
 * Date 2020/08/21 14:57
 * Copyright (C)
 */
public class XiguaTest {

    public static final String PLAY_ADDR_API = "http://hotsoon.snssdk.com/hotsoon/item/video/_playback/?video_id=";

    public static void ParseUrl(String url) throws IOException {
        VideoModel videoModel = new VideoModel();
        HashMap<String, String> headers = MapUtil.newHashMap();
        headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Mobile Safari/537.36");
        String redirectUrl = HttpUtil.createGet(url).addHeaders(headers).execute().header("Location");

        // 获取 itemId
        String itemId = TextUtil.parseItemIdFromUrl(redirectUrl);


        String result = HttpUtil.createGet("https://m.365yg.com/i" + itemId + "/info/?i=" + itemId).addHeaders(headers).execute().body();


        String video_id = JsonUtil.getJsonValue(result, "data.video_id");

        videoModel.setName(JsonUtil.getJsonValue(result, "data.title"));
        videoModel.setPlayAddr(PLAY_ADDR_API + video_id);
        videoModel.setCover(JsonUtil.getJsonValue(result, "data.poster_url"));

    }


    public static void main(String[] args) throws IOException {
        XiguaTest.ParseUrl("https://v.ixigua.com/JrHM5QM/");
    }
}
