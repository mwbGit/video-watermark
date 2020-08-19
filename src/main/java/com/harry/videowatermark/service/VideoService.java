package com.harry.videowatermark.service;

import com.harry.videowatermark.model.VideoModel;

/**
 * ClassName: VideoService
 * Description:
 *
 * @author honghh
 * Date 2020/08/18 14:42
 * Copyright (C) Harry技术
 */
public interface VideoService {
    /**
     * 视频路径
     *
     * @param url
     * @return
     */
    VideoModel parseUrl(String url);
}
