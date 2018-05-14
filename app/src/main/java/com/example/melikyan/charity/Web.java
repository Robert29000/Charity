package com.example.melikyan.charity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.melikyan.charity.Data.UsersAnnotations;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class Web extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
        setContentView(R.layout.activity_web_view);
        final WebView webView=findViewById(R.id.webview);
        final int money=getIntent().getIntExtra("MONEY",0);
        final UsersAnnotations user=getIntent().getParcelableExtra("USER");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("EASY",url);
                if(url.indexOf("/pro")!=-1) {
                    if (url.substring(0, url.indexOf("/pro")).equals("https://paymentcard.yamoney.ru")) {
                        Log.d("EASY","DGSD");
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        database.child("Annoucments").child(user.uid).child("moneyincome").setValue(money);
                        user.moneyincome=money;
                        onBackPressed();
                    }
                }
            }
        });
        String uri=getIntent().getStringExtra("URI");
        String post=getIntent().getStringExtra("DATA");
        Log.d("EASY",post);
        webView.postUrl(uri,post.getBytes("UTF-8"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
