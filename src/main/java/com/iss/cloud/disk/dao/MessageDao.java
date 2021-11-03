package com.iss.cloud.disk.dao;

import com.iss.cloud.disk.model.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageDao {

    int insert(Message msg);

    int update(int id);

    int delete(int id);

    int getCount(@Param("userId") int userId, @Param("isRead") int isRead);

    List<Message> getMessages(int userId, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    Message getMessage(int id);
}
