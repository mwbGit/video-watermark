package com.harry.videowatermark.service.impl;

import cn.hutool.http.HttpUtil;
import com.harry.videowatermark.common.JsonUtil;
import com.harry.videowatermark.common.TextUtil;
import com.harry.videowatermark.model.VideoModel;
import com.harry.videowatermark.service.VideoService;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @author honghh
 * Date 2020/08/18 14:42
 * Copyright (C) Harry技术
 */
@Service
public class ZuiYouServiceImpl implements VideoService {
    public static final String API = "https://share.izuiyou.com/api/post/detail";

    @Override
    public VideoModel parseUrl(String strUrl) {
        VideoModel videoModel = new VideoModel();
        try {
            // 获取 短链接 URL
            String url = TextUtil.extractUrl(strUrl);

            // 获取 itemId
            String itemId = TextUtil.getSubString(url, "?pid=", "&zy_to=");

            String result = HttpUtil.post(API, "{\"pid\":" + itemId + "}");

            String id = JsonUtil.getJsonValue(result, "data.post.imgs[0].id");
            videoModel.setName(JsonUtil.getJsonValue(result, "data.post.content"));
            videoModel.setCover(JsonUtil.getJsonValue(result, "data.post.videos." + id + ".cover_urls[0]"));
            videoModel.setPlayAddr(JsonUtil.getJsonValue(result, "data.post.videos." + id + ".url"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoModel;
    }

    public static void main(String[] args) {
        System.out.println(new ZuiYouServiceImpl().parseUrl("#最右#分享一条有趣的内容给你，不好看算我输。请戳链接>> https://share.izuiyou.com/hybrid/share/post?pid=163566379&zy_to=applink&share_count=1&m=edeff28c14f6f5abe4a19132d6eb13c9&d=424645e5e03ce0f18021c1766c8ea257ca2cd1100b1030381072823502f28011&app=zuiyou&recommend=r0&name=n0&title_type=t0"));
    }
}
