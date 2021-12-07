package com.harry.videowatermark.model;

import com.harry.videowatermark.utils.IdSecurityUtils;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2020/12/4
 */
@Data
@ToString
@Table(name = "file_info")
public class FileInfo {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id")
    private long id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "add_time")
    private Date addTime;
    @Column(name = "ip")
    private String ip;
    @Column(name = "region")
    private String region;

    public String getIdDesc() {
        return IdSecurityUtils.encryptId(id);
    }


    public String getAddTimeDesc() {
        return DateFormatUtils.format(addTime, "yyyy-MM-dd HH:mm:ss");
    }
}