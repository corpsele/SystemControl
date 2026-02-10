package com.systemcontrol.corpsele.systemcontrol.mvvm.model;

/**
 * 工厂模式：负责创建 Note 对象
 * 封装初始化逻辑，例如数据清洗和自动计算优先级
 */
public class ApiModelFactory {
    public static ApiModel createApiModel(String title, String description, String apiKey, String url) {
        // 1. 数据清洗
        String cleanTitle = title.trim();
        if (cleanTitle.isEmpty()) {
            cleanTitle = "无标题笔记";
        }
        // 2. 业务逻辑：根据标题长度或关键词自动设置优先级
        int priority = 2; // 默认 Medium
        if (cleanTitle.contains("紧急") || cleanTitle.contains("!!!")) {
            priority = 1; // High
        } else if (cleanTitle.length() < 5) {
            priority = 3; // Low
        }
        String strApiKey = apiKey.trim();
        String strUrl = url.trim();
        // 3. 返回新对象 (ID 默认为 0，由数据库生成)
        return new ApiModel(0, cleanTitle, description.trim(), priority, strApiKey, strUrl);
    }
}
