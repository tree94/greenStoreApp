package com.seoul.greenstore.greenstore.Dto;

import java.util.Date;

/**
 * Created by X on 2016-09-24.
 */

public class Notice  {

    private int nkey;
    private String ntitle;
    private String ncontent;
    private Date ndate;


    public int getNkey() {
        return nkey;
    }
    public void setNkey(int nkey) {
        this.nkey = nkey;
    }
    public String getNtitle() {
        return ntitle;
    }
    public void setNtitle(String ntitle) {
        this.ntitle = ntitle;
    }
    public String getNcontent() {
        return ncontent;
    }
    public void setNcontent(String ncontent) {
        this.ncontent = ncontent;
    }
    public Date getNdate() {
        return ndate;
    }
    public void setNdate(Date ndate) {
        this.ndate = ndate;
    }

}
