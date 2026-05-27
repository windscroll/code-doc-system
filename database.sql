-- 删除旧数据库（如果存在）
DROP DATABASE IF EXISTS code_doc_system;

-- 创建数据库
CREATE DATABASE code_doc_system DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE code_doc_system;

-- 文件夹表（树形结构核心）
CREATE TABLE folder (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    parent_id BIGINT DEFAULT 0 COMMENT '父文件夹ID，0为根目录',
    name VARCHAR(100) NOT NULL COMMENT '文件夹名称',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 文档表
CREATE TABLE document (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    folder_id BIGINT NOT NULL COMMENT '所属文件夹',
    title VARCHAR(200) NOT NULL COMMENT '文档标题',
    content TEXT COMMENT '题目描述',
    sort INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 题解表（一题多解）
CREATE TABLE solution (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    doc_id BIGINT NOT NULL COMMENT '所属文档',
    code_content TEXT NOT NULL COMMENT '代码内容',
    language VARCHAR(50) DEFAULT 'java' COMMENT '语言',
    copy_count INT DEFAULT 0 COMMENT '复制次数',
    sort INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 复制日志（防刷统计）
CREATE TABLE copy_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    solution_id BIGINT NOT NULL,
    ip VARCHAR(50) NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_ip_solution (ip, solution_id, create_time)
);

-- 管理员表
CREATE TABLE admin (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL COMMENT 'MD5加密',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ========== 初始化数据 ==========

-- 根文件夹
INSERT INTO folder (id, parent_id, name, sort) VALUES (1, 0, '📁 我的题库', 0);

-- 示例文件夹
INSERT INTO folder (parent_id, name, sort) VALUES (1, '📁 算法题库', 1);
INSERT INTO folder (parent_id, name, sort) VALUES (1, '📁 数据结构', 2);
INSERT INTO folder (parent_id, name, sort) VALUES (1, '📁 数据库', 3);

-- 示例文档
INSERT INTO document (folder_id, title, content, sort) VALUES (2, '两数之和', '## 题目描述\n给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target 的那 两个 整数，并返回它们的数组下标。\n\n示例：\n输入：nums = [2,7,11,15], target = 9\n输出：[0,1]', 1);

INSERT INTO document (folder_id, title, content, sort) VALUES (2, '反转链表', '## 题目描述\n给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。', 2);

-- 示例题解
INSERT INTO solution (doc_id, code_content, language, copy_count, sort) VALUES (1, 'class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        Map<Integer, Integer> map = new HashMap<>();\n        for (int i = 0; i < nums.length; i++) {\n            int complement = target - nums[i];\n            if (map.containsKey(complement)) {\n                return new int[]{map.get(complement), i};\n            }\n            map.put(nums[i], i);\n        }\n        return new int[0];\n    }\n}', 'java', 0, 1);

INSERT INTO solution (doc_id, code_content, language, copy_count, sort) VALUES (2, 'class Solution {\n    public ListNode reverseList(ListNode head) {\n        ListNode prev = null;\n        ListNode curr = head;\n        while (curr != null) {\n            ListNode nextTemp = curr.next;\n            curr.next = prev;\n            prev = curr;\n            curr = nextTemp;\n        }\n        return prev;\n    }\n}', 'java', 0, 1);

-- 管理员账号 admin / 123456（MD5加密）
INSERT INTO admin (username, password) VALUES ('admin', 'e10adc3949ba59abbe56e057f20f883e');