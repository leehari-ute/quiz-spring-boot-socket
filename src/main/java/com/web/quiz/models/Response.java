package com.web.quiz.models;

public class Response {
    private String message;
    private int httpStatus;
    private boolean success;
    private Object data;

    public Response(){
    }

    public Response(String message, int httpStatus, boolean success, Object data) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.success = success;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "message='" + message + '\'' +
                ", httpStatus=" + httpStatus +
                ", success=" + success +
                ", data=" + data +
                '}';
    }
}
