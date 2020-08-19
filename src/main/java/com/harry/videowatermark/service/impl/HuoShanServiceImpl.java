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
public class HuoShanServiceImpl implements VideoService {

	@Override
	public VideoModel parseUrl(String url) {
		// TODO Auto-generated method stub
		VideoModel videoModel=new VideoModel();
		/*HttpRequest request = HttpRequest.get(url);
		String res = request.body();
		System.out.println(res);*/
		try {
			OkHttpClient okHttpClient = new OkHttpClient();
			Request request = new Request.Builder().url(url).build();
			Response response = okHttpClient.newCall(request).execute();
			String result=response.body().string();
			System.out.println(result);
			result= TextUtil.getSubString(result, "create({d:", "});");
			String videoId= JsonUtil.getJsonValue(result, "video.uri");
			videoModel.setPlayAddr("http://hotsoon.snssdk.com/hotsoon/item/video/_playback/?video_id="+videoId);
			videoModel.setCover(JsonUtil.getJsonValue(result, "video.cover.url_list[0]"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return videoModel;
	}
	
	public static void main(String[] args) {
		System.out.println(new HuoShanServiceImpl().parseUrl("https://reflow.huoshan.com/hotsoon/s/th01P3Eu700/"));
	}
}
