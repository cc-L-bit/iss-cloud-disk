package com.iss.cloud.disk.service;

import com.iss.cloud.disk.model.Message;
import com.iss.cloud.disk.model.Pagination;
import com.iss.cloud.disk.model.ResultModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageService {

    ResultModel insert(Message[] msgs);

    ResultModel update(int id);

    ResultModel delete(int id);

    int getCount(int userId, int isRead);

    Pagination<Message> getMessages(Pagination page);

    Message getMessage(int id);

}
