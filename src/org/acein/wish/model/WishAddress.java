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

import java.util.Hashtable;

public class WishAddress {

    private String street_address1;
    private String street_address2;
    private String city;
    private String state;
    private String zipcode;
    private String country;
    private String phone_number;

    public WishAddress(String addr1, String addr2, String city, String state, String zip, String country, String phone) {
        this.street_address1 = addr1;
        this.street_address2 = addr2;
        this.city = city;
        this.state = state;
        this.zipcode = zip;
        this.country = country;
        this.phone_number = phone;
    }

    public Hashtable getParams() {
        Hashtable params = new Hashtable();
        params.put("street_address1", this.street_address1);
        params.put("street_address2", this.street_address2);
        params.put("city", this.city);
        params.put("state", this.state);
        params.put("zipcode", this.zipcode);
        params.put("country", this.country);
        params.put("phone_number", this.phone_number);
        return params;
    }

}
