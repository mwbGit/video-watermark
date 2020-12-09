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
@Table(name = "water_order_report")
public class WaterOrderReport {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id")
    private long id;
    //0 初始 1请求失败 2成功
    @Column(name = "status")
    private int status;
    @Column(name = "device_id")
    private String device_id;
    @Column(name = "receipt_data")
    private String receipt_data;
    @Column(name = "create_time")
    private Date create_time;
    @Column(name = "update_time")
    private Date update_time;

}