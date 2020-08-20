package com.harry.videowatermark;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import com.harry.videowatermark.common.JsonUtil;
import com.harry.videowatermark.common.TextUtil;
import com.harry.videowatermark.model.VideoModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;

/**
 * ClassName: HuoshanTest
 * Description:
 *
 * @author honghh
 * Date 2020/08/20 14:28
 * Copyright (C) 洛阳乾发供应链管理有限公司
 */
public class HuoshanTest {
    public static final String API = "https://share.huoshan.com/api/item/info?item_id=";

    public static void huoshanParseUrl(String url) throws IOException {
        VideoModel videoModel = new VideoModel();
        HashMap<String, String> headers = MapUtil.newHashMap();
        headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Mobile Safari/537.36");
        String redirectUrl = HttpUtil.createGet(url).addHeaders(headers).execute().header("Location");

        String itemId = TextUtil.getSubString(redirectUrl, "item_id=", "&tag=");

        String result = HttpUtil.createGet(API + itemId).addHeaders(headers).execute().body();


        String playAddr = JsonUtil.getJsonValue(result, "data.item_info.url");
        videoModel.setName(JsonUtil.getJsonValue(result, "data.item_info.item_id"));
        videoModel.setPlayAddr(playAddr.replace("reflow", "source").replace("mark=2", "mark=0"));
        videoModel.setCover(JsonUtil.getJsonValue(result, "data.item_info.cover"));

        TextUtil.downloadVideo(videoModel, "/Users/honghh/Downloads/");
    }



    public static void main(String[] args) throws IOException {
        HuoshanTest.huoshanParseUrl("https://share.huoshan.com/hotsoon/s/75UUtJcnYa8/ ");
    }
}
