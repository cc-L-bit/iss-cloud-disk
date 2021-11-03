package com.iss.cloud.disk.service.impl;

import com.iss.cloud.disk.model.*;
import com.iss.cloud.disk.dao.UserDao;
import com.iss.cloud.disk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;



    @Override
    public User login(String username, String password) {
        User user = this.userDao.login(username, password);
        // 检查登录用户在 hdfs中是否存在个人目录
        if (user != null && !StringUtils.hasText(user.getPath())) {
            String path = "/" + username + "/";
            //this.hdfsService.mkdirs(path);
            user.setPath(path);
            this.userDao.updatePath(user);
        }
        return user;
    }

    @Override
    public Pagination<User> getUsers(Pagination page) {
        int start = (page.getPageNum() - 1) * page.getPageSize();
        List<User> rows = this.userDao.getUsers(start, page.getPageSize());
        page.setRows(rows);

        int total = this.userDao.getCount();
        page.setTotal(total);

        return page;
    }

    @Override
    public List<User> chooseUser(int id) {
        return this.userDao.chooseUser(id);
    }

    @Override
    public User getUser(int id) {
        return this.userDao.getUser(id);
    }

    @Override
    public ResultModel insertUser(User user) {
        user.setPath("/" + user.getUsername() + "/"); // 个人存储路径
        user.setSize(5 * 1024 * 1024 * 1024); // 5G 存储空间
        user.setUsedSize(0);
        user.setPhoto("avatar-1.jpg"); // 默认头像
        int result = this.userDao.insertUser(user);
        return result > 0 ? ResultModel.success("Your imaginary data has been inserted.") : ResultModel.dbError();
    }

    @Override
    public ResultModel register(User user) {
        user.setUserRole(new Role(1)); // 新用户注册的账号为普通用户
        user.setPath("/" + user.getUsername() + "/");
        int result = this.userDao.insertUser(user);
        return result > 0 ? ResultModel.success("Your imaginary data has been inserted.") : ResultModel.dbError();
    }

    @Override
    public ResultModel updateUser(User user) {
        int result = this.userDao.updateUser(user);
        return result > 0 ? ResultModel.success("Your imaginary data has been updated.") : ResultModel.dbError();
    }

    @Override
    public ResultModel delete(int[] ids) {
        int result = this.userDao.delete(ids);
        return result > 0 ? ResultModel.success("Your imaginary data has been deleted.") : ResultModel.dbError();

    }

    @Override
    public ResultModel grantAuthorization(Map<String, Object> reqMap) {
        int result = this.userDao.grantAuthorization(reqMap);
        return result > 0 ? ResultModel.success("Your imaginary data has been granted.") : ResultModel.dbError();
    }

    @Override
    public ResultModel updatePhotoInfo(User user) {
        int result = this.userDao.updatePhotoInfo(user);
        return result > 0 ? ResultModel.success("Your photo info has been update.") : ResultModel.dbError();
    }
}
