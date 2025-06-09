/*
 * Copyright (C) 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yizlan.twilight.web.protocol;


import com.yizlan.gelato.canonical.protocol.TernaryResult;

import java.io.Serializable;

public class Result<T> implements TernaryResult<String, T>, Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean success = false;

    private String code;

    private String message = "操作成功";

    private T data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }

    public static <T> Result<T> success(String msg, T data) {
        return build(Boolean.TRUE, msg, data);
    }

    @Override
    public Result<T> success() {
        return build(Boolean.TRUE, "code", message, null);
    }

    @Override
    public Result<T> failure() {
        return build(Boolean.FALSE, null, null,null);
    }

    public static <T> Result<T> failure(String code, String message) {
        return build(Boolean.FALSE, code, message, null);
    }


    public Result() {
        // to do nothing
    }

    private Result(Boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private static <T> Result<T> build(Boolean success, String code, String message, T data) {
        return new Result<>(success, code, message, data);
    }

    private static <T> Result<T> build(Boolean success, String msg, T data) {
        return build(success, msg, null, data);
    }

    private static <T> Result<T> build(Boolean success, T data) {
        return build(success, null, "操作成功666", data);
    }
}
