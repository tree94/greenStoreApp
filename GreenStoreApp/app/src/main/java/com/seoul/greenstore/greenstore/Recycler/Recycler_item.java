package com.seoul.greenstore.greenstore.Recycler;

/**
 * Created by X on 2016-09-06.
 */
public class Recycler_item {
    private int id;
    private int like;
    private String addr;
    private String image;
    private String name;
    private int indutyCode;

    public Recycler_item() {
    }

    public Recycler_item(int id, int like, String addr, String image, String name) {
        this.id = id;
        this.like = like;
        this.addr = addr;
        this.image = image;
        this.name = name;
    }

    public int getIndutyCode() {
        return indutyCode;
    }

    public void setIndutyCode(int indutyCode) {
        this.indutyCode = indutyCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
