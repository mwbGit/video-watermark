package com.harry.videowatermark;

import com.google.gson.Gson;
import com.harry.videowatermark.bean.ApiResult;
import com.harry.videowatermark.bean.ItemList;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: DouyinTest
 * Description: 抖音视频去除水印工具
 *
 * @author honghh
 * Date 2020/08/18 14:51
 * Copyright (C) Harry技术
 */
public class DouyinTest {


    public static final String UA = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_1_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16D57 Version/12.0 Safari/604.1";
    public static final String API = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=";


    static class Result {
        public String videoUrl;
        public String musicUrl;
        public String name;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Result{");
            sb.append("videoUrl='").append(videoUrl).append('\'');
            sb.append(", musicUrl='").append(musicUrl).append('\'');
            sb.append(", name='").append(name).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }


    /**
     * 远程获取无水印视频地址.
     *
     * @param shareInfo
     * @return
     */
    public static ApiResult fetchVideoScheme(String shareInfo) {
        try {
            String shortUrl = extractUrl(shareInfo);
            String originUrl = convertUrl(shortUrl);
            String itemId = parseItemIdFromUrl(originUrl);
            ApiResult apiBean = requestToAPI(itemId);
            return apiBean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Result fetch(String shareInfo) {
        try {
            String shortUrl = extractUrl(shareInfo);
            String originUrl = convertUrl(shortUrl);
            String itemId = parseItemIdFromUrl(originUrl);
            ApiResult apiBean = requestToAPI(itemId);
            Result result = new Result();
            ItemList item = apiBean.getItemList().get(0);
            result.name = item.getShareInfo().getShareTitle();
            String originVideoUrl = item.getVideo().getPlayAddr().getUrlList().get(0);
            // 去水印视频转换.
            result.videoUrl = originVideoUrl.replace("playwm", "play");
            result.musicUrl = item.getMusic().getPlayUrl().getUri();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载抖音无水印视频到某个路径下.
     *
     * @param shareInfo
     * @param saveToFolder
     */
    public static void downloadVideo(String shareInfo, String saveToFolder) throws IOException {
        Result result = fetch(shareInfo);
        if (result != null) {
            Map<String, String> headers = new HashMap<>();
            headers.put("Connection", "keep-alive");
            headers.put("Host", "aweme.snssdk.com");
            headers.put("User-Agent", UA);
            BufferedInputStream in = Jsoup.connect(result.videoUrl)
                    .headers(headers).timeout(10000)
                    .ignoreContentType(true).execute().bodyStream();

            File file = new File(saveToFolder + "/" + result.name + ".mp4");
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            out.close();
            in.close();
            System.out.println("save file to " + file.getAbsolutePath());
        }
    }


    /**
     * 从路径中提取itemId
     *
     * @param url
     * @return
     */
    public static String parseItemIdFromUrl(String url) {
        // https://www.iesdouyin.com/share/video/6519691519585160455/?region=CN&mid=6519692104368098051&u_code=36fi3lehcdfb&titleType=title
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
     * 短连接转换成长地址
     *
     * @param shortURL
     * @return
     * @throws IOException
     */
    public static String convertUrl(String shortURL) throws IOException {
        URL inputURL = new URL(shortURL);
        URLConnection urlConn = inputURL.openConnection();
        System.out.println("Short URL: " + shortURL);
        urlConn.getHeaderFields();
        String ans = urlConn.getURL().toString();
        System.out.println("Original URL: " + ans);
        return ans;
    }

    /**
     * 抽取URL
     *
     * @param rawInfo
     * @return
     */
    public static String extractUrl(String rawInfo) {
        if (StringUtils.isBlank(rawInfo)) {
            return StringUtils.EMPTY;
        }
        for (String string : rawInfo.split(" ")) {
            if (string.startsWith("http")) {
                return string;
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * 解析抖音API获取视频结果.
     *
     * @param itemId
     * @return
     * @throws Exception
     */
    public static ApiResult requestToAPI(String itemId) throws Exception {
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
            ApiResult apiBean = new Gson().fromJson(response.toString(), ApiResult.class);
            return apiBean;
        }
    }



    private static void downloadV(Result result, String fileUrl) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Connection", "keep-alive");
        headers.put("Host", "aweme.snssdk.com");
        headers.put("User-Agent", UA);
        BufferedInputStream in = Jsoup.connect(result.videoUrl)
                .headers(headers).timeout(10000)
                .ignoreContentType(true).execute().bodyStream();

        File file = new File(fileUrl + "/" + result.name + ".mp4");
        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        int b;
        while ((b = in.read()) != -1) {
            out.write(b);
        }
        out.close();
        in.close();
        System.out.println("save file to " + file.getAbsolutePath());
    }


    private static Result videoResult(String msgFromDouyin) {
        return fetch(msgFromDouyin);
    }

    public static void main(String[] args) throws IOException {

        // 1. 获取短视频链接
        String msgFromDouyin = "(一)我打12360他竟告诉我不可以订票？ https://v.douyin.com/JME6Ax8/ 复制此链接，打开【抖音短视频】，直接观看视频！";

        // 2. 获取短视频路径
        Result result = DouyinTest.videoResult(msgFromDouyin);
        // 3.下载到本地
        DouyinTest.downloadV(result, "/Users/honghh/Downloads/");


        // 远程获取无水印视频地址
        ApiResult apiResult = DouyinTest.fetchVideoScheme(msgFromDouyin);
        System.out.println(apiResult.toString());

//        https://aweme.snssdk.com/aweme/v1/play/?video_id=v0300f000000bssukie32o9j46e2nfbg&ratio=720p&line=0

    }


   /* public static void main(String[] args) throws IOException {
      String url = " https://aweme.snssdk.com/aweme/v1/play/?video_id=v0300f000000bssukie32o9j46e2nfbg&ratio=720p&line=0";
        Map<String, String> headers = new HashMap<>();
        headers.put("Connection", "keep-alive");
        headers.put("Host", "aweme.snssdk.com");
        headers.put("User-Agent", UA);
        BufferedInputStream in = Jsoup.connect(url)
                .headers(headers).timeout(10000)
                .ignoreContentType(true).execute().bodyStream();

        File file = new File("/Users/honghh/Downloads/" + new Date() + ".mp4");
        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        int b;
        while ((b = in.read()) != -1) {
            out.write(b);
        }
        out.close();
        in.close();
        System.out.println("save file to " + file.getAbsolutePath());
    }*/
}
