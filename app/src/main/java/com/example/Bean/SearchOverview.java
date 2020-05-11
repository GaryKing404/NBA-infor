package com.example.Bean;

public class SearchOverview {

    private String title;
    private String content;
    private int match_id;

    public SearchOverview(String title, String content, int match_id) {
        this.title = title;
        this.content = content;
        this.match_id = match_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public int getMatch_id() {
        return match_id;
    }

}
