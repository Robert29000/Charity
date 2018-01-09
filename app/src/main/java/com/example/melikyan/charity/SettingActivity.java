package com.example.melikyan.charity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SettingActivity extends AppCompatActivity {
    private EditText firstname,lastname,age,login,password;
    private AutoCompleteTextView region;
    private SharedPreferences ss;
    private String  pass,log,reg,ag,last,first;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);
        region=findViewById(R.id.region);
        String[] countries=getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,countries);
        region.setAdapter(adapter);

    }

    public void SaveSettings(View view) {
        firstname=findViewById(R.id.firstname);
        first=firstname.getText().toString();
        lastname=findViewById(R.id.lastname);
        last=lastname.getText().toString();
        age=findViewById(R.id.age);
        ag=age.getText().toString();
        region=findViewById(R.id.region);
        reg=region.getText().toString();
        login=findViewById(R.id.editText3);
        log=login.getText().toString();
        password=findViewById(R.id.editText4);
        pass=password.getText().toString();
        if(first.equals("") || last.equals("") || ag.equals("") || reg.equals("") || log.equals("") || pass.equals("")) {
            new Toast(SettingActivity.this).makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show();
        }
        else{
            new Connect().execute("0"+",'"+log+"','"+pass+"','"+first+"','"+last+"','"+reg+"','"+ag+"'");
        }
        mHandler=new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                new Toast(SettingActivity.this).makeText(SettingActivity.this, "Логин уже существует", Toast.LENGTH_LONG).show();
            }
        };
    }
    class Connect extends AsyncTask<String,Void,Void>{


        @Override
        protected Void doInBackground(String... strings) {
           try {
               Socket s = new Socket("192.168.0.112", 3345);
               DataOutputStream out = new DataOutputStream(s.getOutputStream());
               out.writeUTF(strings[0]);
               DataInputStream in=new DataInputStream(s.getInputStream());
               int res=in.readInt();
               if(res==0) {
                   Message message = mHandler.obtainMessage();
                   message.sendToTarget();
                   return null;
               }else{
                   SQLiteDatabase ann=getBaseContext().openOrCreateDatabase("charity.db",MODE_PRIVATE,null);
                   ann.execSQL("INSERT INTO user(Login,Name,Lastname,Region,Age) VALUES ('"+login+"','"+first+"','"+last+"','"+reg+"','"+ag+"');");
                   ann.close();
                   Intent intent=new Intent(SettingActivity.this,AnnoucmentsActivity.class);
                   startActivity(intent);
               }
           }
           catch (IOException e){

           }
           return null;
        }
    }
}
