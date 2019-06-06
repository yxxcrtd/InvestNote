package com.caimao.weixin.note.util.execption;

/**
 * 业务异常信息
 */
@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {
    private Integer code;
    private String message;

    public BusinessException(String message, Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
