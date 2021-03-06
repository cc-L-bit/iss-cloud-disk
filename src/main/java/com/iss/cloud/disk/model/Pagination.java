package com.iss.cloud.disk.model;

import java.util.ArrayList;
import java.util.List;

public class Pagination<T> {

    private int pageNum = 1;
    private int pageSize = 10;
    private int total;
    private List<T> rows = new ArrayList<T>();
    private int pid;
    private int currentUser;

    public Pagination() {
    }

    public Pagination(int total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(int currentUser) {
        this.currentUser = currentUser;
    }
}
