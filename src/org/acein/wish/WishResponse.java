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

import org.json.JSONObject;

public class WishResponse {

    private WishRequest request;
    //private Hashtable responseData;
    private JSONObject responseData;
    private String rawResponse;

    private WishPager pager;

    private String message;
    private int status_code;

    public WishResponse(WishRequest request, /*Hashtable responseData,*/ String rawResponse) {
        this.request = request;

        //this.responseData = responseData;
        this.rawResponse = rawResponse;
        
        parseRawResponse();  
    }
    
    private void parseRawResponse(){
        // {"message":"Unauthorized Request","code":4000,"data":{}}
        JSONObject jo = new JSONObject(rawResponse);
        //JSONArray ja = new JSONArray(rawResponse);
        
        message = jo.getString("message");
        status_code = jo.getInt("code");
        System.out.println("Response: (" + status_code + "):" + message);
        
        responseData = jo.getJSONObject("data");
        
        if (jo.has("paging") && jo.get("paging") != null) {
            this.pager = new WishPager((WishPager) this.responseData.get("paging"));
        }
    }

    public int getStatusCode() {
        return status_code;
    }

    public JSONObject getData() {
        return responseData;
    }

    public boolean hasMore() {
        if (this.pager != null) {
            return this.pager.hasNext();
        } else {
            return false;
        }
    }

    public String getMessage() {
        return message;
    }

    public WishRequest getRequest() {
        return this.request;
    }

    public JSONObject getResponse() {
        return responseData;
    }

    public String getRawResponse() {
        return this.rawResponse;
    }

}
