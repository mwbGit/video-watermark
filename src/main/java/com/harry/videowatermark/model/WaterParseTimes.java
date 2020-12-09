package com.harry.videowatermark.model;

import lombok.Data;
import lombok.ToString;
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
@Table(name = "water_parse_times")
public class WaterParseTimes {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id")
    private long id;
    @Column(name = "device_id")
    private String device_id;
    @Column(name = "app_version")
    private String app_version;
    @Column(name = "times")
    private int times;
    @Column(name = "create_time")
    private Date create_time;
    @Column(name = "update_time")
    private Date update_time;

}