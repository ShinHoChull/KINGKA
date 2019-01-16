package com.gc.kingka;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gc.dto.MainBannerDTO;
import com.gc.dto.MessageDTO;
import com.gc.dto.SettingDTO;
import com.gc.modules.common.CustomHandler;
import com.gc.modules.common.Custom_SharedPreferences;
import com.gc.modules.common.Globar;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv;

    private ArrayList<ImageView> imgArray;

    private ImageView bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt10;

    //Logout V & Bt
    private LinearLayout logoutBt;
    private TableRow loginV;

    private ArrayList<SettingDTO> pushArray;

    //handler
    private CustomHandler customHandler;
    private Globar g;

    //shared
    private Custom_SharedPreferences csp;

    //deviceId
    private String deviceID;

    //pushValue
    private ArrayList<String> pushValueArray;

    private ImageView titleOption;





    public void viewReset() {
        this.customHandler = new CustomHandler(this);
        this.g = new Globar(this);
        this.csp = new Custom_SharedPreferences(this);

        this.tv = (TextView) findViewById(R.id.titlebar_title);
        this.loginV = findViewById(R.id.setting_logOutV);

        this.imgArray = new ArrayList<>();
        this.imgArray.add((ImageView)findViewById(R.id.setting_op1));
        this.imgArray.add((ImageView)findViewById(R.id.setting_op2));
        this.imgArray.add((ImageView)findViewById(R.id.setting_op3));
        this.imgArray.add((ImageView)findViewById(R.id.setting_op4));
        this.imgArray.add((ImageView)findViewById(R.id.setting_op5));
        this.imgArray.add((ImageView)findViewById(R.id.setting_op6));
        this.imgArray.add((ImageView)findViewById(R.id.setting_op7));
        this.imgArray.add((ImageView)findViewById(R.id.setting_op8));
        this.imgArray.add((ImageView)findViewById(R.id.setting_op9));
        this.imgArray.add((ImageView)findViewById(R.id.setting_op10));


        this.logoutBt = findViewById(R.id.setting_logout);
        this.titleOption = findViewById(R.id.titlebar_option);
        this.titleOption.setVisibility(View.GONE);
    }

    private void listenerRegister() {
        this.logoutBt.setOnClickListener(this);
        for ( ImageView iv : imgArray ) {
            iv.setOnClickListener(this);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        this.viewReset();
        this.listenerRegister();

        this.tv.setText("설정");
        this.deviceID = this.csp.getValue("deviceid","");

        if (this.csp.getValue("isLogin" , false) == false) {
            this.loginV.setVisibility(View.GONE);
            this.logoutBt.setVisibility(View.GONE);
        }

        this.getPush();
    }

    public void getPush () {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Gson gson = new Gson();
                Message msg = customHandler.obtainMessage();
                try {
                    JsonElement je = g.getParser(g.baseUrl+g.urls.get("getPush")+"?deviceid="+deviceID);
                    String[] s1 = je.toString().split(",");
                    ArrayList<String> tempArray = new ArrayList<>();

                    for (String r : s1) {
                        String[] s2 = r.split(":");
                        tempArray.add(s2[s2.length - 1].replace("\"","") );
                    }
                    pushValueArray = tempArray;
                    msg.what = CustomHandler.SETTING_GET_CODE;
                    customHandler.sendMessage(msg);

                } catch (Exception e) {

                    msg.obj = new MessageDTO("Failed to fetch data.(Setting Error)",
                            e.toString());
                    msg.what = CustomHandler.ALERT_WINDOW_CODE;
                    customHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void updateOption() {

        for ( int i = 0, j = this.imgArray.size(); i < j; i++ ) {

            ImageView iv = imgArray.get(i);
            String s = pushValueArray.get(i).replace("}","");

            if ( s.equals("Y") ) {
                iv.setImageResource(R.drawable.togleon);
            } else {
                iv.setImageResource(R.drawable.togleoff);
            }
        }
    }

    public void changeVal(final String index) {
        final String s = pushValueArray.get(Integer.valueOf(index)).replace("}","");

        new Thread() {
            @Override
            public void run() {
                super.run();
                Gson gson = new Gson();
                Message msg = customHandler.obtainMessage();
                try {
                    JsonElement je = g.getParser(g.baseUrl+g.urls.get("setPush")+"?deviceid="+deviceID
                            +"&val="+s
                            +"&key=push"+(Integer.valueOf(index)+1));
                    String val = je.toString();
                    Log.d("settingVal",val);

                } catch (Exception e) {

                    msg.obj = new MessageDTO("Failed to fetch data.(Setting Error)",
                            e.toString());
                    msg.what = CustomHandler.ALERT_WINDOW_CODE;
                    customHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {

        String position = "";

        switch (v.getId()) {
            case R.id.setting_op1:
                position = "0";
                break;
            case R.id.setting_op2:
                position = "1";
                break;
            case R.id.setting_op3:
                position = "2";
                break;
            case R.id.setting_op4:
                position = "3";
                break;
            case R.id.setting_op5:
                position = "4";
                break;
            case R.id.setting_op6:
                position = "5";
                break;
            case R.id.setting_op7:
                position = "6";
                break;
            case R.id.setting_op8:
                position = "7";
                break;
            case R.id.setting_op9:
                position = "8";
                break;
            case R.id.setting_op10:
                position = "9";
                break;
            case R.id.setting_logout:
                this.csp.put("isLogin",false);
                this.g.msg("로그아웃 되었습니다.");
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
                break;
        }

        if ( position.equals("") == false ) {
            ImageView iv = imgArray.get(Integer.valueOf(position));
            String s = pushValueArray.get(Integer.valueOf(position)).replace("}","");
            if ( s.equals("Y") ) {
                pushValueArray.set(Integer.valueOf(position),"N");
                iv.setImageResource(R.drawable.togleoff);
            } else {
                pushValueArray.set(Integer.valueOf(position),"Y");
                iv.setImageResource(R.drawable.togleon);
            }
            Log.d("pushValue",s);
            this.changeVal(position);
        }


    }
}
