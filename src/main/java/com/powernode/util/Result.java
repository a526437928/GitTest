package com.powernode.util;


import com.powernode.Exception.ResultException;

public class Result{
    private boolean success;

    private int status;

    private String msg;

    private Object data;

    public Result(boolean success,int status, String msg) {
        this.status = status;
        this.msg = msg;
        this.success = success;
    }

    public Result(boolean success,int status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.success = success;
    }

    //无返回值，但是是成功的。
    public static Result OK(){
        return new Result(true,200,"SUCCESS");
    }

    //成功的，存在返回值的
    public static Result OK(Object data){
        return new Result(true,200,"SUCCESS",data);
    }

    public static Result build(boolean success,int status,String msg){
        return new Result(success,status,msg);
    }

    public static Result build(int status, ResultException e){
        return new Result(false,status,e.getMessage());
    }

    public static Result build(ResultException e){
        return new Result(false,e.getStatus(),e.getMessage());
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
