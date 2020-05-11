package com.example.Bean;

import java.util.List;


public class CommentBean {

    private int total;
    private List<CommentDetailBean> list;

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setList(List<CommentDetailBean> list) {
        this.list = list;
    }

    public List<CommentDetailBean> getList() {
        return list;
    }

}

//}
