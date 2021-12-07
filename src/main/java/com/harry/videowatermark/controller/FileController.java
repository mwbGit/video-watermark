package com.harry.videowatermark.controller;

import com.harry.videowatermark.mapper.FileInfoMapper;
import com.harry.videowatermark.model.FileInfo;
import com.harry.videowatermark.utils.IdSecurityUtils;
import com.harry.videowatermark.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 描述:
 * 文件上传下载服务
 * @author mengweibo@kanzhun.com
 * @create 2020/12/5
 */
@Slf4j
@Controller
@RequestMapping("/file")
public class FileController {
    private static final int FILE_MAX_IMG_SIZE = 50 * 1024 * 1024;
    private static final String ROOT_PATH = "/data/wwwroot/file/";
//    private static final String ROOT_PATH = "/data/file/";
    @Autowired
    private FileInfoMapper fileInfoMapper;

    @RequestMapping("/index.html")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/file_index");
        List<FileInfo> fileInfos = fileInfoMapper.selectLimit100();
        modelAndView.addObject("files", fileInfos);
        log.info("FileController.index into...");
        return modelAndView;
    }

    @PostMapping("/upload")
    public String upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        String ip = IpUtil.getIpAddr(request);
        log.info("upload ip={}", ip);
        if (file == null || file.getSize() == 0 || file.getSize() > FILE_MAX_IMG_SIZE) {
            return "redirect:/file/index.html";
        }
        try {
            String dateStr = DateFormatUtils.format(new Date(), "yyyyMMdd");
            String type = FilenameUtils.getExtension(file.getOriginalFilename());
            String rootPath = ROOT_PATH + dateStr;
            String path = rootPath + "/" + UUID.randomUUID().toString() + "." + type;
            File pathFile = new File(rootPath);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            Files.write(Paths.get(path), file.getBytes());
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(file.getOriginalFilename());
            fileInfo.setFilePath(path);
            fileInfo.setAddTime(new Date());
            fileInfo.setIp(ip);
            fileInfo.setRegion(IpUtil.getCityInfo(ip));
            fileInfoMapper.insert(fileInfo);
            log.info("FileController.upload ok ip={}, id={}", ip, fileInfo.getId());
        } catch (Exception e) {
            log.error("FileController.upload fail ip={}", ip);
        }
        return "redirect:/file/index.html";
    }

    /**
     * 文件下载
     */
    @RequestMapping(value = "/download")
    public ResponseEntity downLoad(String fileId, HttpServletRequest request, HttpServletResponse response) {
        FileInputStream file = null;
        FileChannel fischannel = null;
        long id = IdSecurityUtils.decryptId(fileId);
        if (id == 0) {
            return null;
        }
        try {
            FileInfo fileInfo = fileInfoMapper.selectByPrimaryKey(id);
            if (fileInfo == null) {
                return null;
            }
            //获取文件类型
            request.setCharacterEncoding("UTF-8");
            String mimeType = request.getServletContext().getMimeType(fileInfo.getFileName());
            //拼接文件
            File file1 = new File(fileInfo.getFilePath());
            file = new FileInputStream(file1);
            long fileLength = file1.length();
            //对文件名进行编码，解决中文名乱码
            String fileName1 = URLEncoder.encode(fileInfo.getFileName(), "UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            //指定文件下载，并解决中文名乱码
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + fileName1);//重点
            response.setHeader("Content-Length", String.valueOf(fileLength));
            response.setContentType(mimeType);
            fischannel = file.getChannel();
            int bufferSize = 2048;
            ByteBuffer buff = ByteBuffer.allocateDirect(4096);
            byte[] byteArr = new byte[bufferSize];
            int nGet;
            while (fischannel.read(buff) != -1) {
                buff.flip();
                while (buff.hasRemaining()) {
                    nGet = Math.min(buff.remaining(), bufferSize);
                    // read bytes from disk
                    buff.get(byteArr, 0, nGet);
                    // write bytes to output
                    response.getOutputStream().write(byteArr);
                }
                buff.clear();
            }
            log.info("FileController.downLoad fileId={}, ip={}", fileId, IpUtil.getIpAddr(request));
        } catch (Exception e) {
            log.error("FileController.downLoad fail fileId={}, ip={}", fileId, IpUtil.getIpAddr(request));
        } finally {
            if (fischannel != null) {
                try {
                    file.close();
                    fischannel.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
