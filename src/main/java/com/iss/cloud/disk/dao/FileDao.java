package com.iss.cloud.disk.dao;

import com.iss.cloud.disk.model.MyFile;
import com.iss.cloud.disk.model.Node;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface FileDao {

    int getCount(@Param("flag") int flag, @Param("userId") int userId, @Param("pid") int pid);

    List<MyFile> getFiles(@Param("start") Integer start, @Param("pageSize") Integer pageSize, @Param("userId") int userId);

    List<MyFile> getRecoveryFiles(@Param("start") Integer start, @Param("pageSize") Integer pageSize, @Param("userId") int userId);

    List<MyFile> getCollectFiles(@Param("start") Integer start, @Param("pageSize") Integer pageSize, @Param("userId") int userId);

    List<MyFile> getFilesByPid(@Param("start") Integer start, @Param("pageSize") Integer pageSize, @Param("pid") int pid);

    List<Node> getFolders(@Param("pid") int pid, @Param("userId") int userId);

    List<MyFile> getFilesByIds(int[] ids);

    MyFile getFile(int id);

    Map<String, Integer> getCountByType(int userId);

    int insertFile(MyFile file);

    int deleteFile(int id);

    //从回收站删除文件
    int delete(int id);

    int recoveryFile(int id);

    int collectFile(int id);

    int cancelCollect(int id);

    //还原回收站
    int cancelRecovery(int id);

    int exists(String path);

    int rename(MyFile file);

    int renameByFilePath(@Param("oldPath") String oldPath, @Param("newPath") String newPath);

}
