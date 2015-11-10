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

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.acein.wish.WishAuth;
import org.acein.wish.WishClient;
import org.acein.wish.WishResponse;

public class Connect {

    public static void main(String[] args) {
        try {

            System.out.println("Wish Merchant SDK for Java");

            final String MERCHANT_ID="5639efb62b296c053f977d3f";
            final String CLIENT_ID = "563e05a87845545a231642dd";
            final String CLIENT_SECRET = "1ae400af836547d09aba562c287eee3a";
            
            WishAuth auth = new WishAuth(CLIENT_ID, CLIENT_SECRET, "sandbox");

            WishResponse response = auth.getToken("ACCESS_TOKEN", "https://www.acein.net/ymt/callback.jsp");

            String token = (String) ((Hashtable) response.getData()).get("access_token");

            WishClient client = new WishClient(token, "sandbox", null);

            String result = "RESULT: " + client.authTest();

            System.out.println(result);
        } catch (Exception ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
