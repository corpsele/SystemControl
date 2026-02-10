package com.systemcontrol.corpsele.systemcontrol.mvvm.model;

public class Search {
    /// 搜索内容
    private String keyWords;

    /// 回答内容
    private String strReply;


    public Search(String keyWords, String strReply) {
        this.keyWords = keyWords;
        this.strReply = strReply;
    }

    /// get
    public String getKeyWords() { return keyWords; }

    public String getStrReply() { return strReply; }



}