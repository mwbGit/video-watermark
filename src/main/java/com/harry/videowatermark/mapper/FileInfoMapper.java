package com.harry.videowatermark.mapper;

import com.harry.videowatermark.model.FileInfo;
import com.harry.videowatermark.model.WaterParseRecord;
import com.harry.videowatermark.model.WaterParseTimes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2020/7/31
 */
@Mapper
public interface FileInfoMapper extends tk.mybatis.mapper.common.Mapper<FileInfo> {

    @Select("Select id,file_name as fileName,add_time as addTime from file_info order by id desc limit 100")
    List<FileInfo> selectLimit100();

}
