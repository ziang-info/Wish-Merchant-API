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
package org.acein.wish.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import org.json.JSONObject;

public class WishProductVariation {
    
    /*
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
            }
    */

    private JSONObject variant;
    
    private boolean enabled;
    private String id;//'id': '52a11ebef94adc0cfee0dbdb',
    private String product_id;//': '52a11ebdf94adc0cfee0dbd9',
    private int inventory;//': '100',
    private double price;//': '100.0',
    private double shipping;//': '10.0',
    private String shipping_time;//': '5-10',
    private String sku;//': 'red-shoe-11'

    public WishProductVariation(JSONObject variantObj) {

        this.variant = variantObj;

        enabled = variant.getBoolean("enabled");
        id = variant.getString("id");
        product_id = variant.getString("product_id");
        inventory = variant.getInt("inventory");
        price = variant.getDouble("price");
        shipping = variant.getDouble("shipping");
        shipping_time = variant.getString("shipping_time");
        sku = variant.getString("sku");
    }

    public Hashtable getParams(ArrayList keys) {
        Hashtable params = new Hashtable();

        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key != null) {
                params.put(key, variant.get(key));
            }
        }
        return params;
    }

    public JSONObject getVariant() {
        return variant;
    }

    public void setVariant(JSONObject variant) {
        this.variant = variant;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getShipping() {
        return shipping;
    }

    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    public String getShipping_time() {
        return shipping_time;
    }

    public void setShipping_time(String shipping_time) {
        this.shipping_time = shipping_time;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    

}
