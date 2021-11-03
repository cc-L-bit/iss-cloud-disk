package com.iss.cloud.disk.controller;

import com.alibaba.fastjson.JSON;
import com.iss.cloud.disk.model.*;
import com.iss.cloud.disk.service.FileService;
import com.iss.cloud.disk.tools.ObsTools;
import com.obs.services.model.ObsObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/file")
public class FileController {
    private final static Logger logger = Logger.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @GetMapping("/getFiles")
    @ResponseBody
    public Pagination<MyFile> getFiles(Pagination page) {
        logger.info("...getFiles  ...request params is {} " + JSON.toJSONString(page));
        return this.fileService.getFiles(page);
    }

    //显示回收站列表
    @GetMapping("/getRecoveryFiles")
    @ResponseBody
    public Pagination<MyFile> getRecoveryFiles(Pagination page) {
        return this.fileService.getRecoveryFiles(page);
    }

    //文件放到回收站
    @GetMapping(value = "/recoveryFile")
    @ResponseBody
    public ResultModel recoveryFile(@RequestParam int[] ids) {
        System.out.println(ids);
        this.fileService.RecoveryFiles(ids);
        return ResultModel.success();
    }

    @GetMapping("/getCollectFiles")
    @ResponseBody
    public Pagination<MyFile> getCollectFiles(Pagination page) {
        return this.fileService.getCollectFiles(page);
    }

    @GetMapping("/getFilesByPid")
    @ResponseBody
    public Pagination<MyFile> getFilesByPid(Pagination page) {
        return this.fileService.getFilesByPid(page);
    }

    @GetMapping("/getFolders")
    @ResponseBody
    public List<Node> getFolders(@RequestParam int pid, @SessionAttribute("user") User user) {
        return this.fileService.getFolders(pid, user.getId());
    }

    @PostMapping("/insertFile")
    @ResponseBody
    public ResultModel insertFile(@RequestParam("file") MultipartFile file, MyFile myFile) throws IOException {
        myFile.setFileSize(file.getSize());
        return this.fileService.insertFile(file, myFile);
    }

    @PostMapping("/mkdir")
    @ResponseBody
    public ResultModel mkdir(MyFile myFile) {
        return this.fileService.mkdir(myFile);
    }

/*
    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> download(@RequestParam String fileName, @RequestParam String filePath) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName, "UTF-8"));
        InputStream input = this.hdfsService.download(filePath);
        return new ResponseEntity<byte[]>(IOUtils.toByteArray(input), headers, HttpStatus.OK);
    }
 */

    @GetMapping("/download")
    public void download(@RequestParam("filePath") String filePath, @RequestParam("fileName") String fileName,
                         HttpServletResponse response) {
        try {
            // 设置响应头和客户端保存文件名
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            // 激活下载操作
            OutputStream os = response.getOutputStream();
            // 从OBS中查询实际文件对象
            ObsObject oobj = ObsTools.getObsObject(filePath);
            if (oobj != null) {
                InputStream input = oobj.getObjectContent();

                byte[] b = new byte[2048];// 循环写入输出流
                int length;
                while ((length = input.read(b)) > 0) {
                    os.write(b, 0, length);
                }
                os.close();
                input.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @GetMapping(value = "/deleteFile")
    @ResponseBody
    public ResultModel deleteFile(@RequestParam int[] ids) {
        System.out.println(ids);
        this.fileService.deleteFile(ids);
        return ResultModel.success();
    }

    @GetMapping(value = "/delete")
    @ResponseBody
    public ResultModel delete(@RequestParam int[] ids) {
        System.out.println(ids);
        this.fileService.delete(ids);
        return ResultModel.success();
    }

    @GetMapping(value = "/shareFile")
    @ResponseBody
    public ResultModel shareFile(@RequestParam int[] ids, @RequestParam int fileId, @SessionAttribute("user") User currentUser) {
        return this.fileService.share(ids, fileId, currentUser);
    }

    @GetMapping(value = "/acceptShare")
    @ResponseBody
    public ResultModel acceptShare(@RequestParam int id, @SessionAttribute("user") User currentUser) {
        return this.fileService.acceptShare(id, currentUser);
    }


    //收藏
    @GetMapping(value = "/collectFile")
    @ResponseBody
    public ResultModel collectFile(@RequestParam int id) {
        return this.fileService.collectFile(id);
    }
    //取消收藏
    @GetMapping(value = "/cancelCollect")
    @ResponseBody
    public ResultModel cancelCollect(@RequestParam int id) {

        return this.fileService.cancelCollect(id);
    }

    //还原回收站
    @GetMapping(value = "/cancelRecovery")
    @ResponseBody
    public ResultModel cancelRecovery(@RequestParam int id) {

        return this.fileService.cancelRecovery(id);
    }

    @GetMapping(value = "/rename")
    @ResponseBody
    public ResultModel rename(MyFile myFile, @RequestParam String newPath) {
        return this.fileService.rename(myFile, newPath);
    }

    @PostMapping(value = "/move")
    @ResponseBody
    public ResultModel move(@RequestBody MoveVO vo) {
        return this.fileService.move(vo);
    }


}
