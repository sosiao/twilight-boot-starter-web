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

package com.yizlan.twilight.web.annotation;

import com.yizlan.gelato.canonical.protocol.TerResult;
import com.yizlan.twilight.web.autoconfigure.texture.HarmonyProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * Abstract global response body advice class for unifying response data handling.
 * This class implements the ResponseBodyAdvice interface to customize the processing of controller return values.
 * The main functionalities include determining whether to process response data based on configuration and
 * providing a base response result object and configuration properties.
 *
 * @param <T> the type of the code field, should implement {@link Comparable} and {@link Serializable}
 * @param <U> the type of the message field, should implement {@link Comparable} and {@link Serializable}
 * @param <S> the type of the data filed
 * @param <Q> the type of the return data object
 * @author Zen Gershon
 * @since 1.0
 */
public abstract class AbstractGlobalResponseBodyAdvice<T extends Comparable<T> & Serializable,
        U extends Comparable<U> & Serializable, S, Q> implements ResponseBodyAdvice<Q> {

    private final HarmonyProperties harmonyProperties;

    protected final TerResult<T, U, S> terResult;

    protected AbstractGlobalResponseBodyAdvice(TerResult<T, U, S> terResult, HarmonyProperties harmonyProperties) {
        Assert.notNull(terResult, "TerResult must not be null");
        Assert.notNull(harmonyProperties, "HarmonyProperties must not be null");
        this.terResult = terResult;
        this.harmonyProperties = harmonyProperties;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> controllerType = returnType.getDeclaringClass();
        String[] packages = harmonyProperties.getPackages();
        boolean emptyPackage = ObjectUtils.isEmpty(packages);
        boolean intercept = emptyPackage || Stream.of(packages).anyMatch(p -> controllerType.getName().startsWith(p));

        return harmonyProperties.isEnabled() && intercept && !TerResult.class.isAssignableFrom(returnType.getParameterType());
    }

}
