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

    public static final String DOUYIN = "douyin.com";
    public static final String IESDOUYIN = "iesdouyin.com";

    public static final String HUOSHAN = "huoshan.com";

    public static final String KUAISHOU = "kuaishou.com";
    public static final String GIFSHOW = "gifshow.com";
    public static final String CHENZHONGTECH = "chenzhongtech.com";

    public static final String PIPIX = "pipix.com";

    public static final String WEISHI = "weishi.qq.com";
    public static final String IZUIYOU = "izuiyou.com";


    public static VideoService getVideo(String type)
            throws InstantiationException, IllegalAccessException {

        if (type.contains(DOUYIN) || type.contains(IESDOUYIN)) {

            return DouyinServiceImpl.class.newInstance();

        } else if (type.contains(HUOSHAN)) {

            return HuoShanServiceImpl.class.newInstance();

        } else if (type.contains(KUAISHOU) || type.contains(GIFSHOW) || type.contains(CHENZHONGTECH)) {

            return KuaiShouServiceImpl.class.newInstance();

        } else if (type.contains(PIPIX)) {

            return PiPiXServiceImpl.class.newInstance();

        } else if (type.contains(WEISHI)) {

            return WeiShiServiceImpl.class.newInstance();

        } else if (type.contains(IZUIYOU)) {

            return ZuiYouServiceImpl.class.newInstance();

        } else {
            logger.error("哎呀！找不到相应的实例化类啦！");
            return null;
        }

    }
}
