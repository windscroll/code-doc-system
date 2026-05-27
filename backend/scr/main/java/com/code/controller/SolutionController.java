package com.code.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.code.common.IpUtil;
import com.code.common.Result;
import com.code.entity.CopyLog;
import com.code.entity.Solution;
import com.code.mapper.CopyLogMapper;
import com.code.mapper.SolutionMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/solution")
@CrossOrigin(origins = "*", allowCredentials = "true")
public class SolutionController {
    
    @Resource
    private SolutionMapper solutionMapper;
    
    @Resource
    private CopyLogMapper copyLogMapper;
    
    @PostMapping("/copy/{id}")
    public Result<Map<String, Object>> copy(@PathVariable Long id, HttpServletRequest request) {
        String ip = IpUtil.getIp(request);
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        
        // 检查1小时内同一IP是否已经统计过
        LambdaQueryWrapper<CopyLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CopyLog::getSolutionId, id)
               .eq(CopyLog::getIp, ip)
               .gt(CopyLog::getCreateTime, oneHourAgo);
        
        Long count = copyLogMapper.selectCount(wrapper);
        
        if (count == 0) {
            // 新增复制日志
            CopyLog log = new CopyLog();
            log.setSolutionId(id);
            log.setIp(ip);
            log.setCreateTime(LocalDateTime.now());
            copyLogMapper.insert(log);
            
            // 更新题解复制次数
            solutionMapper.incrCopy(id);
        }
        
        // 返回最新的题解信息
        Solution solution = solutionMapper.selectById(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("copyCount", solution.getCopyCount());
        result.put("solutionId", solution.getId());
        
        return Result.success(result);
    }
    
    @GetMapping("/{id}")
    public Result<Solution> getById(@PathVariable Long id) {
        Solution solution = solutionMapper.selectById(id);
        return Result.success(solution);
    }
}