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

package com.yizlan.twilight.web;

import com.yizlan.gelato.canonical.protocol.TerResult;
import com.yizlan.twilight.web.actuate.HealthController;
import com.yizlan.twilight.web.advice.GlobalExceptionHandler;
import com.yizlan.twilight.web.advice.GlobalResponseHandler;
import com.yizlan.twilight.web.autoconfigure.texture.HarmonyAutoConfiguration;
import com.yizlan.twilight.web.autoconfigure.texture.HarmonyProperties;
import com.yizlan.twilight.web.config.TwilightConfig;
import com.yizlan.twilight.web.controller.HomeController;
import com.yizlan.twilight.web.controller.order.BookController;
import com.yizlan.twilight.web.controller.personal.AccountController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {HarmonyAutoConfiguration.class, TwilightConfig.class})
@TestPropertySource("classpath:application.yaml")
public class HarmonyApplicationTest {

    @InjectMocks
    private AccountController accountController;

    @InjectMocks
    private BookController bookController;

    @InjectMocks
    private HomeController homeController;

    @InjectMocks
    private HealthController healthController;

    @Resource
    private TerResult<String, String, Object> terResult;

    @Resource
    private HarmonyProperties harmonyProperties;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(homeController, accountController, bookController, healthController)
                .setControllerAdvice(new GlobalResponseHandler(terResult, harmonyProperties),
                        new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testGlobalResponseResult() throws Exception {
        // echo `home`
        mockMvc.perform(MockMvcRequestBuilders.get("/home")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("home"))
                .andDo(MockMvcResultHandlers.print());
        // echo `{"success":true,"code":"code","message":"操作成功","data":"book"}`
        mockMvc.perform(MockMvcRequestBuilders.get("/order/book")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("book"))
                .andDo(MockMvcResultHandlers.print());
        // echo `{"success":false,"code":"500","message":"无权查看.","data":null}`
        mockMvc.perform(MockMvcRequestBuilders.get("/personal/account")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("500"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("无权查看."))
                .andDo(MockMvcResultHandlers.print());
        // echo `It's ok!`
        mockMvc.perform(MockMvcRequestBuilders.get("/health")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
