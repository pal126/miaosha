package com.pal.miaosha.Result;

public class Resulta<T> {

    private int code;
    private String msg;
    private T data;

    /**
     *  成功时候的调用
     * */
    public static  <T> Resulta<T> success(T data){
        return new Resulta<T>(data);
    }

    /**
     *  失败时候的调用
     * */
    public static  <T> Resulta<T> error(CodeMsga codeMsg){
        return new Resulta<T>(codeMsg);
    }

    private Resulta(T data) {
        this.data = data;
    }

    private Resulta(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Resulta(CodeMsga codeMsg) {
        if(codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
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

    @Override
    public String toString() {
        return "Resulta{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
