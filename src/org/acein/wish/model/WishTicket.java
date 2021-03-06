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

public class WishTicket {

    private Hashtable tickets = new Hashtable();

    public WishTicket(Hashtable tickets) {
        this.tickets = tickets;

        /*
         $vars = get_object_vars($ticket);
         foreach ($vars as $key=>$val){
         $this->$key = $val;
         }
         */
    }

    public Hashtable getParams2(ArrayList keys) {
        Hashtable params = new Hashtable();

        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key != null) {
                params.put(key, tickets.get(key));
            }
        }
        return params;
    }

}
