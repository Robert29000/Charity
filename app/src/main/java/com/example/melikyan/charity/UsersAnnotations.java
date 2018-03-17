package com.example.melikyan.charity;


import android.graphics.Bitmap;

/**
 * Created by melikyan on 08.03.2018.
 */

public class UsersAnnotations {
    public String domain,name,text;
    public int moneyincome,moneyneeded;
    public String uid;
    public Bitmap bitmap;
    UsersAnnotations(String domain,int moneyincome,int moneyneeded,String name,String text){
        this.domain=domain;
        this.moneyincome=moneyincome;
        this.moneyneeded=moneyneeded;
        this.name=name;
        this.text=text;
    }
    UsersAnnotations(){

    }
}
