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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;
import org.acein.util.URLBuilder;
import org.acein.wish.exception.InvalidArgumentException;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class WishRequest {

    public static final String VERSION = "v2/";
    public static final String BASE_PROD_PATH = "https://merchant.wish.com/api/";
    public static final String BASE_SANDBOX_PATH = "https://sandbox.merchant.wish.com/api/";
    public static final String BASE_STAGE_PATH = "https://merch.corp.contextlogic.com/api/";

    private WishSession session;
    private String method;
    private String path;
    private Hashtable<String,String> params;

    public WishRequest(WishSession session, String method, String path, Hashtable<String,String> params) {
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
    
    /*
     public WishResponse execute2() throws Exception {
 
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
        conn.setRequestProperty("content-type", "text/json");
        conn.setRequestProperty("User-agent","wish-php-sdk");
        // options.put("CURLOPT_RETURNTRANSFER", true);
        // options.put("CURLOPT_HEADER", "true");
        
         conn.connect();// 握手  

         if (method.equalsIgnoreCase("POST")) {
            //options.put("CURLOPT_POSTFIELDS", params);

             OutputStream os = conn.getOutputStream();// 拿到输出流  
             // os.write("?path=c:/test.xml&test=2012".getBytes("utf-8"));  
             PrintWriter out = new PrintWriter(os);
             
             String p = URLBuilder.httpBuildQuery(params, null);
             Logger.getLogger("WishRequest").info(p);
             
             out.print(p);
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
     */
    
    public WishResponse execute() throws Exception {

        String urlStr = this.getRequestURL() + this.getVersion() + this.path;
        String result = null;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        if (method.equalsIgnoreCase("GET")) {
            urlStr = urlStr + "?" + URLBuilder.httpBuildQuery((Hashtable) params, null);//http_build_query(params);
            System.out.println("http get:" + urlStr);
            
            HttpGet httpget = new HttpGet(urlStr);
            response = httpclient.execute(httpget);
            
        } else if (method.equalsIgnoreCase("POST")) {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            //formparams.add(new BasicNameValuePair("param1", "value1"));
            //formparams.add(new BasicNameValuePair("param2", "value2"));

            Enumeration<String> en = params.keys();
            while (en.hasMoreElements()) {
                String key = en.nextElement();
                formparams.add(new BasicNameValuePair(key, params.get(key)));
            }
            
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
            HttpPost httppost = new HttpPost(urlStr);
            httppost.setEntity(entity);
            
            response = httpclient.execute(httppost);
        } else {
            Logger.getLogger("WishRequest").info("Invalid Submit Method.");
        }

        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();

                // do something useful
                InputStreamReader isr = new InputStreamReader(instream);
                BufferedReader br = new BufferedReader(isr);
                result = br.readLine();
                System.out.println(result);

                instream.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
        }

        //JSONObject j = new JSONObject(respStr);
        //Hashtable decoded_result = new Hashtable(); // From json structure.
        return new WishResponse(this, /*decoded_result,*/ result);
    }
}
