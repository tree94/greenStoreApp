package com.seoul.greenstore.greenstore.Recycler;

/**
 * Created by X on 2016-09-06.
 */
public class Recycler_item {
    int image;
    String title;

    int getImage(){
        return this.image;
    }
    String getTitle(){
        return this.title;
    }

    public Recycler_item(int image, String title){
        this.image=image;
        this.title=title;
    }
}
