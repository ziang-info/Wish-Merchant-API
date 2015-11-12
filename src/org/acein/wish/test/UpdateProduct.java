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

import org.acein.wish.WishClient;
import org.acein.wish.model.WishProduct;
import org.acein.wish.model.WishProductVariation;

public class UpdateProduct {

    public static void main(String[] args) throws Exception {

        // ACCESS_TOKEN
        final String token = "ACCESS_TOKEN";
        WishClient client = new WishClient(token, "sandbox", null);

        try {
            //Get your product by its ID
            WishProduct product = client.getProductById("535d205bb9ee84128ac15fc0");
            System.out.println(product);

            //Get your product variation by its SKU
            WishProductVariation product_var = client.getProductVariationBySKU("var 7");
            System.out.println(product_var);

            //Enable or disable your product variation
            client.enableProductVariation(product_var);
            //or $client.enableProductVariationBySKU("var 6");
            client.disableProductVariation(product_var);
            //or $client.disableProductVariationBySKU("var 6");

            //Update your product variation and save it
            product_var.setInventory(10);
            client.updateProductVariation(product_var);
        } catch (Exception e) {
            System.out.println("There was an error performing an operation.\n");
            e.printStackTrace();
        }

    }
}
