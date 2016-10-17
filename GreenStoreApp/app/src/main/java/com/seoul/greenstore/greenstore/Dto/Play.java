package com.seoul.greenstore.greenstore.Dto;

import android.location.Location;

/**
 * Created by user on 2016-10-17.
 */
public class Play {
    private String playPhoto;   //주변 이미지
    private String playTitle;   //주변 이름
    private String playAddr;    //주변 주소
    private Location playLoc;   //주변 좌표, 좌표 때문에 dto 생성함.

    public String getPlayPhoto() {
        return playPhoto;
    }

    public void setPlayPhoto(String playPhoto) {
        this.playPhoto = playPhoto;
    }

    public String getPlayTitle() {
        return playTitle;
    }

    public void setPlayTitle(String playTitle) {
        this.playTitle = playTitle;
    }

    public String getPlayAddr() {
        return playAddr;
    }

    public void setPlayAddr(String playAddr) {
        this.playAddr = playAddr;
    }

    public Location getPlayLoc() {
        return playLoc;
    }

    public void setPlayLoc(Location playLoc) {
        this.playLoc = playLoc;
    }
}
