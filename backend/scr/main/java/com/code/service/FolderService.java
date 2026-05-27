package com.code.service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.code.entity.Folder;
import com.code.entity.Document;
import com.code.entity.Solution;
import com.code.mapper.FolderMapper;
import com.code.mapper.DocumentMapper;
import com.code.mapper.SolutionMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class FolderService {
    @Resource FolderMapper folderMapper;
    @Resource DocumentMapper documentMapper;
    @Resource SolutionMapper solutionMapper;
    public List<Folder> tree() {
        List<Folder> folders = folderMapper.selectList(null);
        List<Document> docs = documentMapper.selectList(null);
        for (Document d : docs) {
            List<Solution> solutions = solutionMapper.selectList(
                new LambdaQueryWrapper<Solution>().eq(Solution::getDocId, d.getId()).orderByAsc(Solution::getSort)
            );
            d.setSolutions(solutions);
        }
        Map<Long, Folder> map = folders.stream().collect(Collectors.toMap(Folder::getId, f->f));
        List<Folder> root = new ArrayList<>();
        for (Folder f : folders) {
            f.setDocuments(docs.stream().filter(d->d.getFolderId().equals(f.getId())).collect(Collectors.toList()));
            if (f.getParentId() == 0) root.add(f);
            else {
                Folder p = map.get(f.getParentId());
                if (p.getChildren() == null) p.setChildren(new ArrayList<>());
                p.getChildren().add(f);
            }
        }
        return root;
    }
}