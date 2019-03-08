package com.clientele.retailers;

import android.graphics.Bitmap;

/**
 * Created by Royal on 11-Feb-18.
 */

public class pojo_category {
    private String name;
    private String image;
    private int id;
    String desc;
    public pojo_category(String name, String image, int id, String desc){
        this.name=name;
        this.image=image;
        this.id=id;
        this.desc=desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
