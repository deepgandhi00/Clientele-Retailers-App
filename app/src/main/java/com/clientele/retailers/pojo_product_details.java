package com.clientele.retailers;

import java.util.List;

/**
 * Created by Royal on 25-Feb-18.
 */

public class pojo_product_details {
    int id;
    String qty;
    String wt;
    String cost;

    public pojo_product_details(){

    }
    public pojo_product_details(int id,String qty,String wt,String cost){
        this.id=id;
        this.qty=qty;
        this.wt=wt;
        this.cost=cost;
    }

    public int getIds() {
        return id;
    }

    public void setIds(int id) {
        this.id = id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setWt(String wt){ this.wt=wt; }

    public String getWt(){ return wt; }
}
