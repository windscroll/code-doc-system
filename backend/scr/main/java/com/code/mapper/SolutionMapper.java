package com.code.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.code.entity.Solution;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
public interface SolutionMapper extends BaseMapper<Solution> {
    @Update("update solution set copy_count=copy_count+1 where id=#{id}")
    void incrCopy(@Param("id") Long id);
}