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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class WishTracker {

    private String tracking_provider;
    private String tracking_number;
    private String ship_note;

    public WishTracker(String tracking_provider, String tracking_number, String ship_note) {

        this.tracking_provider = tracking_provider;

        if (tracking_number != null) {
            this.tracking_number = tracking_number;
        }

        if (ship_note != null) {
            this.ship_note = ship_note;
        }
    }

    public Hashtable getParams() {
        Hashtable params = new Hashtable();

        params.put("tracking_provider", this.tracking_provider);
        params.put("tracking_number", this.tracking_number);
        params.put("ship_note", this.ship_note);
        return params;
    }

}
