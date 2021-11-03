package com.iss.cloud.disk.model;

public class ResultModel<T> {

    public static final boolean SUCCESS = true;
    public static final boolean ERROR = false;

    /**
     * 操作是否成功
     */
    private boolean operate;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 结果数据
     */
    private T data;


    public ResultModel(boolean operate, String msg, T data) {
        this.operate = operate;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResultModel<T> success() {
        return success("操作成功 !", null);
    }

    public static <T> ResultModel<T> success(String msg) {
        return success(msg, null);
    }

    public static <T> ResultModel<T> success(String msg, T data) {
        return new ResultModel<T>(ResultModel.SUCCESS, msg, data);
    }

    public static <T> ResultModel<T> error() {
        return error("操作失败 !", null);
    }

    public static <T> ResultModel<T> dbError() {
        return error("数据库 操作失败 !", null);
    }

    public static <T> ResultModel<T> hdfsError() {
        return error("HDFS 操作失败 !", null);
    }

    public static <T> ResultModel<T> obsError() {
        return error("OBS 操作失败 !", null);
    }

    public static <T> ResultModel<T> error(String msg) {
        return error(msg, null);
    }

    public static <T> ResultModel<T> error(String msg, T data) {
        return new ResultModel<T>(ResultModel.ERROR, msg, data);
    }

    public boolean isOperate() {
        return operate;
    }

    public void setOperate(boolean operate) {
        this.operate = operate;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
