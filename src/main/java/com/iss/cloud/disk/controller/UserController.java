package com.iss.cloud.disk.controller;

import com.alibaba.fastjson.JSON;
import com.iss.cloud.disk.model.Pagination;
import com.iss.cloud.disk.model.ResultModel;
import com.iss.cloud.disk.model.Role;
import com.iss.cloud.disk.model.User;
import com.iss.cloud.disk.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private final static Logger logger = Logger.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    //更改头像信息
    @GetMapping("/updatePhotoInfo")
    @ResponseBody
    public String updatePhotoInfo(HttpServletRequest request,int id,String photo){
        User user = new User();
        user.setId(id);
        user.setPhoto(photo);
        userService.updatePhotoInfo(user);
        request.getSession().setAttribute("user",this.userService.getUser(id));
        return "success";
    }

    // 获得用户数据列表
    @GetMapping("/getUsers")
    @ResponseBody
    public Pagination<User> getUsers(Pagination page) {
        logger.info("...getUsers  ...request params is {} " + JSON.toJSONString(page));
        return this.userService.getUsers(page);
    }

    // 获得用户数据列表
    @GetMapping("/chooseUser")
    @ResponseBody
    public List<User> chooseUser(@SessionAttribute("user") User user) {
        return this.userService.chooseUser(user.getId());
    }

    // 获取指定ID用户信息
    @GetMapping("/getUserInfo")
    @ResponseBody
    public User getUserInfo(HttpServletRequest request) {
        return this.userService.getUser(Integer.parseInt(request.getParameter("id")));
    }

    // 新增用户信息
    @PostMapping("/insertUser")
    @ResponseBody
    public ResultModel insertUser(User user) {
        return this.userService.insertUser(user);
    }

    // 用户信息注册
    @PostMapping("/register")
    @ResponseBody
    public ResultModel register(User user) {
        return this.userService.register(user);
    }

    // 修改用户信息
    @PostMapping("/updateUser")
    @ResponseBody
    public ResultModel updateUser(User user) {
        return this.userService.updateUser(user);
    }

    // 删除用户数据
    @GetMapping("/delete")
    @ResponseBody
    public ResultModel delete(int[] ids) {
        logger.info("...delete  ...request params is {} " + JSON.toJSONString(ids));
        return this.userService.delete(ids);
    }

    @PostMapping(value = "/grantAuthorization")
    @ResponseBody
    public ResultModel grantAuthorization(@RequestParam Map<String, Object> reqMap, @SessionAttribute("user") User user) {
        logger.info("...grantAuthorization ...request params is {} " + JSON.toJSONString(reqMap));
        logger.info("...grantAuthorization.user   is {} " + JSON.toJSONString(user));

        reqMap.put("currentUserId", user.getId());
        reqMap.put("grantUserIds",  reqMap.get("ids").toString().split(","));
        return this.userService.grantAuthorization(reqMap);
    }
}
