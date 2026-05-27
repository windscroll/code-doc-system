package com.code.controller;
import com.code.common.Result;
import com.code.entity.Folder;
import com.code.service.FolderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;
@RestController
@RequestMapping("/folder")
public class FolderController {
    @Resource FolderService folderService;
    @GetMapping("/tree")
    public Result<List<Folder>> tree() {
        return Result.success(folderService.tree());
    }
}