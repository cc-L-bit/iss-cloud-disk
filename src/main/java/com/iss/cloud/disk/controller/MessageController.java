package com.iss.cloud.disk.controller;

import com.iss.cloud.disk.model.Message;
import com.iss.cloud.disk.model.Pagination;
import com.iss.cloud.disk.model.ResultModel;
import com.iss.cloud.disk.model.User;
import com.iss.cloud.disk.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/msg")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/getMessages")
    @ResponseBody
    public Pagination<Message> getMessages(Pagination page, @SessionAttribute("user") User user) {
        page.setCurrentUser(user.getId());
        return this.messageService.getMessages(page);
    }

    @GetMapping("/update")
    @ResponseBody
    public ResultModel update(int id) {
        return this.messageService.update(id);
    }

}
