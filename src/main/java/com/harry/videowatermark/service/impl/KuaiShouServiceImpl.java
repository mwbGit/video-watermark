package com.harry.videowatermark.service.impl;

import com.harry.videowatermark.common.JsonUtil;
import com.harry.videowatermark.common.TextUtil;
import com.harry.videowatermark.model.VideoModel;
import com.harry.videowatermark.service.VideoService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @author honghh
 * Date 2020/08/18 14:42
 * Copyright (C) Harry技术
 */
@Service
public class KuaiShouServiceImpl implements VideoService {

    @Override
    public VideoModel parseUrl(String url) {
        // TODO Auto-generated method stub
        VideoModel videoModel = new VideoModel();
		/*HttpRequest request = HttpRequest.get(url);
		String res = request.body();
		System.out.println(res);*/
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = okHttpClient.newCall(request).execute();
            String result = response.body().string();
            String photoId = TextUtil.getSubString(result, "\\\"photoId\\\":\\\"", "\\\"");
            System.out.println(photoId);
            url = "https://api.kmovie.gifshow.com/rest/n/kmovie/app/photo/getPhotoById?WS&jjh_yqc&ws&photoId=" + photoId;
            request = new Request.Builder().url(url).build();
            response = okHttpClient.newCall(request).execute();
            result = response.body().string();
            System.out.println(result);
            videoModel.setName(JsonUtil.getJsonValue(result, "photo.caption"));
            videoModel.setPlayAddr(JsonUtil.getJsonValue(result, "photo.mainUrl"));
            videoModel.setCover(JsonUtil.getJsonValue(result, "photo.coverUrl"));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return videoModel;
    }

    public static void main(String[] args) {
        System.out.println(new KuaiShouServiceImpl().parseUrl(" https://v.kuaishou.com/5jFrsH"));
    }
}
