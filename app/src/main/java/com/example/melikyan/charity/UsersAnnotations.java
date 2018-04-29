package com.example.melikyan.charity;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by melikyan on 08.03.2018.
 */
@IgnoreExtraProperties
public class UsersAnnotations implements Parcelable {
    public String domain,name,text,bimapUri;
    public int moneyincome,moneyneeded,imagescount,wallet;
    public String uid;
    public UsersAnnotations(String domain,int moneyincome,int moneyneeded,String name,String text,int imagescount,int wallet){
        this.domain=domain;
        this.moneyincome=moneyincome;
        this.moneyneeded=moneyneeded;
        this.name=name;
        this.text=text;
        this.imagescount=imagescount;
        this.wallet=wallet;
    }
    UsersAnnotations(){

    }

    public UsersAnnotations(Parcel in){
        domain=in.readString();
        name=in.readString();
        text=in.readString();
        moneyneeded=in.readInt();
        moneyincome=in.readInt();
        imagescount=in.readInt();
        wallet=in.readInt();
        bimapUri=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(domain);
        dest.writeString(name);
        dest.writeString(text);
        dest.writeInt(moneyneeded);
        dest.writeInt(moneyincome);
        dest.writeInt(imagescount);
        dest.writeInt(wallet);
        dest.writeString(bimapUri);
    }

    public static final Creator<UsersAnnotations> CREATOR=new Creator<UsersAnnotations>() {
        @Override
        public UsersAnnotations createFromParcel(Parcel source) {
            return new UsersAnnotations(source);
        }

        @Override
        public UsersAnnotations[] newArray(int size) {
            return new UsersAnnotations[size];
        }
    };
}
