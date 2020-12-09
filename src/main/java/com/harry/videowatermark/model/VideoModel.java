package com.harry.videowatermark.model;

/**
 * ClassName: VideoModel
 * Description:
 *
 * @author honghh
 * Date 2020/08/18 14:42
 * Copyright (C) Harry技术
 */
public class VideoModel {
    /**
     * 视频名
     */
    private String name;
    /**
     * 视频背景
     */
    private String cover;
    /**
     * 无水印地址
     */
    private String playAddr;
    /**
     * 音频地址
     */
    private String musicUrl;

    public VideoModel() {
    }

    public VideoModel(String playAddr) {
        this.playAddr = playAddr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPlayAddr() {
        return playAddr;
    }

    public void setPlayAddr(String playAddr) {
        this.playAddr = playAddr;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    @Override
    public String toString() {
        return "VideoModel{" +
                "name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", playAddr='" + playAddr + '\'' +
                ", musicUrl='" + musicUrl + '\'' +
                '}';
    }
}
