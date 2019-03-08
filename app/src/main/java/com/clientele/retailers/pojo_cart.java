package com.clientele.retailers;

/**
 * Created by Royal on 08-Mar-18.
 */

public class pojo_cart {
    int cart_id;
    int product_id;
    int product_qty;

    public pojo_cart(int cart_id,int product_id, int product_qty) {
        this.product_id = product_id;
        this.product_qty = product_qty;
        this.cart_id=cart_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(int product_qty) {
        this.product_qty = product_qty;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }
}
