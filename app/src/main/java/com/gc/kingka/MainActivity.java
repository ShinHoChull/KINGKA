package com.gc.kingka;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.gc.dto.MainBannerDTO;
import com.gc.dto.MenuDTO;
import com.gc.dto.MessageDTO;
import com.gc.dto.NoticeDTO;
import com.gc.dto.SponsorDTO;
import com.gc.modules.Adapters.CustomGridViewAdapter;
import com.gc.modules.Adapters.CustomPagerAdapter;
import com.gc.modules.Adapters.SponsorPagerAdapter;
import com.gc.modules.common.CustomHandler;
import com.gc.modules.common.Custom_SharedPreferences;
import com.gc.modules.common.Globar;
import com.gc.modules.common.HttpConnection;
import com.gc.modules.common.JsonReader;
import com.gc.modules.common.SelectV;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener ,View.OnClickListener {

    //main Banner
    private TimerTask mTask;
    private Timer mtimer;
    private int timer = 5000;

    //Sponsor Banner
    private TimerTask smTask;
    private Timer smtimer;
    private int stimer = 5000;

    private ViewPager viewPager;
    private GridView gridLayout;
    private Globar g;

    //Banner List
    private ArrayList<MainBannerDTO> bannerArray;
    //Bottom Sponsor List
    private ArrayList<SponsorDTO> sponsorArray;
    //Notice List
    private ArrayList<NoticeDTO> noticeArray;

    //handler
    private CustomHandler customHandler;
    //share
    private Custom_SharedPreferences csp;

    //bottom Sponsor Banner
    private ViewPager sponsorBanner;
    //bottomSponsor viewPagerView
    private LinearLayout sponsorView;
    //bottomSponsor viewPagerCloseImage
    private ImageView closeButton , searchBt;
    //notice Text
    private TextView notice_text;

    private Boolean isOnResume = false;


    private void viewReset() {
        this.customHandler = new CustomHandler(this);
        this.csp = new Custom_SharedPreferences(this);
        this.g = new Globar(this);

        this.searchBt = findViewById(R.id.status_bar_search);
        this.searchBt.setVisibility(View.VISIBLE);

        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        this.gridLayout = (GridView) findViewById(R.id.main_grid);
        this.sponsorBanner = findViewById(R.id.main_bottom_viewpager);
        this.sponsorView = findViewById(R.id.main_Sponsors_bottomV);
        this.closeButton= findViewById(R.id.main_Sponsors_closeBt);
        this.notice_text = findViewById(R.id.main_notice_text);
        this.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation slideDown = AnimationUtils.loadAnimation(getBaseContext(),R.anim.anim_slide_out_bottom);
                sponsorView.setVisibility(View.GONE);
                sponsorView.startAnimation(slideDown);
            }
        });

        CustomGridViewAdapter cga = new CustomGridViewAdapter(this, getLayoutInflater());
        this.gridLayout.setAdapter(cga);
        this.sponsorBanner.setPageMargin(this.g.w(20));
        this.sponsorBanner.setPadding(this.g.w(10),this.g.w(15),this.g.w(0),this.g.w(15));
    }

    private void listenerRegister() {
        this.gridLayout.setOnItemClickListener(this);
        this.notice_text.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.viewReset();
        this.listenerRegister();

        //Menu 산하연구학회 때문에..
        new Thread() {
            @Override
            public void run() {
                super.run();
                Gson gson = new Gson();
                Message msg = customHandler.obtainMessage();
                try {
                    JsonElement je = g.getParser(g.baseUrl+g.urls.get("researchList"));
                    csp.put("research",gson.toJson(je));

                } catch (Exception e) {
                    msg.obj = new MessageDTO("Failed to fetch data.(resarchList Error)",
                            e.toString());
                    msg.what = CustomHandler.ALERT_WINDOW_CODE;
                    customHandler.sendMessage(msg);
                }
            }
        }.start();

        //메인 배너 이미지 다운로드 받기
        new Thread() {
            @Override
            public void run() {
                super.run();
                Gson gson = new Gson();
                Message msg = customHandler.obtainMessage();
                try {
                    JsonElement je = g.getParser(g.baseUrl+g.urls.get("banner"));
                    Type listType = new TypeToken<ArrayList<MainBannerDTO>>(){}.getType();
                    ArrayList<MainBannerDTO> bannerArr =  gson.fromJson(je,listType);
                    bannerArray = bannerArr;

                    msg.what = CustomHandler.MAIN_TIMER_CODE;
                    customHandler.sendMessage(msg);
                } catch (Exception e) {
                    msg.obj = new MessageDTO("Failed to fetch data.(Banner Error)",
                            e.toString());
                    msg.what = CustomHandler.ALERT_WINDOW_CODE;
                    customHandler.sendMessage(msg);
                }
            }
        }.start();

        //메인 스폰서 배너 이미지 다운로드 받기
        new Thread() {
            @Override
            public void run() {
                super.run();
                Gson gson = new Gson();
                Message msg = customHandler.obtainMessage();
                try {
                    JsonElement je = g.getParser(g.baseUrl+g.urls.get("sponsor"));
                    Type listType = new TypeToken<ArrayList<SponsorDTO>>(){}.getType();
                    sponsorArray =  gson.fromJson(je,listType);
                    msg.what = CustomHandler.MAIN_SPONSOR_CODE;
                    customHandler.sendMessage(msg);

                } catch (Exception e) {

                    msg.obj = new MessageDTO("Failed to fetch data.(Banner Error)",
                            e.toString());
                    msg.what = CustomHandler.ALERT_WINDOW_CODE;
                    customHandler.sendMessage(msg);
                }
            }
        }.start();


        this.permissionDemand();
    }

    public void permissionDemand () {
        int permission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR);
        int permission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission1 == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( this.isOnResume == true ) {
            this.isOnResume = false;
            if (this.mtimer == null) this.timerStart();
            if (this.smtimer == null) this.sponsorTimerStart();
        }

        //메인 공지사항
        new Thread() {
            @Override
            public void run() {
                super.run();
                Gson gson = new Gson();
                Message msg = customHandler.obtainMessage();
                try {
                    JsonElement je = g.getParser(g.baseUrl+g.urls.get("getNoti"));
                    Type listType = new TypeToken<ArrayList<NoticeDTO>>(){}.getType();
                    noticeArray =  gson.fromJson("["+je+"]",listType);
                    msg.what = CustomHandler.MAIN_NOTICE_CODE;
                    customHandler.sendMessage(msg);
                } catch (Exception e) {
                    msg.obj = new MessageDTO("Failed to fetch data.(Banner Error)",
                            e.toString());
                    msg.what = CustomHandler.ALERT_WINDOW_CODE;
                    customHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.isOnResume = true;
        if ( this.mtimer != null )  {
            this.mtimer.cancel();
            this.mtimer = null;
        }
        if ( this.smtimer != null ) {
            this.smtimer.cancel();
            this.smtimer = null;
        }
    }

    public void noticeUpdate() {

        String notiV = this.noticeArray.get(0).getSubject();
        if (notiV.length() >= 20) {
            notiV = notiV.substring(0,20);
            notiV += "...";
        }
        this.notice_text.setText(notiV);
    }

    public void timerStart () {
        TabLayout tabLayout = findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(this.viewPager, true);

        CustomPagerAdapter adapter = new CustomPagerAdapter(getApplicationContext(),getLayoutInflater(),this.bannerArray);
        this.viewPager.setAdapter(adapter);

        if ( this.bannerArray.size() > 1 ) {
            mTask = new TimerTask() {
                @Override
                public void run() {
                    Message msg = customHandler.obtainMessage();
                    msg.what = CustomHandler.MAIN_SLIDE_CODE;
                    customHandler.sendMessage(msg);
                }
            };
            mtimer = new Timer();
            mtimer.schedule(mTask, this.timer, this.timer);
        }
    }

    public void sponsorTimerStart () {

        if ( this.sponsorArray.size() <= 0 ) {
            sponsorView.setVisibility(View.GONE);
            return;
        }

        SponsorPagerAdapter spa = new SponsorPagerAdapter(getApplicationContext(),getLayoutInflater(),this.sponsorArray,this);
        this.sponsorBanner.setAdapter(spa);

        if ( this.sponsorArray.size() >= 3 ) {
            smTask = new TimerTask() {
                @Override
                public void run() {
                    Message msg = customHandler.obtainMessage();
                    msg.what = CustomHandler.MAIN_SPONSOR_SLIDE_CODE;
                    customHandler.sendMessage(msg);
                }
            };
            smtimer = new Timer();
            smtimer.schedule(smTask, this.stimer, this.stimer);
        }
    }

    public void sponsorSlideView () {
        int postion = this.sponsorBanner.getCurrentItem();
        this.sponsorBanner.setCurrentItem(postion + 1, true);
    }

    public void slideView() {
        int postion = this.viewPager.getCurrentItem();
        if (postion >= this.bannerArray.size()-1) {
            this.viewPager.setCurrentItem(0, false);
        } else {
            this.viewPager.setCurrentItem(postion + 1, true);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String tempTitle = "";

        switch (position) {
            case 0:
                tempTitle = "학회소개";
                break;
            case 1:
                //학술행사 앱으로..
                Intent content = new Intent(this,ScheduleActivity.class);
                startActivity(content);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
                return;

            case 2:
                tempTitle = "회원공간";
                break;
            case 3:
                //산하연구회
                Intent research = new Intent(this,ResearchActivity.class);
                startActivity(research);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
                return;
            case 4:
                tempTitle = "학회지 JCG";
                Intent content1 = new Intent(Intent.ACTION_VIEW, Uri.parse(this.g.linkUrl(5, 0)));
                startActivity(content1);
                return;
            case 5:
                tempTitle = "일반자료실";
               break;
        }
        Intent content = new Intent(this,ContentsActivity.class);
        content.putExtra("paramUrl",this.g.linkUrl(0,position));
        content.putExtra("title", tempTitle);
        content.putExtra("num", String.valueOf(position));
        startActivity(content);
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.main_notice_text:
                Intent content = new Intent(this,ContentsActivity.class);
                content.putExtra("paramUrl",this.g.urls.get("notiView")+"?code=notice_web&sid="+this.noticeArray.get(0).getSid());
                content.putExtra("title", "공지사항");
                content.putExtra("num", String.valueOf(2));
                startActivity(content);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
                break;
        }

    }
}
