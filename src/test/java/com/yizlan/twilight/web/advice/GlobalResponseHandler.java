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

package com.yizlan.twilight.web.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yizlan.gelato.canonical.protocol.TerResult;
import com.yizlan.twilight.web.annotation.AbstractGlobalResponseBodyAdvice;
import com.yizlan.twilight.web.autoconfigure.texture.HarmonyProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global response handler.
 *
 * @author Zen Gershon
 * @since 1.0
 */
@RestControllerAdvice
public class GlobalResponseHandler extends AbstractGlobalResponseBodyAdvice<String, String, Object, Object> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public GlobalResponseHandler(TerResult<String, String, Object> terResult, HarmonyProperties harmonyProperties) {
        super(terResult, harmonyProperties);
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        if (returnType.getGenericParameterType().equals(String.class)) {

            try {
                OBJECT_MAPPER.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

                return OBJECT_MAPPER.writeValueAsString(terResult.success().data(data));
            } catch (JsonProcessingException e) {
                return terResult.failure().message("json数据转化异常");
            }
        } else {
            return terResult.success().data(data);
        }
    }

}
