package com.iss.cloud.disk.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Message {
    private String id;
    private String content;
    private User fromUser;
    private User toUser;
    private MyFile file;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    private int isRead;

    public Message() {
    }

    public Message(String content, User fromUser, User toUser, MyFile file) {
        this.content = content;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public MyFile getFile() {
        return file;
    }

    public void setFile(MyFile file) {
        this.file = file;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
}
