
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

package org.acein.wish.exception;

import org.acein.wish.WishRequest;
import org.acein.wish.WishResponse;

public class ServiceResponseException extends RuntimeException{

  protected WishResponse response;
  protected WishRequest request;

  protected String exceptionType;
  protected String exceptionCode;

  public ServiceResponseException(String message,WishRequest request,WishResponse response){
    super(message);
    this.request = request;
    this.response = response;
  }
  
  public String getExceptionCode(){
    return this.exceptionCode;
  }

  public String getExceptionType(){
    return this.exceptionType;
  }

  public WishRequest getRequest(){
    return this.request;
  }

  public void setResponse(WishResponse response){
    this.response = response;
  }
  public WishResponse getResponse(){
    return this.response;
  }

  public String getErrorMessage(){
    return (this.response!=null) ? this.response.getMessage() : null;
  }

  public int getStatusCode(){
    return (this.response!=null) ? this.response.getStatusCode() : null;
  }

  public String toString(){
      String message = "ServiceResponseException(" + exceptionCode + ", " 
              + exceptionType + ")："
              + this.getMessage();
      
      /*
    $message = get_class($this).': '
     .'Message: '.this.getMessage()."\n"
     .'Status code: '.this.getStatusCode()."\n"
     .'Error message: '.this.getErrorMessage()."\n"
     .'Stack trace: '."\n";
     foreach(this.getTrace() as $trace){
      $message = $message.$trace['file'].' at '.$trace['function'].':'.
        $trace['line']."\n";
     }
              */
      
     return message;
  }


}