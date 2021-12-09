package org.itxtech.pams.controller;

public class Response {
    public static final int CODE_ERR = 0;
    public static final int CODE_OK = 1;

    public int code = CODE_OK;
    public String msg = "";
    public Object data;
}

