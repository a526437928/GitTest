package com.powernode.Exception;

import com.powernode.util.Result;
import com.powernode.util.ResultEnum;

public class ResultException extends RuntimeException {

    private ResultEnum resultEnum;

    public ResultException(ResultEnum resultEnum) {
        this.resultEnum = resultEnum;
    }

    public ResultException(String message) {
        super(message);
    }

    public String getMessage() {
        if (resultEnum == null) {
            return super.getMessage();
        }
        return resultEnum.getMessage();
    }

    public int getStatus() {
        return resultEnum.getStatus();
    }


}
