<?php
/**
 * Copyright 2014 Wish.com, ContextLogic or its affiliates. All Rights Reserved.
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

namespace Wish;

use Wish\Exception\UnauthorizedRequestException;
use Wish\Exception\ServiceResponseException;
use Wish\Exception\OrderAlreadyFulfilledException;
use Wish\Model\WishProduct;
use Wish\Model\WishProductVariation;
use Wish\Model\WishOrder;
use Wish\Model\WishTracker;
use Wish\Model\WishReason;


class WishAuth{
  private $client_id;
  private $client_secret;

  public function __construct($client_id,$client_secret,$session_type='prod'){
    $this->client_id = $client_id;
    $this->client_secret = $client_secret;
    $this->session = new WishSession('',$session_type,null);
  }

  public function getToken($code, $redirect_uri){
    $type = 'POST';
    $path = 'oauth/access_token';
    $params = array(
      'client_id'=>$this->client_id,
      'client_secret'=>$this->client_secret,
      'code'=>$code,
      'grant_type'=>'authorization_code',
      'redirect_uri'=>$redirect_uri);

    $request = new WishRequest($this->session,$type,$path,$params);
    $response = $request->execute();
    if($response->getStatusCode()==4000){
      throw new UnauthorizedRequestException("Unauthorized access",
        $request,
        $response);
    }
    if($response->getStatusCode()==1016){
      throw new UnauthorizedRequestException("Access code expired",
        $request,
        $response);
    }

    return $response;
  }

  public function refreshToken($refresh_token){
    $type = 'POST';
    $path = 'oauth/refresh_token';
    $params = array(
      'client_id'=>$this->client_id,
      'client_secret'=>$this->client_secret,
      'refresh_token'=>$refresh_token,
      'grant_type'=>'refresh_token');

    $request = new WishRequest($this->session,$type,$path,$params);
    $response = $request->execute();
    if($response->getStatusCode()==4000){
      throw new UnauthorizedRequestException("Unauthorized access",
        $request,
        $response);
    }

    return $response;
  }
}

