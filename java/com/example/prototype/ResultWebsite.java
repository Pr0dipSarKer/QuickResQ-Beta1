package com.example.prototype;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.example.prototype.R;

public class ResultWebsite extends AppCompatActivity {

    TextView urlWeb;
    WebView webSite;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_website);

        urlWeb = (TextView) findViewById(R.id.webUrl);
        webSite = (WebView) findViewById(R.id.webSiteView);


        Bundle bundle = getIntent().getExtras();
        s = bundle.getString("webURL");


        if (s.equalsIgnoreCase("")){

            urlWeb.setText("No website Found");

        }else{
            urlWeb.setText(s);
            webSite.getSettings().setJavaScriptEnabled(true);
            webSite.setWebViewClient(new WebViewClient());

            webSite.loadUrl(s);

        }





    }
}
