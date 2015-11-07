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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import org.acein.util.URLBuilder;
import org.acein.wish.exception.ConnectionException;
import org.acein.wish.exception.InvalidArgumentException;
import org.json.JSONObject;

public class WishRequest {

    public static final String VERSION = "v2/";
    public static final String BASE_PROD_PATH = "https://merchant.wish.com/api/";
    public static final String BASE_SANDBOX_PATH = "https://sandbox.merchant.wish.com/api/";
    public static final String BASE_STAGE_PATH = "https://merch.corp.contextlogic.com/api/";

    private WishSession session;
    private String method;
    private String path;
    private Hashtable params;

    public WishRequest(WishSession session, String method, String path, Hashtable params) {
        this.session = session;
        this.method = method;
        this.path = path;
        params.put("access_token", session.getAccessToken());

        if (session.getMerchantId() != null) {
            params.put("merchant_id", session.getMerchantId());
        }
        this.params = params;
    }

    public String getVersion() {
        return VERSION;
    }

    public String getRequestURL() throws InvalidArgumentException {
        switch (this.session.getSessionType()) {
            case WishSession.SESSION_PROD:
                return BASE_PROD_PATH;
            case WishSession.SESSION_SANDBOX:
                return BASE_SANDBOX_PATH;
            case WishSession.SESSION_STAGE:
                return BASE_STAGE_PATH;
            default:
                throw new InvalidArgumentException("Invalid session type");
        }
    }

    /*
    public WishResponse execute() throws ConnectionException, InvalidArgumentException {
        String url = this.getRequestURL() + this.getVersion() + this.path;
        String curl = curl_init();
        

        Hashtable options = new Hashtable();
        options.put("CURLOPT_CONNECTTIMEOUT", 10);
        options.put("CURLOPT_RETURNTRANSFER", true);
        options.put("CURLOPT_ENCODING", "");
        options.put("CURLOPT_USERAGENT", "wish-java-sdk");
        options.put("CURLOPT_HEADER", "true");

        if (this.method.equalsIgnoreCase("GET")) {
            url = url + "?" + URLBuilder.httpBuildQuery(params, null);//http_build_query(params);
        } else {
            options.put("CURLOPT_POSTFIELDS", params);
        }

        options.put("CURLOPT_URL", url);
        curl_setopt_array(curl, options);

        String result = curl_exec(curl);
        String error = curl_errno(curl);

        String error_message = curl_error(curl);

        if (error != null) {
            throw new ConnectionException(error_message);
        }
        
        String httpStatus = curl_getinfo(curl, CURLINFO_HTTP_CODE);
        int headerSize = curl_getinfo(curl, b);
        curl_close(curl);

        String decoded_result = json_decode(result);
        if (decoded_result == null) {
            Hashtable out = new Hashtable();
            parse_str(result, out);
            return new WishResponse(this, out, result);
        }
        
        return new WishResponse(this, decoded_result, result);
    }
    */
    
    
     public WishResponse execute() throws Exception, InvalidArgumentException, MalformedURLException {
 
         String urlStr = this.getRequestURL() + this.getVersion() + this.path;
        
        if (method.equalsIgnoreCase("GET")) {
            urlStr = urlStr + "?" + URLBuilder.httpBuildQuery(params, null);//http_build_query(params);
        } 

        URL url = new URL(urlStr);
        System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(10000);
        conn.setRequestProperty("content-type", "text/html");
        conn.setRequestProperty("User-agent","wish-java-sdk");
        // options.put("CURLOPT_RETURNTRANSFER", true);
        // options.put("CURLOPT_HEADER", "true");
        
         conn.connect();// 握手  

         if (method.equalsIgnoreCase("POST")) {
            //options.put("CURLOPT_POSTFIELDS", params);

             OutputStream os = conn.getOutputStream();// 拿到输出流  
             // os.write("?path=c:/test.xml&test=2012".getBytes("utf-8"));  
             PrintWriter out = new PrintWriter(os);
             out.print(URLBuilder.httpBuildQuery(params, null));

             out.flush();
             os.flush();
             out.close();
             os.close();
        }

        InputStream is = conn.getInputStream();//拿到输入流  
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String s = br.readLine();
        System.out.println(s);

        br.close();
        isr.close();
        is.close();
        
        
        JSONObject j = new JSONObject(s);
        String result = "";
        Hashtable decoded_result = new Hashtable(); // From json structure.
        
        return new WishResponse(this, decoded_result, result);
    }
}
