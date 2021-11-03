package com.iss.cloud.disk.service.impl;

import com.iss.cloud.disk.model.*;
import com.iss.cloud.disk.dao.FileDao;
import com.iss.cloud.disk.service.FileService;
import com.iss.cloud.disk.service.MessageService;
import com.iss.cloud.disk.tools.ObsTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileDao fileDao;

    @Autowired
    private MessageService messageService;

    @Override
    public Pagination<MyFile> getFiles(Pagination page) {
        int start = (page.getPageNum() - 1) * page.getPageSize();
        List<MyFile> rows = this.fileDao.getFiles(start, page.getPageSize(), page.getCurrentUser());
        page.setRows(rows);
        int total = this.fileDao.getCount(0, page.getCurrentUser(), 0);
        page.setTotal(total);
        return page;
    }

    @Override
    public Pagination<MyFile> getCollectFiles(Pagination page) {
        int start = (page.getPageNum() - 1) * page.getPageSize();
        List<MyFile> rows = this.fileDao.getCollectFiles(start, page.getPageSize(), page.getCurrentUser());
        page.setRows(rows);
        int total = this.fileDao.getCount(1, page.getCurrentUser(), 0);
        page.setTotal(total);
        return page;
    }

    @Override
    public Pagination<MyFile> getRecoveryFiles(Pagination page) {
        int start = (page.getPageNum() - 1) * page.getPageSize();
        List<MyFile> rows = this.fileDao.getRecoveryFiles(start, page.getPageSize(), page.getCurrentUser());
        page.setRows(rows);
        int total = this.fileDao.getCount(2, page.getCurrentUser(), 0);
        page.setTotal(total);
        return page;
    }

    @Override
    public ResultModel RecoveryFiles(int[] ids) {
        boolean delFlag = false;
        for (int id : ids) {
            MyFile file = this.fileDao.getFile(id); // 查询文件完整信息
            List<MyFile> subFileList = fileDao.getFilesByPid(0, 10, file.getId());
            if (!CollectionUtils.isEmpty(subFileList)) {
                delFlag = true;
                break;
            }
            this.fileDao.recoveryFile(id);
        }
            if (delFlag) {
                return ResultModel.error("先删除文件夹下的文件");
            }
            return ResultModel.success();
    }

    @Override
    public Pagination<MyFile> getFilesByPid(Pagination page) {
        int start = (page.getPageNum() - 1) * page.getPageSize();
        List<MyFile> rows = this.fileDao.getFilesByPid(start, page.getPageSize(), page.getPid());
        page.setRows(rows);
        int total = this.fileDao.getCount(3, page.getCurrentUser(), page.getPid());
        page.setTotal(total);
        return page;
    }

    @Override
    public List<Node> getFolders(int pid, int userId) {
        List<Node> nodes = this.fileDao.getFolders(pid, userId);
        for (Node node : nodes) {
            node.setTags(new String[]{node.getFilePath()});
            List<Node> children = this.getFolders(node.getId(), userId);
            if (children.size() > 0) {
                node.setNodes(children);
            }
        }
        return nodes;
    }

    @Override
    public Map<String, Integer> getCountByType(int userId) {
        Map<String, Integer> map = this.fileDao.getCountByType(userId);
        if (map == null) {
            map = new HashMap<String, Integer>();
            map.put("img", 0);
            map.put("doc", 0);
            map.put("music", 0);
            map.put("radio", 0);
        }
        return map;
    }

    @Override
    public ResultModel insertFile(MultipartFile file, MyFile myFile)  throws IOException {
        // 上传文件到 hdfs 中保存
        // boolean result = this.hdfsService.upload(myFile.getFilePath(), file.getInputStream());

        // 写入 OBS,上传文件到 OBS 中保存
        String mkdir = myFile.getFilePath();
        if (mkdir == null ||  mkdir.isEmpty()) {
            return ResultModel.error("filePath属性不能为空");
        }else{
            // /tfl/tangfengliang//Apache Drill.jpg    /tfl/ppp//bbb//ccc/
            if (mkdir.contains("//")){
                mkdir = mkdir.replace("//", "/");
                myFile.setFilePath(mkdir);
            }
        }

        boolean result = ObsTools.uploadFile(mkdir, file.getInputStream());

        // 将文件信息保存至数据库
        if (result) {
            myFile.setCreateTime(new Date());
            int flag = this.fileDao.insertFile(myFile);
            if (flag > 0) {
                return ResultModel.success();
            } else {
                return ResultModel.dbError();
            }
        }
        return ResultModel.obsError();
    }

    @Override
    public ResultModel mkdir(MyFile myFile) {
        String obsPath = myFile.getFilePath() + "/" + myFile.getFileName()+ "/"; // a/b/c/d

        // 2种情况： /tfl/ppp/   /tfl/ppp//bbb//ccc/
        if (myFile.getFileName() == null ||  myFile.getFileName().isEmpty()) {
            return ResultModel.error("文件夹名称不能为空");
        }else{
            // /tfl/ppp/   /tfl/ppp//bbb//ccc/
            if (obsPath.contains("//")){
                obsPath = obsPath.replace("//", "/");
            }
        }

       boolean result = ObsTools.uploadFile(obsPath,new ByteArrayInputStream(new byte[0]));
        if (result) {
            myFile.setFilePath(obsPath);
            myFile.setCreateTime(new Date());
            int flag = this.fileDao.insertFile(myFile);
            if (flag > 0) {
                return ResultModel.success();
            } else {
                return ResultModel.dbError();
            }
        }
        return ResultModel.obsError();
    }

    @Override
    public ResultModel deleteFile(int[] ids) {
        boolean delFlag = false;
        for (int id : ids) {
            MyFile file = this.fileDao.getFile(id); // 查询文件完整信息

            List<MyFile> subFileList = fileDao.getFilesByPid(0,10,file.getId());

            if (!CollectionUtils.isEmpty(subFileList)){
                delFlag = true;
                break;
            }

            // 先删除数据库记录
            int flag = this.fileDao.deleteFile(id);
            if (flag > 0) {
                // 数据记录删除成功，删除OBS记录  deleteObject
                boolean result = ObsTools.deleteObject(file.getFilePath());
            } else
                return ResultModel.dbError();
        }

        if (delFlag){
            return  ResultModel.error("先删除文件夹下的文件");
        }

        return  ResultModel.success();
    }

    @Override
    public ResultModel delete(int[] ids) {
        boolean delFlag = false;
        for (int id : ids) {
            MyFile file = this.fileDao.getFile(id); // 查询文件完整信息
            List<MyFile> subFileList = fileDao.getFilesByPid(0,10,file.getId());
            if (!CollectionUtils.isEmpty(subFileList)){
                delFlag = true;
                break;
            }
            // 先删除数据库记录
            int flag = this.fileDao.delete(id);
            if (flag > 0) {
                // 数据记录删除成功，删除OBS记录  deleteObject
                boolean result = ObsTools.deleteObject(file.getFilePath());
            } else
                return ResultModel.dbError();
        }
        if (delFlag){
            return  ResultModel.error("先删除文件夹下的文件");
        }
        return  ResultModel.success();
    }


    @Override
    public ResultModel share(int[] ids, int fileId, User currentUser) {
        Message[] messages = new Message[ids.length];
        for (int i = 0; i < ids.length; i++) {
            String content = currentUser.getUsername() + " send a file to you, #Click to view ";
            messages[i] = new Message(content, currentUser, new User(ids[i]), new MyFile(fileId));
        }
        return this.messageService.insert(messages);
    }

    @Override
    public ResultModel acceptShare(int id, User user) {
        MyFile myFile = this.fileDao.getFile(id);
        String newPath = user.getPath() + myFile.getFileName();
        boolean result = true;
        //this.hdfsService.copy(myFile.getFilePath(), newPath);
        if (result) {
            myFile.setPid(0);
            myFile.setFilePath(newPath);
            myFile.setUser(user);
            myFile.setCreateTime(new Date());
            int flag = this.fileDao.insertFile(myFile);
            if (flag > 0) {
                return ResultModel.success();
            } else {
                //this.hdfsService.delete(myFile.getFilePath());
                return ResultModel.dbError();
            }
        }
        return ResultModel.hdfsError();
    }


    @Override
    public ResultModel collectFile(int id) {
        return this.fileDao.collectFile(id) > 0 ? ResultModel.success() : ResultModel.error();
    }

    @Override
    public ResultModel cancelCollect(int id) {
        return this.fileDao.cancelCollect(id) > 0 ? ResultModel.success() : ResultModel.error();
    }

    @Override
    public ResultModel cancelRecovery(int id) {
        return this.fileDao.cancelRecovery(id) > 0 ? ResultModel.success() : ResultModel.error();
    }


    @Override
    public ResultModel rename(MyFile myFile, String newPath) {
        /*
        boolean hdfsFlag = this.hdfsService.exists(newPath);
        boolean dbFlag = this.fileDao.exists(newPath) > 0;
        if (hdfsFlag && dbFlag) {
            return ResultModel.error("操作失败, 文件名已存在 !");
        }
        boolean result = this.hdfsService.rename(myFile.getFilePath(), newPath);
        if (!result) {
            return ResultModel.hdfsError();
        }
        // 保留旧 filepath 用于还原 HDFS 文件
        String oldPath = myFile.getFilePath();
        // 构造新的文件对象
        myFile.setFilePath(newPath);
        myFile.setFileName(newPath.substring(newPath.lastIndexOf("/") + 1, newPath.length()));
        if (this.fileDao.rename(myFile) > 0) {
            // 如果是文件夹，那么需要修改该文件夹下的子文件文件路径
            if (myFile.getFileType() == 5) {
                this.fileDao.renameByFilePath(oldPath, newPath);
            }
            return ResultModel.success();
        } else {
            // 数据库文件修改失败，则还原 HDFS 文件
            this.hdfsService.rename(myFile.getFilePath(), oldPath);
            return ResultModel.dbError();
        }
        */

        return ResultModel.success();
    }

    @Override
    public ResultModel move(MoveVO vo) {
        List<String> failNames = new ArrayList<String>();
        for (MyFile file : vo.getMyFiles()) {
            file.setPid(vo.getNewFile().getId());
            ResultModel result = this.rename(file, vo.getNewFile().getFilePath() + "/" + file.getFileName());
            if (!result.isOperate()) {
                failNames.add(file.getFileName());
            }
        }
        return failNames.size() == 0 ? ResultModel.success() : ResultModel.error("The following data operation failed: ", failNames.toString());
    }

}
