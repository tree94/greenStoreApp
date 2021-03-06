package com.seoul.greenstore.greenstore.Review;

import java.util.Date;

/**
 * Created by X on 2016-09-26.
 */
public class Review_item {
    private int rkey;
    private int mkey;
    private int sh_id;
    private String rcontents;
    private int relike;
    private Date rdate;
    private String storeName;
    private String image;
    private String sh_addr;
    private String induty_name;
    private int induty;
    private String mname;
    private String dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public int getSh_id() {
        return sh_id;
    }

    public void setSh_id(int sh_id) {
        this.sh_id = sh_id;
    }

    public int getInduty() {
        return induty;
    }

    public void setInduty(int induty) {
        this.induty = induty;
    }

    public String getInduty_name() {
        return induty_name;
    }

    public void setInduty_name(String induty_name) {
        this.induty_name = induty_name;
    }

    public String getSh_addr() {
        return sh_addr;
    }

    public void setSh_addr(String sh_addr) {
        this.sh_addr = sh_addr;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Review_item() {
    }

    public Review_item(int rkey, int mkey, int sh_id, String rcontents, int relike, Date rdate, String sh_addr) {
        this.rkey = rkey;
        this.mkey = mkey;
        this.sh_id = sh_id;
        this.rcontents = rcontents;
        this.relike = relike;
        this.rdate = rdate;
    }

    public int getRkey() {
        return rkey;
    }

    public void setRkey(int rkey) {
        this.rkey = rkey;
    }

    public int getMkey() {
        return mkey;
    }

    public void setMkey(int mkey) {
        this.mkey = mkey;
    }


    public String getRcontents() {
        return rcontents;
    }

    public void setRcontents(String rcontents) {
        this.rcontents = rcontents;
    }

    public int getRelike() {
        return relike;
    }

    public void setRelike(int relike) {
        this.relike = relike;
    }

    public Date getRdate() {
        return rdate;
    }

    public void setRdate(Date rdate) {
        this.rdate = rdate;
    }
}
