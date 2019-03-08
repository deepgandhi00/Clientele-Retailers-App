package com.clientele.retailers;

/**
 * Created by Royal on 08-Mar-18.
 */

public class pojo_cart_product {
    int cid;
    String product_image;
    String product_name;
    String product_qty;
    String product_wt;
    String cost;
    int qty;

    public pojo_cart_product(int cid,String product_image,String product_name, String product_qty,String product_wt, String cost,int qty) {
        this.cid=cid;
        this.product_image=product_image;
        this.product_name = product_name;
        this.product_qty=product_qty;
        this.product_wt=product_wt;
        this.cost = cost;
        this.qty=qty;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }



    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public String getProduct_wt() {
        return product_wt;
    }

    public void setProduct_wt(String product_wt) {
        this.product_wt = product_wt;
    }
}
