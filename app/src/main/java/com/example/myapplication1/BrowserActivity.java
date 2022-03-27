package com.example.myapplication1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class BrowserActivity extends AppCompatActivity {

    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        web = (WebView) this.findViewById(R.id.web1);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl("https://m.daum.net");

        Button btn1 = (Button) findViewById(R.id.button1);
        Button btn2 = (Button) findViewById(R.id.button2);
        Button btn3 = (Button) findViewById(R.id.button3);

        btn1.setOnClickListener(new View.OnClickListener()         {
            public void onClick(View v) {
                web.goBack(); }
        });

        btn2.setOnClickListener(new View.OnClickListener()         {
            public void onClick(View v) {
                web.reload(); }
        });

        btn3.setOnClickListener(new View.OnClickListener()         {
            public void onClick(View v) {
                web.goForward(); }
        });

    }
}