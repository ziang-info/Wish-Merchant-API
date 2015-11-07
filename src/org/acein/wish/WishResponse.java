
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

import java.util.ArrayList;
import java.util.Hashtable;

public class WishResponse{
  
  private WishRequest request;
  private Hashtable responseData;
  private String rawResponse;
  
  private WishPager pager;

  private int status_code;

  public WishResponse(WishRequest request,Hashtable responseData,String rawResponse){
    this.request = request;
    this.responseData = responseData;
    this.rawResponse = rawResponse;
    if(this.responseData.get("paging") != null){
      this.pager = new WishPager((WishPager)this.responseData.get("paging"));
    }
  }

  public int getStatusCode(){
    return (int)this.responseData.get("code");
  }

  public Object getData(){
    return this.responseData.get("data");
  }
  
  public boolean hasMore(){
    if(this.pager!=null){
      return this.pager.hasNext();
    }else {
      return false;
    }
  }

  public String getMessage(){
    return (String)this.responseData.get("message");
  }
  public WishRequest getRequest(){
    return this.request;
  }
  public Hashtable getResponse(){
    return this.responseData;
  }
  public String getRawResponse(){
    return this.rawResponse;
  }



}