package com.harry.videowatermark.mapper;

import com.harry.videowatermark.model.WaterOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2020/7/31
 */
@Mapper
public interface WaterOrderMapper extends tk.mybatis.mapper.common.Mapper<WaterOrder> {

    @Insert("insert ignore into water_order_detail (order_id, receipt_info) values (#{order_id}, #{receipt_info})")
    void insertDetail(@Param("order_id") long order_id, @Param("receipt_info") String receipt_info);

    @Select("Select id from water_order where device_id=#{device_id}")
    Long selectIdByDevice_id(@Param("device_id") String device_id);

    @Select("Select id,expires_date_ms as expires_date_ms, is_trial_period as is_trial_period,purchase_date_ms as purchase_date_ms from water_order where device_id=#{device_id}")
    WaterOrder selectByDevice_id(@Param("device_id") String device_id);
}
