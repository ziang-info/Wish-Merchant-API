
/**
 * Copyright 2015 Ziang.info, ContextLogic or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.acein.wish;

public class WishSession {

    public static final int SESSION_PROD = 1;
    public static final int SESSION_SANDBOX = 2;
    public static final int SESSION_STAGE = 3;

    private String api_key;
    private int session_type;
    private String merchant_id;

    private String access_token;

    public WishSession(String access_token, String session_type, String merchant_id) {
        this.access_token = access_token;
        this.merchant_id = merchant_id;
        switch (session_type) {
            case "sandbox":
                this.session_type = SESSION_SANDBOX;
                break;
            case "prod":
                this.session_type = SESSION_PROD;
                break;
            case "stage":
                this.session_type = SESSION_STAGE;
                break;
            default:
                throw new IllegalArgumentException("Invalid session type");
        }
    }

    public String getAccessToken() {
        return this.access_token;
    }

    public int getSessionType() {
        return this.session_type;
    }

    public String getMerchantId() {
        return this.merchant_id;
    }

}
