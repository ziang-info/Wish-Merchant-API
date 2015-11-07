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
package org.acein.wish;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import org.acein.wish.exception.ConnectionException;
import org.acein.wish.exception.InvalidArgumentException;
import org.acein.wish.exception.OrderAlreadyFulfilledException;
import org.acein.wish.exception.ServiceResponseException;
import org.acein.wish.exception.UnauthorizedRequestException;
import org.acein.wish.model.WishAddress;
import org.acein.wish.model.WishOrder;
import org.acein.wish.model.WishProduct;
import org.acein.wish.model.WishProductVariation;
import org.acein.wish.model.WishTicket;
import org.acein.wish.model.WishTracker;

public class WishClient {

    private WishSession session;
    private ArrayList products;
    private ArrayList orders;

    private final int LIMIT = 50;

    // session_type="prod"
    public WishClient(String access_token, String session_type, String merchant_id) {
        this.session = new WishSession(access_token, session_type, merchant_id);
    }

    public WishResponse getResponse(String type, String path, Hashtable params)
            throws Exception, UnauthorizedRequestException, InvalidArgumentException, ServiceResponseException {

        WishRequest request = new WishRequest(this.session, type, path, params);

        WishResponse response = request.execute();
        if (response.getStatusCode() == 4000) {
            throw new UnauthorizedRequestException("Unauthorized access",
                    request,
                    response);
        }
        if (response.getStatusCode() == 1015) {
            throw new UnauthorizedRequestException("Access Token expired",
                    request,
                    response);
        }
        if (response.getStatusCode() == 1016) {
            throw new UnauthorizedRequestException("Access Token revoked",
                    request,
                    response);
        }
        if (response.getStatusCode() == 1000) {
            throw new ServiceResponseException("Invalid parameter",
                    request,
                    response);
        }
        if (response.getStatusCode() == 1002) {
            throw new OrderAlreadyFulfilledException("Order has been fulfilled",
                    request,
                    response);
        }
        if (response.getStatusCode() != 0) {
            throw new ServiceResponseException("Unknown error",
                    request,
                    response);
        }
        return response;

    }

    public ArrayList getResponseIter(String method, String uri, Class getClass, Hashtable params) throws Exception, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception, InvalidArgumentException, ServiceResponseException {
        int start = 0;
        params.put("limit", LIMIT);

        ArrayList class_arr = new ArrayList(); //array();

        WishResponse response = this.getResponse(method, uri, params);

        do {
            params.put("start", start);

            Iterator it = ((ArrayList) response.getData()).iterator();

            while (it.hasNext()) {
                Hashtable class_raw = (Hashtable) it.next();
                //class_arr[] = new getClass(class_raw);
                class_arr.add(getClass.getConstructor(Hashtable.class).newInstance(class_raw));
            }
            start += LIMIT;
        } while (response.hasMore());

        return class_arr;
    }

    public String authTest() throws Exception, InvalidArgumentException, ServiceResponseException {
        WishResponse response = this.getResponse("GET", "auth_test", null);
        return "success";

    }

    // PRODUCT
    public WishProduct getProductById(String id) throws Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable params = new Hashtable();
        params.put("id", id);

        WishResponse response = this.getResponse("GET", "product", params);
        return new WishProduct((Hashtable) response.getData());
    }

    public WishProduct createProduct(Hashtable object) throws Exception, InvalidArgumentException, ServiceResponseException {
        WishResponse response = this.getResponse("POST", "product/add", object);
        return new WishProduct((Hashtable) response.getData());
    }

    public String updateProduct(WishProduct product) throws Exception, InvalidArgumentException, ServiceResponseException {

        String[] params = {"id",
            "name",
            "description",
            "tags",
            "brand",
            "landing_page_url",
            "upc",
            "main_image",
            "extra_images"};

        Hashtable object = product.getParams(new ArrayList<String>(Arrays.asList(params)));

        WishResponse response = this.getResponse("POST", "product/update", object);

        return "success";
    }

    public void enableProduct(WishProduct product) throws Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable p = product.getParams(new ArrayList<String>(Arrays.asList(new String[]{"id"})));
        this.enableProductById((String) p.get("id"));//product.id);
    }

    public String enableProductById(String id) throws Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable params = new Hashtable();
        params.put("id", id);

        WishResponse response = this.getResponse("POST", "product/enable", params);
        return "success";
    }

    public void disableProduct(WishProduct product) throws Exception, InvalidArgumentException, ServiceResponseException {
        this.disableProductById(product.getId());
    }

    public String disableProductById(String id) throws Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable params = new Hashtable();
        params.put("id", id);

        WishResponse response = this.getResponse("POST", "product/disable", params);
        return "success";
    }

    public ArrayList getAllProducts() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception, InvalidArgumentException, ServiceResponseException {
        return this.getResponseIter("GET", "product/multi-get", WishProduct.class, null); // "Wish/Model/WishProduct"
    }

    public String removeExtraImages(WishProduct product) throws Exception, InvalidArgumentException, ServiceResponseException {
        return this.removeExtraImagesById(product.getId());
    }

    public String removeExtraImagesById(String id) throws Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable params = new Hashtable();
        params.put("id", id);

        WishResponse response = this.getResponse("POST", "product/remove-extra-images", params);
        return "success";
    }

    // PRODUCT VARIATION
    public WishProductVariation createProductVariation(Hashtable object) throws Exception, InvalidArgumentException, ServiceResponseException {
        WishResponse response = this.getResponse("POST", "variant/add", object);
        return new WishProductVariation((Hashtable) response.getData());
    }

    public WishProductVariation getProductVariationBySKU(String sku) throws Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable params = new Hashtable();
        params.put("sku", sku);

        WishResponse response = this.getResponse("GET", "variant", params);
        return new WishProductVariation((Hashtable) response.getData());
    }

    public String updateProductVariation(WishProductVariation var) throws Exception, InvalidArgumentException, ServiceResponseException {

        String[] pStrs = new String[]{
            "sku",
            "inventory",
            "price",
            "shipping",
            "enabled",
            "size",
            "color",
            "msrp",
            "shipping_time",
            "main_image"
        };

        ArrayList pAL = new ArrayList(Arrays.asList(pStrs));

        Hashtable params = var.getParams(pAL);

        WishResponse response = this.getResponse("POST", "variant/update", params);
        return "success";
    }

    public void enableProductVariation(WishProductVariation var) throws Exception, InvalidArgumentException, ServiceResponseException {
        this.enableProductVariationBySKU(var.getSku());
    }

    public String enableProductVariationBySKU(String sku) throws Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable params = new Hashtable();
        params.put("sku", sku);

        WishResponse response = this.getResponse("POST", "variant/enable", params);
        return "success";
    }

    public void disableProductVariation(WishProductVariation var) throws Exception, InvalidArgumentException, ServiceResponseException {
        this.disableProductVariationBySKU(var.getSku());
    }

    public String disableProductVariationBySKU(String sku) throws Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable params = new Hashtable();
        params.put("sku", sku);

        WishResponse response = this.getResponse("POST", "variant/disable", params);

        return "success";
    }

    public String updateInventoryBySKU(String sku, String newInventory) throws Exception, InvalidArgumentException, ServiceResponseException {

        Hashtable params = new Hashtable();
        params.put("sku", sku);
        params.put("inventory", newInventory);

        WishResponse response = this.getResponse("POST", "variant/update-inventory", params);
        return "success";
    }

    public ArrayList getAllProductVariations() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception, InvalidArgumentException, ServiceResponseException {
        return this.getResponseIter(
                "GET",
                "variant/multi-get",
                WishProductVariation.class, null); // "Wish/Model/WishProductVariation"
    }

    // ORDER
    public WishOrder getOrderById(String id) throws Exception, InvalidArgumentException, ServiceResponseException {

        Hashtable params = new Hashtable();
        params.put("id", id);

        WishResponse response = this.getResponse("GET", "order", params);
        return new WishOrder((Hashtable) response.getData());
    }

    public ArrayList getAllChangedOrdersSince(Date time) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception, InvalidArgumentException, ServiceResponseException {

        Hashtable params = new Hashtable();
        if (time != null) {
            params.put("since", time);
        }

        // "Wish/Model/WishOrder"
        return this.getResponseIter(
                "GET",
                "order/multi-get",
                WishOrder.class,
                params);
    }

    public ArrayList getAllUnfulfilledOrdersSince(Date time) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable params = new Hashtable();
        if (time != null) {
            params.put("since", time);
        }

        // "Wish/Model/WishOrder"
        return this.getResponseIter(
                "GET",
                "order/get-fulfill",
                WishOrder.class,
                params);
    }

    public String fulfillOrderById(String id, WishTracker tracking_info) throws Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable params = tracking_info.getParams();
        params.put("id", id); //params["id"]=id;
        WishResponse response = this.getResponse("POST", "order/fulfill-one", params);
        return "success";
    }

    public String fulfillOrder(WishOrder order, WishTracker tracking_info) throws Exception, InvalidArgumentException, ServiceResponseException {
        return this.fulfillOrderById(order.getId(), tracking_info);
    }

    public String refundOrderById(String id, String reason, String note) throws Exception, InvalidArgumentException, ServiceResponseException {

        Hashtable params = new Hashtable();
        params.put("id", id);
        params.put("reason_code", reason);
        if (note != null) {
            params.put("reason_note", note);
        }

        WishResponse response = this.getResponse("POST", "order/refund", params);
        return "success";
    }

    public String refundOrder(WishOrder order, String reason, String note) throws Exception, InvalidArgumentException, ServiceResponseException {
        return refundOrderById(order.getId(), reason, note);
    }

    public String updateTrackingInfo(WishOrder order, WishTracker tracker) throws Exception, InvalidArgumentException, ServiceResponseException {
        return this.updateTrackingInfoById(order.getId(), tracker);
    }

    public String updateTrackingInfoById(String id, WishTracker tracker) throws Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable params = tracker.getParams();
        params.put("id", id);

        WishResponse response = this.getResponse("POST", "order/modify-tracking", params);
        return "success";
    }

    public String updateShippingInfo(WishOrder order, WishAddress address) throws Exception, InvalidArgumentException, ServiceResponseException {
        return this.updateShippingInfoById(order.getId(), address);
    }

    public String updateShippingInfoById(String id, WishAddress address) throws Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable params = address.getParams();
        params.put("id", id);
        WishResponse response = this.getResponse("POST", "order/change-shipping", params);
        return "success";
    }

    // TICKET
    public WishTicket getTicketById(String id) throws Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable params = new Hashtable();
        params.put("id", id);

        WishResponse response = this.getResponse("GET", "ticket", params);
        return new WishTicket((Hashtable) response.getData());
    }

    public ArrayList getAllActionRequiredTickets() throws NoSuchMethodException, InstantiationException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception, InvalidArgumentException, ServiceResponseException {
        // "Wish/Model/WishTicket"
        return this.getResponseIter(
                "GET",
                "ticket/get-action-required",
                WishTicket.class, null);
    }

    public String replyToTicketById(String id, String reply) throws Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable params = new Hashtable();
        params.put("id", id);
        params.put("reply", reply);
        WishResponse response = this.getResponse("POST", "ticket/reply", params);
        return "success";
    }

    public String closeTicketById(String id) throws Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable params = new Hashtable();
        params.put("id", id);

        WishResponse response = this.getResponse("POST", "ticket/close", params);
        return "success";
    }

    public String appealTicketById(String id) throws Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable params = new Hashtable();
        params.put("id", id);

        WishResponse response = this.getResponse("POST", "ticket/appeal-to-wish-support", params);
        return "success";
    }

    public String reOpenTicketById(String id, String reply) throws Exception, InvalidArgumentException, ServiceResponseException {
        Hashtable params = new Hashtable();
        params.put("id", id);
        params.put("reply", reply);

        WishResponse response = this.getResponse("POST", "ticket/re-open", params);
        return "success";
    }

}
