/**
 * Copyright 2014 Wish.com, ContextLogic or its affiliates. All Rights Reserved.
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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.acein.wish.WishAuth;
import org.acein.wish.WishClient;
import org.acein.wish.WishResponse;
import org.json.JSONObject;

public class Connect {

    public static void main(String[] args) {
        try {

            System.out.println("Wish Merchant SDK for Java");

            //final String MERCHANT_ID = "5639efb62b296c053f977d3f";
            final String CODE = "c390157fdfe7430b9b667ae75fa22f8a";
            final String CLIENT_ID = "563e05a87845545a231642dd"; 
            final String CLIENT_SECRET = "1ae400af836547d09aba562c287eee3a"; 

            WishAuth auth = new WishAuth(CLIENT_ID, CLIENT_SECRET, "sandbox");

            // ACCESS_TOKEN
            WishResponse response = auth.getToken(CODE, "https://www.ziang.info:8443/Test/wish/callback");

            String token = null;

            JSONObject data = response.getData();
            if (data.has("access_token")) {
                token = data.getString("access_token");

                WishClient client = new WishClient(token, "sandbox", null);

                String result = "RESULT: " + client.authTest();

                System.out.println(result);
            } else {
                System.out.println("No Access token get.");
            }
        } catch (Exception ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
