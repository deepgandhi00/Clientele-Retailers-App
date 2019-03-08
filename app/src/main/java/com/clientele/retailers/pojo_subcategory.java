package com.clientele.retailers;

import android.graphics.Bitmap;

/**
 * Created by Royal on 13-Feb-18.
 */

public class pojo_subcategory {
    private String sub_name;
    private String sub_image;
    private int sub_id;
    String sub_desc;

    public pojo_subcategory(String sub_name, String sub_image, int sub_id, String sub_desc) {
        this.sub_name = sub_name;
        this.sub_image = sub_image;
        this.sub_id = sub_id;
        this.sub_desc = sub_desc;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_image() {
        return sub_image;
    }

    public void setSub_image(String sub_image) {
        this.sub_image = sub_image;
    }

    public int getSub_id() {
        return sub_id;
    }

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
    }

    public String getSub_desc() {
        return sub_desc;
    }

    public void setSub_desc(String sub_desc) {
        this.sub_desc = sub_desc;
    }
}
