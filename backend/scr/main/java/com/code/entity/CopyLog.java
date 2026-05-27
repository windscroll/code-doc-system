package com.code.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@TableName("copy_log")
public class CopyLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long solutionId;
    private String ip;
    private LocalDateTime createTime;
}