package com.example.melikyan.charity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase ann=getBaseContext().openOrCreateDatabase("charity.db",MODE_PRIVATE,null);
                ann.execSQL("create table if not exists annouc (name TEXT,images TEXT,money_neede INTEGER,money_come INTEGER);");
                ann.execSQL("create table if not exists user (Login TEXT,Name TEXT,Lastname TEXT,Region TEXT,Age TEXT);");
                Cursor query=ann.rawQuery("SELECT * FROM user;",null);
                if(!query.moveToNext()){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    query.close();
                    ann.close();
                }else{
                    Intent intent = new Intent(MainActivity.this, AnnoucmentsActivity.class);
                    startActivity(intent);
                    query.close();
                    ann.close();
                }
            }
        },3000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
