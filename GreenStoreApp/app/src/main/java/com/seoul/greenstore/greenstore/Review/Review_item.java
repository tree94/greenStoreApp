package com.seoul.greenstore.greenstore.Review;

import java.util.Date;

/**
 * Created by X on 2016-09-26.
 */
public class Review_item {
    int rkey;
    int mkey;
    int sh_id;
    String rcontents;
    int relike;
    Date rdate;
    String store_name;

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public Review_item() {
    }

    public Review_item(int rkey, int mkey, int sh_id, String rcontents, int relike, Date rdate) {
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

    public int getSh_id() {
        return sh_id;
    }

    public void setSh_id(int sh_id) {
        this.sh_id = sh_id;
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
