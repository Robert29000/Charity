package com.example.melikyan.charity;


import android.graphics.Bitmap;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by melikyan on 08.03.2018.
 */
@IgnoreExtraProperties
public class UsersAnnotations {
    public String domain,name,text;
    public int moneyincome,moneyneeded,index,imagescount;
    public String uid;
    public Bitmap bitmap;
    public UsersAnnotations(String domain,int moneyincome,int moneyneeded,String name,String text,int imagescount){
        this.domain=domain;
        this.moneyincome=moneyincome;
        this.moneyneeded=moneyneeded;
        this.name=name;
        this.text=text;
        this.imagescount=imagescount;
    }
    UsersAnnotations(){

    }
}
