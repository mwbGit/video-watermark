package com.harry.videowatermark.controller;

import com.harry.videowatermark.common.TextUtil;
import com.harry.videowatermark.model.ApiResult;
import com.harry.videowatermark.model.VideoModel;
import com.harry.videowatermark.service.VideoService;
import com.harry.videowatermark.service.impl.VideoFactory;
import com.harry.videowatermark.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: VideoController
 * Description:
 *
 * @author honghh
 * Date 2020/08/18 16:30
 * Copyright (C) Harry技术
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/video")
public class VideoController {
    private static final String MD5_SAFE = "watermarksafe";

    @RequestMapping("/parse")
    public ApiResult parse(@RequestParam("url") String url, @RequestParam("sign") String sign, @RequestParam("source") String source) {
        log.info("VideoController.parse source={}, url={}, sign={},", source, url, sign);
        try {
            String shortUrl = TextUtil.extractUrl(url);
            if (shortUrl == null) {
                return ApiResult.failed("请输入正确地址");
            }
            if (!sign.equals(MD5Util.md5(url, MD5_SAFE))) {
                return ApiResult.failed("签名错误");
            }

            VideoService videoService = VideoFactory.getVideo(shortUrl);
            if (videoService != null) {
                VideoModel videoModel = videoService.parseUrl(shortUrl);
                if (videoModel != null) {
                    return ApiResult.success(videoModel);
                } else {
                    return ApiResult.failed("视频链接异常");
                }
            }
        } catch (Exception e) {
            log.error("VideoController.parse err url={}, sign={},e={}", url, sign, e);
        }
        return ApiResult.failed();
    }

    @RequestMapping("/parse/v2")
    public ApiResult parseV2(@RequestParam("url") String url, @RequestParam(value = "source", required = false) String source) {
        log.info("VideoController.parse err url={}, source={},", url, source);
        try {
            if ("aliyun".equals(source)) {
                return ApiResult.failed();
            }
            String shortUrl = TextUtil.extractUrl(url);
            if (shortUrl == null) {
                return ApiResult.failed("请输入正确地址");
            }

            VideoService videoService = VideoFactory.getVideo(shortUrl);
            if (videoService != null) {
                VideoModel videoModel = videoService.parseUrl(shortUrl);
                if (videoModel != null) {
                    return ApiResult.success(videoModel);
                } else {
                    return ApiResult.failed("视频链接异常");
                }
            }
        } catch (Exception e) {
            log.error("VideoController.parse err url={}, source={},e={}", url, source, e);
        }
        return ApiResult.failed();
    }

    public static void main(String[] args) {
        String str = TextUtil.extractUrl("论当代大龄女青年的枯燥生活…>>https://h5.weishi.qq.com/weishi/feed/7dPuvopGI1KitOMwL/wsfeed?wxplay=1&id=7dPuvopGI1KitOMwL&spid=9028756019097772032&qua=v1_iph_weishi_8.3.1_397_app_a&chid=100081014&pkg=3670&attach=cp_reserves3_1000370011");
        System.out.println(str);
    }

}
