package com.iss.cloud.disk.service;

import com.iss.cloud.disk.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


public interface FileService {

    /**
     * 获得文件列表
     */
    Pagination<MyFile> getFiles(Pagination page);

    /**
     * 获得文件列表
     */
    Pagination<MyFile> getCollectFiles(Pagination page);

    /**
     * 获得文件列表
     */
    Pagination<MyFile> getRecoveryFiles(Pagination page);

    //回收文件
    ResultModel RecoveryFiles(int[] ids);


    /**
     * 获得文件列表
     */
    Pagination<MyFile> getFilesByPid(Pagination page);

    List<Node> getFolders(int pid, int userId);

    Map<String, Integer> getCountByType(int userId);

    ResultModel insertFile(MultipartFile file, MyFile myFile) throws IOException;

    ResultModel mkdir(MyFile myFile);

    ResultModel deleteFile(int[] ids);

    ResultModel delete(int[] ids);

    ResultModel share(int[] ids, int fileId, User currentUser);

    ResultModel acceptShare(int id, User user);

    ResultModel collectFile(int id);

    ResultModel cancelCollect(int id);

    ResultModel cancelRecovery(int id);

    ResultModel rename(MyFile myFile, String newPath);

    ResultModel move(MoveVO vo);

}
