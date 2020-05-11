package com.example.Bean;


public class ReplyDetailBean {
    private String user_name;
    private int ly_id;
    private String content;
    private String date;

    public ReplyDetailBean(String nickName, String content) {
        this.user_name = nickName;
        this.content = content;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setLy_id(int ly_id) {
        this.ly_id = ly_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public int getLy_id() {
        return ly_id;
    }
}
