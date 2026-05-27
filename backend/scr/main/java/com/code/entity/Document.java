package com.code.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data
@TableName("document")
public class Document {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long folderId;
    private String title;
    private String content;
    private Integer sort;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private transient List<Solution> solutions;
}