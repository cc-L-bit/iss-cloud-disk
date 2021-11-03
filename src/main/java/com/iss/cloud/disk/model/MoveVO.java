package com.iss.cloud.disk.model;

import java.util.List;

public class MoveVO {

    private List<MyFile> myFiles;
    private MyFile newFile;


    public List<MyFile> getMyFiles() {
        return myFiles;
    }

    public void setMyFiles(List<MyFile> myFiles) {
        this.myFiles = myFiles;
    }

    public MyFile getNewFile() {
        return newFile;
    }

    public void setNewFile(MyFile newFile) {
        this.newFile = newFile;
    }
}
