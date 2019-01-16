package com.gc.kingka;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.gc.modules.common.Globar;

public class PopWebviewActivity extends Activity implements View.OnClickListener {

    private WebView wv;
    private String paramUrl = "";
    private Globar g;

    private ImageView closeBt;

    //Content
    public void viewReset() {
        this.wv =  findViewById(R.id.webpop_webview);
        this.closeBt = findViewById(R.id.webpop_close);
        this.g = new Globar(this);
    }

    private void listenerRegster() {
        this.closeBt.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_webview);
        this.viewReset();
        this.listenerRegster();

        Intent intent = new Intent(this.getIntent());
        this.paramUrl = intent.getStringExtra("paramUrl");
        Log.d("PoPUrl_param",this.paramUrl);

        this.wv.getSettings().setUseWideViewPort(true);
        this.wv.getSettings().setJavaScriptEnabled(true);
        this.wv.getSettings().setLoadWithOverviewMode(true);
        this.wv.getSettings().setDefaultTextEncodingName("utf-8");
        this.wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.wv.getSettings().setSupportMultipleWindows(false);
        this.wv.getSettings().setDomStorageEnabled(true);
        this.wv.getSettings().setBuiltInZoomControls(true);
        this.wv.getSettings().setDisplayZoomControls(false);

        this.wv.loadUrl(this.paramUrl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.webpop_close :
                finish();
                break;
        }

    }
}
