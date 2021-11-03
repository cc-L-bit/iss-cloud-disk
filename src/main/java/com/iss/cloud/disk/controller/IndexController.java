package com.iss.cloud.disk.controller;

import com.iss.cloud.disk.model.ResultModel;
import com.iss.cloud.disk.model.User;
import com.iss.cloud.disk.service.FileService;
import com.iss.cloud.disk.service.MessageService;
import com.iss.cloud.disk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private MessageService messageService;

    @RequestMapping("/")
    public String welcome() {
        return "/login";
    }


    @RequestMapping("/index")
    public String index(HttpServletRequest request, @SessionAttribute("user") User user) {
        request.getSession().setAttribute("user", this.userService.getUser(user.getId()));
        request.getSession().setAttribute("fileCount", this.fileService.getCountByType(user.getId()));
        request.getSession().setAttribute("messages", this.messageService.getCount(user.getId(), 0));
        return "/index";
    }

    @RequestMapping("/login")
    @ResponseBody
    public ResultModel login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = this.userService.login(username, password);
        if (user == null) {
            return ResultModel.error("Wrong username or password !");
        }
        request.getSession().setAttribute("user", user);
        request.getSession().setAttribute("fileCount", this.fileService.getCountByType(user.getId()));
        return ResultModel.success();
    }


    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession(false).invalidate();
        return "redirect:/";
    }

    @GetMapping("/forward")
    public String forward(@RequestParam("html") String html) {
        return "/view/" + html;
    }

    @GetMapping("/register")
    public String register() {
        return "/register";
    }
}
