package com.powernode.handle;

import com.powernode.Exception.ResultException;
import com.powernode.util.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.plugin2.server.main.ResultID;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class GlobalExceptioHandler {

    @ExceptionHandler
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request, Exception e){
        ResultException resultException = null;
        try {
            if(e instanceof ResultException){
                resultException =(ResultException) e;
            }else {
                resultException  = new ResultException("未知类型异常");

            }

            return Result.build(1005,resultException);
        } catch (ResultException e1) {

        }
        return null;
    }

}
