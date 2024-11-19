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

package com.yizlan.twilight.web.autoconfigure.texture;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Global Response Result.
 *
 * @author Zen Gershon
 * @since 1.0
 */
@ConfigurationProperties(prefix = "gelato.texture")
public class HarmonyProperties {

    /**
     * Whether to enable Harmony.
     */
    private boolean enabled = false;

    /**
     * The packages to be scanned.
     */
    private String[] packages;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String[] getPackages() {
        return packages;
    }

    public void setPackages(String[] packages) {
        this.packages = packages;
    }
}
