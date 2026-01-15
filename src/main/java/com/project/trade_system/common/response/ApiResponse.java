package com.project.trade_system.common.response;

public class ApiResponse<T> {

    private boolean success;
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> res = new ApiResponse<>();
        res.success = true;
        res.data = data;
        return res;
    }
}
