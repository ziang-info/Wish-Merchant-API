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

import java.util.ArrayList;
import org.acein.wish.WishClient;

public class GetAllProducts {

    public static void main(String[] args) throws Exception {

        // ACCESS_TOKEN
        final String token = "ACCESS_TOKEN";
        WishClient client = new WishClient(token, "sandbox", null);

        //Get an array of all your products
        ArrayList products = client.getAllProducts();
        System.out.println(products.size());

        //Get an array of all product variations
        ArrayList product_vars = client.getAllProductVariations();
        System.out.println(product_vars.size());

    }
}
