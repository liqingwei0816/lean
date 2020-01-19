/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloud.config;


import org.springframework.util.StringUtils;

public class ZookeeperConfigUtil {
    public static final String RULE_ROOT_PATH = "/sentinel_rule_config";
    //添加不同规则路径问题
    public static final String RULE_FLOW_PATH = "/flow_rule_config";
    public static final String RULE_DEGRADE_PATH = "/degrade_rule_config";


    public static String getFlowPath(String appName) {
        return getPath(appName, RULE_FLOW_PATH);
    }

    public static String getDegradePath(String appName) {
        return getPath(appName, RULE_DEGRADE_PATH);
    }

    private static String getPath(String appName, String path) {
        StringBuilder stringBuilder = new StringBuilder(RULE_ROOT_PATH);

        if (StringUtils.isEmpty(appName)) {
            return stringBuilder.toString();
        }
        if (appName.startsWith("/")) {
            stringBuilder.append(appName);
        } else {
            stringBuilder.append("/")
                    .append(appName);
        }
        stringBuilder.append(path);
        return stringBuilder.toString();
    }
}