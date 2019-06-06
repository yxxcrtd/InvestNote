package com.caimao.weixin.note.util;

public enum ExceptionTypeConstants {
    SYSTEM_EXCEPTION(0),
    DATA_VALIDATION_EXCEPTION(1),
    BIZ_EXCEPTION(2);

    private int id;

    private ExceptionTypeConstants(int id) {
        this.id = id;
    }

    public int value() {
        return this.id;
    }
}
