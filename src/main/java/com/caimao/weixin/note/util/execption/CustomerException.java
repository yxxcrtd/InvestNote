package com.caimao.weixin.note.util.execption;

public class CustomerException extends RuntimeException {

    private static final long serialVersionUID = 5860353203820291969L;

    private Integer code;
	@SuppressWarnings("unused")
	private String sourceType;

    public CustomerException(Integer code) {
        this.code = code;
    }

    public CustomerException(String message, Integer code) {
        super(message);
        this.code = code;
    }
    public CustomerException(String message, Integer code, String sourceType) {
        super(message);
        this.code = code;
        this.sourceType=sourceType;
    }
    
    public CustomerException(String code, String message, String sourceType) {
        super(message);
        code = code.replaceAll("[^0-9]", "");
        this.code = Integer.parseInt(code);
        this.sourceType=sourceType;
    }

    public CustomerException(String message, Throwable cause, Integer code) {
        super(message, cause);
        this.code = code;
    }

    public CustomerException(Throwable cause, Integer code) {
        super(cause);
        this.code = code;
    }

    public CustomerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
