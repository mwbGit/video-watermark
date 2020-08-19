package com.harry.videowatermark.service.impl;

import com.harry.videowatermark.bean.ApiResult;
import com.harry.videowatermark.bean.ItemList;
import com.harry.videowatermark.common.TextUtil;
import com.harry.videowatermark.model.VideoModel;
import com.harry.videowatermark.service.VideoService;
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


    @Override
    public VideoModel parseUrl(String strUrl) {
        try {
            String shortUrl = TextUtil.extractUrl(strUrl);
            String oriUrl = TextUtil.convertUrl(shortUrl);
            String itemId = TextUtil.parseItemIdFromUrl(oriUrl);
            ApiResult apiBean = TextUtil.requestToApi(itemId);
            VideoModel videoModel = new VideoModel();
            ItemList item = apiBean.getItemList().get(0);
            String originVideoUrl = item.getVideo().getPlayAddr().getUrlList().get(0);
            // 去水印视频转换.
            videoModel.setName(item.getShareInfo().getShareTitle());
            videoModel.setPlayAddr(originVideoUrl.replace("playwm", "play"));
            videoModel.setMusicUrl(item.getMusic().getPlayUrl().getUri());
			// videoModel.setCover(cover);
            return videoModel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void main(String[] args) throws IOException {
        VideoModel videoModel = new DouyinServiceImpl().parseUrl("https://v.douyin.com/JMv1b5M/");
        TextUtil.downloadVideo(videoModel, "/Users/honghh/Downloads/");

    }
}
