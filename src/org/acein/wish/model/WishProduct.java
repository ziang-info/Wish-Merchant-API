
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

package org.acein.wish.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

public class WishProduct{

    
    /**
    {'Product': {
        'description': ' this is a cool shoe',
        'id': '52a11ebdf94adc0cfee0dbd9',
        'name': 'red shoe',
        'number_saves': '0',
        'number_sold': '0',
        'parent_sku': 'red-shoe',
        'review_status': 'pending',
        'tags': [
            {'Tag': {'id': 'red','name': 'red'}},
            {'Tag': {'id': 'cool','name': 'cool'}},
            {'Tag': {'id': 'shoe','name': 'shoe'}}
        ],
        'variants': [
            {'Variant': {
                'enabled': 'True',
                'id': '52a11ebef94adc0cfee0dbdb',
                'product_id': '52a11ebdf94adc0cfee0dbd9',
                'inventory': '100',
                'price': '100.0',
                'shipping': '10.0',
                'shipping_time': '5-10',
                'sku': 'red-shoe-11'
                }
            }]
        }
    }
    */
    
    private JSONObject productObj;

    private String id;
    private String name;
    private String description;
    private String brand;
    private String landing_page_url;
    private String upc;
    private String main_image;
    private String extra_images;
    private int number_saves;
    private int number_sold;
    private String review_status;
    
    private Vector tags = new Vector();
    private Vector variants;

    
  public WishProduct(JSONObject object){

      
    productObj = object.getJSONObject("Product");
    
    this.id = productObj.getString("id");
    

    this.name = productObj.getString("id");
    this.description = productObj.getString("description");
    this.brand = productObj.getString("brand");
    this.landing_page_url = productObj.getString("landing_page_url");
    this.upc = productObj.getString("upc");
    this.main_image = productObj.getString("main_image");
    this.extra_images = productObj.getString("extra_images");
    this.number_saves = productObj.getInt("number_saves");
   this.number_sold = productObj.getInt("number_sold");
    this.review_status = productObj.getString("review_status");
    
    // tags
     JSONArray tagsArr = productObj.getJSONArray("tgs");
     Iterator<Object> it = tagsArr.iterator();
     while(it.hasNext()){
         JSONObject jo = (JSONObject) it.next();
         WishProductTag t = new WishProductTag(jo.getJSONObject("Tag"));
         this.tags.add(t);
     }
     
     // variants
     JSONArray varsArr = productObj.getJSONArray("variants");
     it = varsArr.iterator();
     while(it.hasNext()){
         JSONObject jo = (JSONObject) it.next();
         WishProductVariation v = new WishProductVariation(jo.getJSONObject("Variant"));
         this.variants.add(v);
     }
    
    /*
    $vars = get_object_vars($product);
    
    foreach ($vars as $key=>$val){
      $this->$key = $val;
    }
    
    $variants = array();

    foreach ($product->variants as $variant){
      $variants[] = new WishProductVariation($variant);
    }
    $this->variants = $variants;
    
    
    Iterator it = object.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key != null) {
                params.put(key, product.get(key));
            }
        }
    */
    
  }

  /*
  public Hashtable getParams(ArrayList keys){
    Hashtable params = new Hashtable();

        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key != null) {
                params.put(key, product.get(key));
            }
        }
        return params;
  }
  */

    public JSONObject getProductObj() {
        return productObj;
    }

    public void setProductObj(JSONObject productObj) {
        this.productObj = productObj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLanding_page_url() {
        return landing_page_url;
    }

    public void setLanding_page_url(String landing_page_url) {
        this.landing_page_url = landing_page_url;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getMain_image() {
        return main_image;
    }

    public void setMain_image(String main_image) {
        this.main_image = main_image;
    }

    public String getExtra_images() {
        return extra_images;
    }

    public void setExtra_images(String extra_images) {
        this.extra_images = extra_images;
    }

    public int getNumber_saves() {
        return number_saves;
    }

    public void setNumber_saves(int number_saves) {
        this.number_saves = number_saves;
    }

    public int getNumber_sold() {
        return number_sold;
    }

    public void setNumber_sold(int number_sold) {
        this.number_sold = number_sold;
    }

    public String getReview_status() {
        return review_status;
    }

    public void setReview_status(String review_status) {
        this.review_status = review_status;
    }

    public Vector getTags() {
        return tags;
    }

    public void setTags(Vector tags) {
        this.tags = tags;
    }

    public Vector getVariants() {
        return variants;
    }

    public void setVariants(Vector variants) {
        this.variants = variants;
    }
  
 

}