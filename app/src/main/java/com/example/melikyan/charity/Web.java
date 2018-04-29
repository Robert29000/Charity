package com.example.melikyan.charity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.melikyan.charity.MainApplication.InformationOfAnnoucment;
import com.yandex.money.api.util.UrlEncodedUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.WatchEvent;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Web extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
        setContentView(R.layout.activity_web_view);
        WebView webView=findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        String uri=getIntent().getStringExtra("URI");
        String post=getIntent().getStringExtra("DATA");
        webView.postUrl(uri,post.getBytes("UTF-8"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
