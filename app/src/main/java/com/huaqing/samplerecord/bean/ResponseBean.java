package com.huaqing.samplerecord.bean;

import com.google.gson.annotations.SerializedName;

public class ResponseBean<T> {

    private static final int SUCCESS_CODE = 1;
    private static final int STATUS_2 = 200;
    private static final int STATUS_3 = 500;
    private static final int MULTIDEVICE_CODE = 602;    // 账号在其他设备登录
    private static final int ACCOUNT_FROZEN_CODE = 609;
    private static final int TOKEN_EXPIRE_CODE = 40003;   //  token 过期
    private String token;
//    private boolean success;
    @SerializedName(value = "code", alternate = {"status"})
    private int code;
    @SerializedName(value = "msg", alternate = {"statusText"})
    private String msg;
    @SerializedName(value = "data", alternate = {"result"})
//    private T data;

    public boolean isSuccess() {
        return STATUS_2 == code||STATUS_3 == code;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
//                "success=" + success +
                ", errorCode='" + code + '\'' +
                ", message='" + msg + '\'' +
//                ", data=" + data +
                '}';
    }

    public static int getSuccessCode() {
        return SUCCESS_CODE;
    }

    public static int getStatus2() {
        return STATUS_2;
    }

    public static int getMultideviceCode() {
        return MULTIDEVICE_CODE;
    }

    public static int getAccountFrozenCode() {
        return ACCOUNT_FROZEN_CODE;
    }

    public static int getTokenExpireCode() {
        return TOKEN_EXPIRE_CODE;
    }

    public String getToken() {
        return token == null ? "" : token;
    }

    public void setToken(String token) {
        this.token = token == null ? "" : token;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? "" : msg;
    }
    //    public void setSuccess(boolean success) {
//        this.success = success;
//    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

//    public T getData() {
//        return data;
//    }
//
//    public void setData(T data) {
//        this.data = data;
//    }


}
