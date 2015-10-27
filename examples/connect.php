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


require_once '../vendor/autoload.php';
use Wish\WishAuth;
use Wish\WishClient;

$auth = new WishAuth('CLIENT_ID','CLIENT_SECRET','sandbox');
$response = $auth->getToken('ACCESS_TOKEN','https://website.com');

$token = $response->getData()->access_token;

$client = new WishClient($token,'sandbox');

print "RESULT: ".$client->authTest();

