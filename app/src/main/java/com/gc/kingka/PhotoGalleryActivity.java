package com.gc.kingka;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.dto.MainGetPhotoDTO;
import com.gc.dto.MessageDTO;
import com.gc.modules.Adapters.MainGetPhoto;
import com.gc.modules.common.CustomHandler;
import com.gc.modules.common.Globar;
import com.gc.modules.common.OnSwipeTouchListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PhotoGalleryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //photo gridView
    private GridView gridView;

    //photo url List
    private ArrayList<MainGetPhotoDTO> urlList;
    private MainGetPhoto mgp;

    //handler
    private CustomHandler customHandler;
    private Globar g;

    private TextView titleTv;

    //Photo Code
    private String code;

    //titlebar option Bt
    private ImageView optionBt , photo_topBt , backBt;

    private int myLastVisiblePos;

    private void viewReset() {
        this.urlList = new ArrayList<>();
        this.customHandler = new CustomHandler(this);
        this.g = new Globar(this);

        this.gridView = findViewById(R.id.mainPhoto_gridview);
        this.myLastVisiblePos = this.gridView.getFirstVisiblePosition();

        this.titleTv = findViewById(R.id.titlebar_title);
        this.backBt = findViewById(R.id.titlebar_back);

        this.optionBt = findViewById(R.id.titlebar_option);
        this.optionBt.setVisibility(View.GONE);

        this.photo_topBt = findViewById(R.id.photo_topBt);
        this.photo_topBt.setVisibility(View.GONE);

    }


    private void listenerRegister() {
        this.gridView.setOnItemClickListener(this);
        this.photo_topBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView.setSelection(0);
                photoUpdate();
                //gridView.smoothScrollToPosition(0);
            }
        });
        this.backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        this.viewReset();
        this.listenerRegister();
        this.titleTv.setText("포토갤러리");

        Intent intent = new Intent(this.getIntent());
        this.getPhotoUrl(intent.getStringExtra("code"));
    }


    public void photoUpdate () {

        this.mgp = new MainGetPhoto(this,getLayoutInflater(),this.urlList);
        this.gridView.setAdapter(this.mgp);
        this.gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int currentFirstVisPos = view.getFirstVisiblePosition();
                if(currentFirstVisPos > myLastVisiblePos) {
                    photo_topBt.setVisibility(View.GONE);
                    //scroll down
                }
                if(currentFirstVisPos < myLastVisiblePos) {
                    photo_topBt.setVisibility(View.VISIBLE);
                    //scroll up
                }
                myLastVisiblePos = currentFirstVisPos;
               Log.d("gridview_scroll",""+myLastVisiblePos);
            }
        });
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
                    JsonElement je = g.getParser(g.baseUrl+g.urls.get("getMainPhoto")+"?code="+photoCode);
                    Type listType = new TypeToken<ArrayList<MainGetPhotoDTO>>(){}.getType();
                    urlList = gson.fromJson(je,listType);
                    msg.what = CustomHandler.MAIN_GETPHOTO;
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
