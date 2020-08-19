package com.harry.videowatermark.common;

import com.google.gson.Gson;
import com.harry.videowatermark.bean.ApiResult;
import com.harry.videowatermark.model.VideoModel;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;


/**
 * Description:
 *
 * @author honghh
 * Date 2020/08/18 14:42
 * Copyright (C) Harry技术
 */
public class TextUtil {
    private static Logger logger = LoggerFactory.getLogger(TextUtil.class);

    public static final String UA = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_1_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16D57 Version/12.0 Safari/604.1";
    public static final String API = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=";

    /**
     * 取两个文本之间的文本值
     *
     * @param text
     * @param left
     * @param right
     * @return
     */
    public static String getSubString(String text, String left, String right) {
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }

    /**
     * 抽取URL
     *
     * @param urlStr
     * @return
     */
    public static String extractUrl(String urlStr) {
        if (StringUtils.isBlank(urlStr) || !urlStr.contains("http")) {
            return StringUtils.EMPTY;
        }

        String newUrlStr = urlStr.substring(urlStr.substring(0, urlStr.indexOf("http")).length());

        for (String string : newUrlStr.split(" ")) {
            if (string.startsWith("http")) {
                return string;
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * 短连接转换成长地址
     *
     * @param shortUrl
     * @return
     * @throws IOException
     */
    public static String convertUrl(String shortUrl) throws IOException {
        URL inputURL = new URL(shortUrl);
        URLConnection urlConn = inputURL.openConnection();
        logger.info("Short URL: {}", shortUrl);
        urlConn.getHeaderFields();
        String ans = urlConn.getURL().toString();
        logger.info("Original URL: {}", ans);
        return ans;
    }

    /**
     * 从路径中提取itemId
     *
     * @param url
     * @return
     */
    public static String parseItemIdFromUrl(String url) {
        String ans = "";
        String[] strings = url.split("/");
        // after video.
        for (String string : strings) {
            if (StringUtils.isNumeric(string)) {
                return string;
            }
        }
        return ans;
    }

    /**
     * 解析抖音API获取视频结果.
     *
     * @param itemId
     * @return
     * @throws Exception
     */
    public static ApiResult requestToApi(String itemId) throws Exception {
        String url = API + itemId;
        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();
        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", UA);

        int responseCode = httpClient.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            //print result
            return new Gson().fromJson(response.toString(), ApiResult.class);
        }
    }

    /**
     * 下载短视频 抖音
     *
     * @param result
     * @param fileUrl
     * @throws IOException
     */
    public static void downloadVideo(VideoModel result, String fileUrl) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Connection", "keep-alive");
        headers.put("Host", "aweme.snssdk.com");
        headers.put("User-Agent", UA);
        BufferedInputStream in = Jsoup.connect(result.getPlayAddr())
                .headers(headers).timeout(10000)
                .ignoreContentType(true).execute().bodyStream();

        File file = new File(fileUrl + "/" + result.getName() + ".mp4");
        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        int b;
        while ((b = in.read()) != -1) {
            out.write(b);
        }
        out.close();
        in.close();

        logger.info("save file to ==> {}", file.getAbsolutePath());
    }

    public static void main(String[] args) {
        String urlStr =
                "";


        //huoshan
        // "光明日报在火山分享了视频，快来围观！传送门戳我>>https://share.huoshan.com/hotsoon/s/E3D2wFb2Va8/ 复制此链接，打开【抖音火山版】，直接观看视频~";

        //xigua
        // "https://v.ixigua.com/Jr1JhYm/";

        //pipixia
        // "https://h5.pipix.com/s/JMoWtWk/";

        //kuaishou
        // "王者荣耀：暴躁程序员还能变身？典韦星元上新！ #王者荣耀 #小妲己的爆料时间 https://v.kuaishou.com/5jFrsH 复制此链接，打开【快手App】直接观看！";

        //douyin
        // "待葡萄熟了的时候都来我家吃葡萄#农村生活 #自家果园 #绿色农业 #三农  https://v.douyin.com/JMPxDMk/ 复制此链接，打开【抖音短视频】，直接观看视频！";

        //zuiyou
        // "#最右#分享一条有趣的内容给你，不好看算我输。请戳链接>> https://share.izuiyou.com/hybrid/share/post?pid=161816638&zy_to=applink&share_count=1&m=edeff28c14f6f5abe4a19132d6eb13c9&d=424645e5e03ce0f18021c1766c8ea257ca2cd1100b1030381072823502f28011&app=zuiyou&recommend=top_ctr&name=use_push_only&title_type=post";


        System.out.println(extractUrl(urlStr));
    }

}
