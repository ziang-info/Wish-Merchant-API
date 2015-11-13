/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.acein.wish.model;

import org.json.JSONObject;

/**
 *
 * @author lattimore
 */
public class WishProductTag {
    /*
    {'Tag': {'id': 'red','name': 'red'}},
    */
    
    private String id;
    private String name;

    WishProductTag(JSONObject jo) {
        this.id = jo.getString("id");
        this.name = jo.getString("name");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
