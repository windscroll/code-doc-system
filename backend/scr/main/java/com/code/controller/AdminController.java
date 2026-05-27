package com.code.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.code.common.MD5Util;
import com.code.common.Result;
import com.code.entity.Admin;
import com.code.mapper.AdminMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource AdminMapper adminMapper;
    @PostMapping("/login")
    public Result login(@RequestBody Admin admin, HttpSession session) {
        Admin db = adminMapper.selectOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getUsername, admin.getUsername()));
        if (db == null) return Result.error("账号不存在");
        if (!db.getPassword().equals(MD5Util.encode(admin.getPassword()))) return Result.error("密码错误");
        session.setAttribute("admin", db);
        return Result.success();
    }
}