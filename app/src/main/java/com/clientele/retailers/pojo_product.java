package com.clientele.retailers;

import java.util.List;

/**
 * Created by Royal on 24-Feb-18.
 */

public class pojo_product {
    int productId;
    String productName;
    String categoryName;
    String SubcategoryName;
    String productImage;
    private List<pojo_product_details> lst;

    public pojo_product(int productId,String productName, String categoryName, String subcategoryName, String productImage,List<pojo_product_details> lst) {
        this.productId=productId;
        this.productName = productName;
        this.categoryName = categoryName;
        SubcategoryName = subcategoryName;
        this.productImage = productImage;
        this.lst=lst;
    }

    public  int getProductId(){
        return productId;
    }
    public void setProductId(int productId){
        this.productId=productId;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubcategoryName() {
        return SubcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        SubcategoryName = subcategoryName;
    }


    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }


    public List<pojo_product_details> getLst() {
        return lst;
    }

    public void setLst(List<pojo_product_details> lst) {
        this.lst = lst;
    }
}
