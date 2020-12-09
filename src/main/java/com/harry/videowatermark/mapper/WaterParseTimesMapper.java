package com.harry.videowatermark.mapper;

import com.harry.videowatermark.model.WaterParseTimes;
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
public interface WaterParseTimesMapper extends tk.mybatis.mapper.common.Mapper<WaterParseTimes> {


    @Select("Select id,times from water_parse_times where device_id=#{device_id}")
    WaterParseTimes selectByDevice_id(@Param("device_id") String device_id);
}
