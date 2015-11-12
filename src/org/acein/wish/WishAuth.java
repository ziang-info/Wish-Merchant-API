/**
 * Copyright 2015 Ziang.info, ContextLogic or its affiliates. All Rights
 * Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.acein.wish;

import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.Hashtable;
import org.acein.wish.exception.ConnectionException;
import org.acein.wish.exception.InvalidArgumentException;
import org.acein.wish.exception.UnauthorizedRequestException;

public class WishAuth {

    private String client_id;
    private String client_secret;

    private WishSession session;

    public WishAuth(String client_id, String client_secret, String session_type) {
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.session = new WishSession("", session_type, null);
    }

    public WishAuth(String client_id, String client_secret) {
        String session_type = "prod";

        this.client_id = client_id;
        this.client_secret = client_secret;
        this.session = new WishSession("", session_type, null);
    }

    public WishResponse getToken(String code, String redirect_uri) throws Exception {
        String type = "POST";
        String path = "oauth/access_token";
        Hashtable params = new Hashtable<String, String>();
        params.put("client_id", URLEncoder.encode(this.client_id, "utf-8"));
        params.put("client_secret", URLEncoder.encode(this.client_secret, "utf-8"));
        params.put("code", URLEncoder.encode(code, "utf-8"));
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", URLEncoder.encode(redirect_uri, "utf-8"));

        WishRequest request = new WishRequest(this.session, type, path, params);
        WishResponse response = request.execute();
        if (response.getStatusCode() == 4000) {
            throw new UnauthorizedRequestException("Unauthorized access", request, response);
        }
        if (response.getStatusCode() == 1016) {
            throw new UnauthorizedRequestException("Access code expired",
                    request,
                    response);
        }

        return response;
    }

    public WishResponse refreshToken(String refresh_token) throws UnauthorizedRequestException, ConnectionException, InvalidArgumentException, Exception {
        String type = "POST";
        String path = "oauth/refresh_token";
        Hashtable params = new Hashtable();
        params.put("client_id", this.client_id);
        params.put("client_secret", this.client_secret);
        params.put("refresh_token", refresh_token);
        params.put("grant_type", "refresh_token");

        WishRequest request = new WishRequest(this.session, type, path, params);
        WishResponse response = request.execute();
        if (response.getStatusCode() == 4000) {
            throw new UnauthorizedRequestException("Unauthorized access",
                    request,
                    response);
        }

        return response;
    }
}
