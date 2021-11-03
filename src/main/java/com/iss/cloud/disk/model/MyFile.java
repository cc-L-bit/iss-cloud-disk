package com.iss.cloud.disk.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class MyFile {

    // 主键
    private int id;
    // 父id
    private int pid;
    // 文件名
    private String fileName;
    // 文件大小
    private long fileSize;
    // 文件类型  1-image, 2-txt, 3-music, 4-movies, 5-folder, 6-其他
    private int fileType;
    // hdfs路径
    private String filePath;
    // 是否收藏
    private int isCollect;
    // 是否回收
    private int isRecovery;
    // 创建者
    private User user;
    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    public MyFile() {
    }

    public static MyFile mkdir(String fileName, String filePath, User user) {
        return new MyFile(0, fileName, 0, 5, filePath, 0, 0, user, new Date());
    }

    public MyFile(int id) {
        this.id = id;
    }

    public MyFile(int id, String fileName, int fileType, String filePath) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
    }

    public MyFile(int pid, String fileName, long fileSize, int fileType, String filePath, int isCollect, int isRecovery, User user, Date createTime) {
        this.pid = pid;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.filePath = filePath;
        this.isCollect = isCollect;
        this.isRecovery = isRecovery;
        this.user = user;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public int getIsRecovery() {
        return isRecovery;
    }

    public void setIsRecovery(int isRecovery) {
        this.isRecovery = isRecovery;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "MyFile{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", fileType=" + fileType +
                ", filePath='" + filePath + '\'' +
                ", isCollect=" + isCollect +
                ", isRecovery=" + isRecovery +
                ", user=" + user +
                ", createTime=" + createTime +
                '}';
    }
}
