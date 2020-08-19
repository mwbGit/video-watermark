package com.harry.videowatermark.controller;

import com.harry.videowatermark.model.VideoModel;
import com.harry.videowatermark.service.VideoService;
import com.harry.videowatermark.service.impl.VideoFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * ClassName: VideoController
 * Description:
 *
 * @author honghh
 * Date 2020/08/18 16:30
 * Copyright (C) Harry技术
 */
@RestController
@RequestMapping("video")
@CrossOrigin
public class VideoController {
    @GetMapping("parse")
    public VideoModel parse(String url) throws InstantiationException, IllegalAccessException {
        VideoService videoService = VideoFactory.getVideo(url);
        if (Objects.isNull(videoService)) {
            return new VideoModel();
        }
        return videoService.parseUrl(url);
    }
}
