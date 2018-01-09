package com.example.melikyan.charity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class LoginActivity extends AppCompatActivity {
    private String log,passwd;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void createProf(View view) {
        Intent intent=new Intent(this,SettingActivity.class);
        startActivity(intent);
    }

    public void ENTER(View view) {
        EditText login=findViewById(R.id.Login);
        log=login.getText().toString();
        EditText password=findViewById(R.id.Password);
        passwd=password.getText().toString();
        if(log.equals("") || passwd.equals(""))
            new Toast(LoginActivity.this).makeText(this,"Заполните все поля",Toast.LENGTH_LONG).show();
        else new Connection().execute("1"+","+log+","+passwd);
        mHandler=new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                new Toast(LoginActivity.this).makeText(LoginActivity.this,"Неверный логин или пароль",Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    class Connection extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
           try {
               Socket s = new Socket("192.168.0.112", 3345);
               DataOutputStream out=new DataOutputStream(s.getOutputStream());
               out.writeUTF(strings[0]);
               DataInputStream in=new DataInputStream(s.getInputStream());
               String res=in.readUTF();
               if(res.equals("0")){
                   Message message = mHandler.obtainMessage();
                   message.sendToTarget();
                   return null;
               }else{
                   SQLiteDatabase ann=getBaseContext().openOrCreateDatabase("charity.db",MODE_PRIVATE,null);
                   int r=0;
                   int counter=0;
                   for(int i=0;i<res.length();i++){
                       if(res.charAt(i)==','){
                           counter++;
                           switch(counter){
                               case 1:
                                   ann.execSQL("INSERT INTO user(Name) VALUES ('"+res.substring(0,i)+"');");
                                   r=i;
                                   break;
                               case 2:
                                   ann.execSQL("INSERT INTO user(Lastname) VALUES ('"+res.substring(r+1,i)+"');");
                                   r=i;
                                   break;
                               case 3:
                                   ann.execSQL("INSERT INTO user(Region) VALUES ('"+res.substring(r+1,i)+"');");
                                   r=i;
                                   break;
                               case 4:
                                   ann.execSQL("INSERT INTO user(Age) VALUES ('"+res.substring(r+1,i)+"');");
                                   r=i;
                                   break;
                           }

                       }
                   }
                   ann.execSQL("INSERT INTO user(Login) VALUES ('"+log+"');");
                   Intent intent=new Intent(LoginActivity.this,AnnoucmentsActivity.class);
                   startActivity(intent);
               }
           }
           catch (IOException e){

           }
           return null;
        }

    }
}
