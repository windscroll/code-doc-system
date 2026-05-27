package com.code.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@TableName("solution")
public class Solution {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long docId;
    private String codeContent;
    private String language;
    private Integer copyCount;
    private Integer sort;
    private LocalDateTime createTime;
}