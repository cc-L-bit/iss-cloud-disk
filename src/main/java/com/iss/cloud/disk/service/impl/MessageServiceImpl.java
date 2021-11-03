package com.iss.cloud.disk.service.impl;

import com.iss.cloud.disk.dao.MessageDao;
import com.iss.cloud.disk.model.Message;
import com.iss.cloud.disk.model.Pagination;
import com.iss.cloud.disk.model.ResultModel;
import com.iss.cloud.disk.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Override
    public ResultModel insert(Message[] msgs) {
        int result = 0;
        for (Message msg : msgs) {
            result += this.messageDao.insert(msg);
        }
        return result == msgs.length ? ResultModel.success("Your imaginary data has been inserted.") : ResultModel.dbError();
    }

    @Override
    public ResultModel update(int id) {
        int result = this.messageDao.update(id);
        return result > 0 ? ResultModel.success("Your imaginary data has been updated.") : ResultModel.dbError();
    }

    @Override
    public ResultModel delete(int id) {
        int result = this.messageDao.delete(id);
        return result > 0 ? ResultModel.success("Your imaginary data has been deleted.") : ResultModel.dbError();
    }

    @Override
    public int getCount(int userId, int isRead) {
        return this.messageDao.getCount(userId, isRead);
    }

    @Override
    public Pagination<Message> getMessages(Pagination page) {
        int start = (page.getPageNum() - 1) * page.getPageSize();
        List<Message> rows = this.messageDao.getMessages(page.getCurrentUser(), start, page.getPageSize());
        page.setRows(rows);
        int total = this.messageDao.getCount(page.getCurrentUser(), 3);
        page.setTotal(total);
        return page;
    }

    @Override
    public Message getMessage(int id) {
        return this.messageDao.getMessage(id);
    }

}
