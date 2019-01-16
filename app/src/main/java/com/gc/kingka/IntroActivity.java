package com.gc.kingka;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.os.Bundle;
import android.widget.Toast;

import com.gc.dto.MessageDTO;
import com.gc.modules.common.CustomHandler;
import com.gc.modules.common.Custom_SharedPreferences;
import com.gc.modules.common.Globar;
import com.gc.modules.common.NetworkState;

public class IntroActivity extends Activity {

    private Custom_SharedPreferences csp;
    private Handler handler;
    private int time = 2000;
    private Globar g;
    //handler
    private CustomHandler customHandler;


    public void init() {
        this.csp = new Custom_SharedPreferences(this);
        this.handler = new Handler();
        this.g = new Globar(this);
        this.customHandler = new CustomHandler(this);

        //초기화.
        this.csp.put("menuKey",0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        this.init();

        if(NetworkState.isNetworkStat(getApplicationContext()) == false) {
            Toast.makeText(getApplicationContext(),"인터넷을 실행시켜주세요",Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        if ( csp.getValue("deviceid","").equals("") ) {

            final String deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            csp.put("deviceid",deviceid);

            //메인 공지사항
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Message msg = customHandler.obtainMessage();
                    try {
                        g.getParser(g.baseUrl+g.urls.get("setToken")+"?deviceid="+deviceid+"&device=android");
                        msg.what = CustomHandler.TOKEN_CODE;
                        customHandler.sendMessage(msg);
                    } catch (Exception e) {
                        msg.obj = new MessageDTO("Failed to fetch data.(Intro Error)",
                                e.toString());
                        msg.what = CustomHandler.ALERT_WINDOW_CODE;
                        customHandler.sendMessage(msg);
                    }
                }
            }.start();
        } else {
            this.moveMain();
        }
    }

    public void moveMain() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, time);
    }


}
