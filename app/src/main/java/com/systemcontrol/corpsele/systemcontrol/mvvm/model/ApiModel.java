package com.systemcontrol.corpsele.systemcontrol.mvvm.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Api")
public class ApiModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String apiKey;

    private String url;
    private int priority; // 1: High, 2: Medium, 3: Low
    // 构造函数设为私有，强制通过工厂创建
    public ApiModel(int id, String title, String description, int priority, String url, String apiKey) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.apiKey = apiKey;
        this.url = url;
    }
    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getPriority() { return priority; }
    // Setters (Room 需要 Setter)
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getApiKey() { return apiKey; }
    public void setUrl(String url) { this.url = url; }
    public String getUrl() { return url; }
}
