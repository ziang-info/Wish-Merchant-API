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
use Wish\Model\WishAddress;
use Wish\Model\WishTicket;


class WishClient{
  private $session;
  private $products;
  private $orders;

  const LIMIT = 50;

  public function __construct($access_token,$session_type='prod',$merchant_id=null){

    $this->session = new WishSession($access_token,$session_type,$merchant_id);

  }

  public function getResponse($type,$path,$params=array()){

    $request = new WishRequest($this->session,$type,$path,$params);
    $response = $request->execute();
    if($response->getStatusCode()==4000){
      throw new UnauthorizedRequestException("Unauthorized access",
        $request,
        $response);
    }
    if($response->getStatusCode()==1015){
      throw new UnauthorizedRequestException("Access Token expired",
        $request,
        $response);
    }
    if($response->getStatusCode()==1016){
      throw new UnauthorizedRequestException("Access Token revoked",
        $request,
        $response);
    }
    if($response->getStatusCode()==1000){
      throw new ServiceResponseException("Invalid parameter",
        $request,
        $response);
    }
    if($response->getStatusCode()==1002){
      throw new OrderAlreadyFulfilledException("Order has been fulfilled",
        $request,
        $response);
    }
    if($response->getStatusCode()!=0){
      throw new ServiceResponseException("Unknown error",
        $request,
        $response);
    }
    return $response;

  }

  public function getResponseIter($method,$uri,$getClass,$params=array()){
    $start = 0;
    $params['limit'] = static::LIMIT;
    $class_arr = array();
    do{
      $params['start']=$start;
      $response = $this->getResponse($method,$uri,$params);
      foreach($response->getData() as $class_raw){
        $class_arr[] = new $getClass($class_raw);
      }
      $start += static::LIMIT;
    }while($response->hasMore());
    return $class_arr;
  }

  public function authTest(){
    $response = $this->getResponse('GET','auth_test');
    return "success";

  }

  // PRODUCT

  public function getProductById($id){
    $params = array('id'=>$id);
    $response = $this->getResponse('GET','product',$params);
    return new WishProduct($response->getData());
  }

  public function createProduct($object){
    $response = $this->getResponse('POST','product/add',$object);
    return new WishProduct($response->getData());
  }

  public function updateProduct(WishProduct $product){

    $params = $product->getParams(array(
      'id',
      'name',
      'description',
      'tags',
      'brand',
      'landing_page_url',
      'upc',
      'main_image',
      'extra_images'));

    $response = $this->getResponse('POST','product/update',$object);

    return "success";
  }

  public function enableProduct(WishProduct $product){
    $this->enableProductById($product->id);
  }

  public function enableProductById($id){
    $params = array('id'=>$id);
    $response = $this->getResponse('POST','product/enable',$params);
    return "success";
  }

  public function disableProduct(WishProduct $product){
    $this->disableProductById($product->id);
  }

  public function disableProductById($id){
    $params = array('id'=>$id);
    $response = $this->getResponse('POST','product/disable',$params);
    return "success";
  }

  public function getAllProducts(){
    return $this->getResponseIter(
      'GET',
      'product/multi-get',
      "Wish\Model\WishProduct");
  }

  public function removeExtraImages(WishProduct $product){
    return $this->removeExtraImagesById($product->id);
  }

  public function removeExtraImagesById($id){
    $params = array('id'=>$id);
    $response = $this->getResponse('POST','product/remove-extra-images',$params);
    return "success";
  }

  // PRODUCT VARIATION

  public function createProductVariation($object){
    $response = $this->getResponse('POST','variant/add',$object);
    return new WishProductVariation($response->getData());
  }

  public function getProductVariationBySKU($sku){
    $response = $this->getResponse('GET','variant',array('sku'=>$sku));
    return new WishProductVariation($response->getData());
  }

  public function updateProductVariation(WishProductVariation $var){
    $params = $var->getParams(array(
        'sku',
        'inventory',
        'price',
        'shipping',
        'enabled',
        'size',
        'color',
        'msrp',
        'shipping_time',
        'main_image'
      ));
    $response = $this->getResponse('POST','variant/update',$params);
    return "success";
  }
  public function enableProductVariation(WishProductVariation $var){
    $this->enableProductVariationBySKU($var->sku);
  }
  public function enableProductVariationBySKU($sku){
    $params = array('sku'=>$sku);
    $response = $this->getResponse('POST','variant/enable',$params);
    return "success";
  }

  public function disableProductVariation(WishProductVariation $var){
    $this->disableProductVariationBySKU($var->sku);
  }
  public function disableProductVariationBySKU($sku){
    $params = array('sku'=>$sku);
    $response = $this->getResponse('POST','variant/disable',$params);
    return "success";
  }

  public function updateInventoryBySKU($sku,$newInventory){
    $params = array('sku'=>$sku,'inventory'=>$newInventory);
    $response = $this->getResponse('POST','variant/update-inventory',$params);
    return "success";
  }

  public function getAllProductVariations(){
    return $this->getResponseIter(
      'GET',
      'variant/multi-get',
      "Wish\Model\WishProductVariation");
  }

  // ORDER

  public function getOrderById($id){
    $response = $this->getResponse('GET','order',array('id'=>$id));
    return new WishOrder($response->getData());
  }

  public function getAllChangedOrdersSince($time=null){
    $params = array();
    if($time){
      $params['since']=$time;
    }
    return $this->getResponseIter(
      'GET',
      'order/multi-get',
      "Wish\Model\WishOrder",
      $params);
  }

  public function getAllUnfulfilledOrdersSince($time=null){
    $params = array();
    if($time){
      $params['since']=$time;
    }
    return $this->getResponseIter(
      'GET',
      'order/get-fulfill',
      "Wish\Model\WishOrder",
      $params);
  }

  public function fulfillOrderById($id,WishTracker $tracking_info){
    $params = $tracking_info->getParams();
    $params['id']=$id;
    $response = $this->getResponse('POST','order/fulfill-one',$params);
    return "success";
  }

  public function fulfillOrder(WishOrder $order, WishTracker $tracking_info){
    return $this->fulfillOrderById($order->order_id,$tracking_info);
  }

  public function refundOrderById($id,$reason,$note=null){
    $params = array(
      'id'=>$id,
      'reason_code'=>$reason);
    if($note){
      $params['reason_note'] = $note;
    }
    $response = $this->getResponse('POST','order/refund',$params);
    return "success";
  }

  public function refundOrder(WishOrder $order,$reason,$note=null){
    return refundOrderById($order->order_id,$reason,$note);
  }

  public function updateTrackingInfo(WishOrder $order,WishTracker $tracker){
    return $this->updateTrackingInfoById($order->order_id,$tracker);
  }

  public function updateTrackingInfoById($id,WishTracker $tracker){
    $params = $tracker->getParams();
    $params['id']=$id;
    $response = $this->getResponse('POST','order/modify-tracking',$params);
    return "success";
  }

  public function updateShippingInfo(WishOrder $order,WishAddres $address){
      return $this->updateShippingInfoById($order->order_id,$address);
  }

  public function updateShippingInfoById($id,WishAddress $address){
    $params = $address->getParams();
    $params['id']=$id;
    $response = $this->getResponse('POST','order/change-shipping',$params);
    return "success";
  }

  // TICKET

  public function getTicketById($id){
    $params['id']=$id;
    $response = $this->getResponse('GET','ticket',$params);
    return new Wishticket($response->getData());
  }

  public function getAllActionRequiredTickets(){
    return $this->getResponseIter(
      'GET',
      'ticket/get-action-required',
      "Wish\Model\WishTicket");
  }

  public function replyToTicketById($id,$reply){
    $params['id']=$id;
    $params['reply']=$reply;
    $response = $this->getResponse('POST','ticket/reply',$params);
    return "success";
  }

  public function closeTicketById($id){
    $params['id']=$id;
    $response = $this->getResponse('POST','ticket/close',$params);
    return "success";
  }

  public function appealTicketById($id){
    $params['id']=$id;
    $response = $this->getResponse('POST','ticket/appeal-to-wish-support',$params);
    return "success";
  }

  public function reOpenTicketById($id,$reply){
    $params['id']=$id;
    $params['reply']=$reply;
    $response = $this->getResponse('POST','ticket/re-open',$params);
    return "success";
  }

}
