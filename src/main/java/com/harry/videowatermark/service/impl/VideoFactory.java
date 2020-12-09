package com.harry.videowatermark.service.impl;

import com.harry.videowatermark.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 *
 * @author honghh
 * Date 2020/08/18 14:42
 * Copyright (C) Harry技术
 */
public class VideoFactory {

    private static Logger logger = LoggerFactory.getLogger(VideoFactory.class);
    //抖音
    public static final String DOUYIN = "douyin";
    public static final String IESDOUYIN = "iesdouyin";
    //火山
    public static final String HUOSHAN = "huoshan";
    //快手
    public static final String KUAISHOU = "kuaishou";
    public static final String KUAISHOU_APP = "kuaishouapp.com";
    public static final String GIFSHOW = "gifshow.com";
    public static final String CHENZHONGTECH = "chenzhongtech.com";
    //皮皮虾
    public static final String PIPIX = "pipix";
    //微视
    public static final String WEISHI = "weishi";
    //最右
    public static final String IZUIYOU = "izuiyou";
    //西瓜
    public static final String XIGUA = "xigua";
    //头条
    public static final String TOUTIAOIMG_COM = "toutiaoimg";
    public static final String TOUTIAOIMG_CN = "toutiaoimg.cn";


//                'bili'=>['b23.tv','www.bilibili.com'],
//                'livideo'=>['www.pearvideo.com'],
//                'meipai'=>['www.meipai.com'],
//                'momo'=>['immomo.com'],
//                'pipigaoxiao'=>['ippzone.com'],
//                'quanminggaoxiao'=>['longxia.music.xiaomi.com'],
//                'shuabao'=>['h5.shua8cn.com','m.shua8cn.com'],
//                'toutiao'=>['toutiaoimg.com','toutiaoimg.cn'],
//                'xiaokaxiu'=>['mobile.xiaokaxiu.com'],
//                'xigua'=>['xigua.com'],
//                'weibo'=>['weibo.com','weibo.cn'],
//                'newweibo'=>['video.weibo.com/show'],
//                'miaopai'=>['miaopai.com'],
//                'qqvideo'=>['m.v.qq.com'],

    public static VideoService getVideo(String type)
            throws InstantiationException, IllegalAccessException {

        if (type.contains(DOUYIN) || type.contains(IESDOUYIN)) {

            return DouyinServiceImpl.class.newInstance();

        } else if (type.contains(HUOSHAN)) {

            return HuoShanServiceImpl.class.newInstance();

        } else if (type.contains(KUAISHOU) || type.contains(GIFSHOW) || type.contains(CHENZHONGTECH) || type.contains(KUAISHOU_APP)) {

            return KuaiShouServiceImpl.class.newInstance();

        } else if (type.contains(PIPIX)) {

            return PiPiXServiceImpl.class.newInstance();

        } else if (type.contains(WEISHI)) {

            return WeiShiServiceImpl.class.newInstance();

        } else if (type.contains(IZUIYOU)) {

            return ZuiYouServiceImpl.class.newInstance();

        } else if (type.contains(TOUTIAOIMG_CN) || type.contains(TOUTIAOIMG_COM)) {

            return TouTiaoServiceImpl.class.newInstance();

        } else if (type.contains(XIGUA)) {

            return XiGuaServiceImpl.class.newInstance();

        } else {
            logger.error("哎呀！找不到相应的实例化类啦！");
            return null;
        }

    }
}
