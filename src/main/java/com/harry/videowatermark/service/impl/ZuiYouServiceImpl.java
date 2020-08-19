package com.harry.videowatermark.service.impl;

import com.harry.videowatermark.common.JsonUtil;
import com.harry.videowatermark.model.VideoModel;
import com.harry.videowatermark.service.VideoService;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @author honghh
 * Date 2020/08/18 14:42
 * Copyright (C) Harry技术
 */
@Service
public class ZuiYouServiceImpl implements VideoService {

	@Override
	public VideoModel parseUrl(String url) {
		// TODO Auto-generated method stub
		VideoModel videoModel=new VideoModel();
		/*HttpRequest request = HttpRequest.get(url);
		String res = request.body();
		System.out.println(res);*/
		try {
			Matcher matcher = Pattern.compile("\\d{9}").matcher(url);
			if(matcher.find()) {
				String pid=matcher.group(0);
				url="https://share.izuiyou.com/api/post/detail";
				OkHttpClient okHttpClient = new OkHttpClient();
				RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{\"pid\":"+pid+"}");
				Request request = new Request.Builder().url(url).post(body).build();
				Response response = okHttpClient.newCall(request).execute();
				String result=response.body().string();
				System.out.println(result);
				String id= JsonUtil.getJsonValue(result, "data.post.imgs[0].id");
				 videoModel.setName(JsonUtil.getJsonValue(result, "data.post.content"));
				 videoModel.setCover(JsonUtil.getJsonValue(result, "data.post.videos."+id+".cover_urls[0]"));
				 videoModel.setPlayAddr(JsonUtil.getJsonValue(result, "data.post.videos."+id+".url"));
			}	 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return videoModel;
	}
	
	public static void main(String[] args) {
		System.out.println(new ZuiYouServiceImpl().parseUrl("https://h5.izuiyou.com/detail/122595613?zy_to=applink&to=applink"));
	}
}
