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
import java.util.Date;
import org.acein.wish.WishClient;
import org.acein.wish.model.WishOrder;
import org.acein.wish.model.WishReason;
import org.acein.wish.model.WishTracker;

public class GetAllOrders {

    public static void main(String[] args) throws Exception {

        // ACCESS_TOKEN
        final String token = "ACCESS_TOKEN";
        WishClient client = new WishClient(token, "sandbox", null);

        //Fulfill one order by ID
        //$tracker = new WishTracker("USPS","123123123","Thanks for buying!");
        //$client.fulfillOrderById("537850c38b72ac0db9472cb4",$tracker);
        //or 
        //$one_order = $client.getOrderById("53767deb43d2470c6d04d856");
        //$client.fulfillOrder($one_order);
        //Get an array of all changed orders since January 20, 2010
        ArrayList changed_orders = client.getAllChangedOrdersSince(new Date("2010-01-20"));
        System.out.println(changed_orders.size() + " changed orders.\n");

        //Get an array of all unfufilled orders since January 20, 2010
        ArrayList unfulfilled_orders = client.getAllUnfulfilledOrdersSince(new Date("2010-01-20"));
        System.out.println(unfulfilled_orders.size() + " changed orders.\n");

        //Fulfill all unfulfilled orders
        for (int i = 0; i < unfulfilled_orders.size(); i++) {
            WishOrder order = (WishOrder) unfulfilled_orders.get(i);
            try {

                //Generate your own tracking information here:"
                WishTracker tracker = new WishTracker("USPS", "123123123", "Thanks for buying!");
                //Fulfill the order using the tracking information
                client.fulfillOrder(order, tracker);
            } catch (Exception e) {
                System.out.println("Order " + order.getOrderId() + " already fulfilled.\n");
                e.printStackTrace();
            }
        }

        //Update tracking information
        WishTracker tracker = new WishTracker("USPS", "123123123", "Thanks for buying!");
        client.updateTrackingInfoById("53785043482e680c58a08f53", tracker);
        //or 
        //$one_order = $client.getOrderById("53767deb43d2470c6d04d856");
        //$client.updateTrackingInfo($one_order,$tracker);

        //Refund an order
        client.refundOrderById("537850c38b72ac0db9472cb4",
                WishReason.NO_MORE_INVENTORY, null);
    }
}
