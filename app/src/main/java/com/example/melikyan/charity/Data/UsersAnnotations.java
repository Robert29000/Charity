package com.example.melikyan.charity.Data;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by melikyan on 08.03.2018.
 */
@IgnoreExtraProperties
public class UsersAnnotations implements Parcelable {
    public String name,text,bimapUri;
    public int moneyincome,moneyneeded,imagescount;
    public long yandexwallet;
    public String uid;
    public UsersAnnotations(int moneyincome,int moneyneeded,String name,String text,int imagescount,long yandexwallet){
        this.moneyincome=moneyincome;
        this.moneyneeded=moneyneeded;
        this.name=name;
        this.text=text;
        this.imagescount=imagescount;
        this.yandexwallet=yandexwallet;
    }
    UsersAnnotations(){

    }

    public UsersAnnotations(Parcel in){
        uid=in.readString();
        name=in.readString();
        text=in.readString();
        moneyneeded=in.readInt();
        moneyincome=in.readInt();
        imagescount=in.readInt();
        yandexwallet=in.readLong();
        bimapUri=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeString(text);
        dest.writeInt(moneyneeded);
        dest.writeInt(moneyincome);
        dest.writeInt(imagescount);
        dest.writeLong(yandexwallet);
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
