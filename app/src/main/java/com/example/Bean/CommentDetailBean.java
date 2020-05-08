package com.example.Bean;

import java.util.List;

/**
 * Created by moos on 2018/4/20.
 */

public class CommentDetailBean {
    private String user_name;
    private int ly_id;
    private String user_img;
    private String date;
    private String content;
    private List<ReplyDetailBean> list;

    public CommentDetailBean(String nickName, String content, String createDate, String user_img) {
        this.user_name = nickName;
        this.content = content;
        this.date = createDate;
        this.user_img = user_img;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setList(List<ReplyDetailBean> list) {
        this.list = list;
    }

    public void setLy_id(int ly_id) {
        this.ly_id = ly_id;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getLy_id() {
        return ly_id;
    }

    public List<ReplyDetailBean> getList() {
        return list;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getUser_img() {
        return user_img;
    }

    public String getUser_name() {
        return user_name;
    }
}
