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
package org.acein.wish.test;

import java.util.Hashtable;
import org.acein.wish.WishClient;
import org.acein.wish.model.WishProduct;
import org.acein.wish.model.WishProductVariation;

public class CreateProduct {

    public static void main(String[] args) {

        // ACCESS_TOKEN
        final String token = "ACCESS_TOKEN";

        WishClient client = new WishClient(token, "sandbox", null);

        Hashtable product = new Hashtable();
        product.put("name", "Red Shoe");
        product.put("main_image", "https://www.google.com/images/srpr/logo11w.png");
        product.put("sku", "prod 7");
        product.put("parent_sku", "prod 7");
        product.put("shipping", "10");
        product.put("tags", "red,shoe,cool");
        product.put("description", "a cool shoe");
        product.put("price", "100");
        product.put("inventory", "10");
        product.put("randomfield", "12321");

        try {
            WishProduct prod_res = client.createProduct(product);
            System.out.println(prod_res);

            Hashtable product_var = new Hashtable();

            product_var.put("parent_sku", product.get("parent_sku"));
            product_var.put("color", "red");
            product_var.put("sku", "var 7");
            product_var.put("inventory", 10);
            product_var.put("price", 10);
            product_var.put("shipping", 10);

            WishProductVariation prod_var = client.createProductVariation(product_var);
            System.out.println(prod_var);

        } catch (Exception e) {
            System.out.println("There was an error performing an operation.\n");
            e.printStackTrace();
        }
    }
}
