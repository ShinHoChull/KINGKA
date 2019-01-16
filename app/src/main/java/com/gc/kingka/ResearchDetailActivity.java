package com.gc.kingka;

import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.dto.MainGetPhotoDTO;
import com.gc.dto.MenuDTO;
import com.gc.dto.MessageDTO;
import com.gc.modules.Adapters.MainGetPhoto;
import com.gc.modules.common.CustomHandler;
import com.gc.modules.common.Custom_SharedPreferences;
import com.gc.modules.common.Globar;
import com.gc.modules.common.SelectV;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ResearchDetailActivity extends AppCompatActivity implements View.OnClickListener ,AdapterView.OnItemClickListener {

    private TextView tv;
    private WebView wv;
    private String paramUrl = "";
    private String title = "";
    private Globar g;

    //selectButton
    private LinearLayout bt1,bt2,bt3;
    private int defaultChoice = 0;
    private String sid = "";

    //handler
    private CustomHandler customHandler;

    //photo
    private GridView gridView;

    //back Button
    private ImageView backBt;

    //share
    private Custom_SharedPreferences csp;


    //titleBar Option
    ArrayList<MenuDTO> list;
    private ImageView titlebarOption;

    //photo url List
    private ArrayList<MainGetPhotoDTO> urlList;
    private MainGetPhoto mgp;


    private void viewReset() {
        this.g = new Globar(this);
        this.csp = new Custom_SharedPreferences(this);
        this.urlList = new ArrayList<>();
        this.customHandler = new CustomHandler(this);

        this.tv = findViewById(R.id.titlebar_title);
        this.wv = findViewById(R.id.researchDetail_webview);
        this.bt1 = findViewById(R.id.researchDetail_one);
        this.bt2 = findViewById(R.id.researchDetail_two);
        this.bt3 = findViewById(R.id.researchDetail_three);
        this.gridView = findViewById(R.id.researchDetail_gridview);
        this.backBt = findViewById(R.id.titlebar_back);
        this.titlebarOption = findViewById(R.id.titlebar_option);
    }

    private void listenerRegster() {
        this.bt1.setOnClickListener(this);
        this.bt2.setOnClickListener(this);
        this.bt3.setOnClickListener(this);
        this.backBt.setOnClickListener(this);
        this.titlebarOption.setOnClickListener(this);
        this.gridView.setOnItemClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research_detail);
        this.viewReset();
        this.listenerRegster();

        Intent intent = new Intent(this.getIntent());
        this.sid = intent.getStringExtra("sid");
        this.title = intent.getStringExtra("title");
        this.tv.setText(this.title);

        //selectV메뉴에서 선택을 하게 된다면..
        this.defaultChoice = intent.getIntExtra("defaultNum",0);
        this.choiceBt();

        //resarch 데이터.
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<MenuDTO>>(){}.getType();
        this.list = gson.fromJson(csp.getValue("research",""),listType);

        this.wv.getSettings().setUseWideViewPort(true);
        this.wv.getSettings().setJavaScriptEnabled(true);
        this.wv.getSettings().setLoadWithOverviewMode(true);
        this.wv.getSettings().setDefaultTextEncodingName("utf-8");
        this.wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.wv.getSettings().setSupportMultipleWindows(false);
        this.wv.getSettings().setDomStorageEnabled(true);
        this.wv.getSettings().setBuiltInZoomControls(true);
        this.wv.getSettings().setDisplayZoomControls(false);
    }


    public void photoUpdate () {
        if ( this.urlList == null ) {
            this.urlList = new ArrayList<>();
            //Photo Data가 없을때 ..

            this.wv.loadUrl(this.g.baseUrl+this.g.urls.get("researchIntroduce")+"?sid=??");
            this.gridView.setVisibility(View.GONE);
            this.wv.setVisibility(View.VISIBLE);
        }
        this.mgp = new MainGetPhoto(this,getLayoutInflater(),this.urlList);
        this.gridView.setAdapter(this.mgp);
    }

    //photo Url 가져오기
    private void getPhotoUrl( final String photoCode) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Gson gson = new Gson();
                Message msg = customHandler.obtainMessage();
                try {
                    JsonElement je = g.getParser(g.baseUrl+g.urls.get("researchPhoto")+"?sid="+photoCode);
                    Type listType = new TypeToken<ArrayList<MainGetPhotoDTO>>(){}.getType();
                    urlList = gson.fromJson(je,listType);
                    msg.what = CustomHandler.RESEARCH_PHOTO_GET_CODE;
                    customHandler.sendMessage(msg);

                } catch (Exception e) {
                    msg.obj = new MessageDTO("Failed to fetch data.(MainGetPhoto Error)",
                            e.toString());
                    msg.what = CustomHandler.ALERT_WINDOW_CODE;
                    customHandler.sendMessage(msg);
                }

            }
        }.start();
    }



    private void resetChoiceBt() {
        this.clickColor(this.bt1,"#CFCFCF","#ffffff");
        this.clickColor(this.bt2,"#CFCFCF","#ffffff");
        this.clickColor(this.bt3,"#CFCFCF","#ffffff");
    }

    private void choiceBt() {
        this.resetChoiceBt();

        if ( this.defaultChoice == 0 ) {//소개
            this.gridView.setVisibility(View.GONE);
            this.wv.setVisibility(View.VISIBLE);
            this.clickColor(this.bt1,"#29469D","#29469D");
            this.wv.loadUrl(this.g.baseUrl+this.g.urls.get("researchIntroduce")+"?sid="+this.sid);
        } else if ( this.defaultChoice == 1 ) {//포토
            this.gridView.setVisibility(View.VISIBLE);
            this.wv.setVisibility(View.GONE);
            this.getPhotoUrl(this.sid);
            this.clickColor(this.bt2,"#29469D","#29469D");

        } else if ( this.defaultChoice == 2 ) {//회칙
            this.gridView.setVisibility(View.GONE);
            this.wv.setVisibility(View.VISIBLE);
            this.clickColor(this.bt3,"#29469D","#29469D");
            this.wv.loadUrl(this.g.baseUrl+this.g.urls.get("researchRaw")+"?sid=0"+this.sid);
        }
    }

    private void clickColor(LinearLayout parent , String textColor , String vColor) {
        for(int i = 0 , j = parent.getChildCount(); i < j ; i++) {
            if ( i == 0 ) {
                TextView v = (TextView) parent.getChildAt(i);
                v.setTextColor(Color.parseColor(textColor));
            } else {
                LinearLayout v = (LinearLayout) parent.getChildAt(i);
                v.setBackgroundColor(Color.parseColor(vColor));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.researchDetail_one :
                this.defaultChoice = 0;
                break;
            case R.id.researchDetail_two :
                this.defaultChoice = 1;
                break;
            case R.id.researchDetail_three :
                this.defaultChoice = 2;
                break;
            case R.id.titlebar_back :
                this.finish();
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
                break;
            case R.id.titlebar_option :

                Intent intent = new Intent(this,SelectV.class);
                intent.putExtra("num", Integer.valueOf(3));
                intent.putExtra("title", this.tv.getText());
                startActivity(intent);

                break;
        }
        this.choiceBt();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MainGetPhotoDTO r = this.urlList.get(position);
        Intent zoomActivity = new Intent(this,Img_ZoomInOut.class);
        zoomActivity.putExtra("nowUrl",r.getUrl());
        zoomActivity.putExtra("choiceNum",position);
        zoomActivity.putExtra("array",this.urlList);
        startActivity(zoomActivity);
    }
}
