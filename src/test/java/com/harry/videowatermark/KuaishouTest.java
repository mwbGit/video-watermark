package com.harry.videowatermark;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.harry.videowatermark.common.JsonUtil;
import com.harry.videowatermark.model.VideoModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName: KuaishouTest
 * Description:
 *
 * @author honghh
 * Date 2020/08/19 15:31
 * Copyright (C) Harry技术
 */
public class KuaishouTest {
    /**
     * 方法描述: 快手解析下载视频
     *
     * @param url
     * @author tarzan
     * @date 2020年08月04日 10:33:40
     */
    public static void ksParseUrl(String url) {
        VideoModel videoModel = new VideoModel();
        HashMap<String, String> headers = MapUtil.newHashMap();
        headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Mobile Safari/537.36");
        String redirectUrl = HttpUtil.createGet(url).addHeaders(headers).execute().header("Location");
        String body = HttpUtil.createGet(redirectUrl).addHeaders(headers).execute().body();
        Document doc = Jsoup.parse(body);
        Elements scripts = doc.select("script");
        for (Element script : scripts) {
            if (script.html().contains("window.pageData=")) {
                String str = script.html().replace("\n", ""); //这里是为了解决 无法多行匹配的问题
                String result = str.replace("window.pageData= ", "");
                videoModel.setName(JsonUtil.getJsonValue(result, "video.caption"));
                videoModel.setPlayAddr(JsonUtil.getJsonValue(result, "video.srcNoMark"));
                videoModel.setCover(JsonUtil.getJsonValue(result, "video.shareCover"));
            }
        }

        downVideo(videoModel.getPlayAddr(), videoModel.getName(), "快手视频");
    }

    /**
     * 方法描述: 下载无水印视频方法
     *
     * @param httpUrl
     * @param title
     * @author tarzan
     * @date 2020年08月04日 10:34:09
     */
    public static void downVideo(String httpUrl, String title, String source) {
        String fileAddress = "/Users/honghh/Downloads" + "/" + source + "/" + new Date() + ".mp4";
        int byteRead;
        try {
            URL url = new URL(httpUrl);
            //获取链接
            URLConnection conn = url.openConnection();
            //输入流
            InputStream inStream = conn.getInputStream();
            //封装一个保存文件的路径对象
            File fileSavePath = new File(fileAddress);
            //注:如果保存文件夹不存在,那么则创建该文件夹
            File fileParent = fileSavePath.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            //写入文件
            FileOutputStream fs = new FileOutputStream(fileSavePath);
            byte[] buffer = new byte[1024];
            while ((byteRead = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteRead);
            }
            inStream.close();
            fs.close();
            System.out.println("\n-----视频保存路径-----\n" + fileSavePath.getAbsolutePath());
        } catch (IOException e) {
//            log.error(e.getMessage());
        }
    }


    public static void main(String[] args) {
        KuaishouTest.ksParseUrl("https://v.kuaishou.com/8lXgzr ");
    }
}
