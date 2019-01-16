package com.gc.kingka;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.dto.MainGetPhotoDTO;
import com.gc.dto.MainPhotoDTO;
import com.gc.dto.MenuDTO;
import com.gc.dto.MessageDTO;
import com.gc.modules.Adapters.PhotoChoiceAdapter;
import com.gc.modules.Adapters.ResearchGridAdapter;
import com.gc.modules.common.CustomHandler;
import com.gc.modules.common.Custom_SharedPreferences;
import com.gc.modules.common.Globar;
import com.gc.modules.common.SelectV;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PhotoChoice extends AppCompatActivity implements AdapterView.OnItemClickListener , View.OnClickListener {

    private TextView titleV;

    //gridview
    private GridView gridView;
    private PhotoChoiceAdapter pca;

    //photoList
    private ArrayList<MainPhotoDTO> arrayList;

    //handler
    private CustomHandler customHandler;
    private Globar g;

    //optionButton
    private ImageView optionBt;

    //shared
    private Custom_SharedPreferences csp;
    //adapter
    private ResearchGridAdapter rga;
    ArrayList<MenuDTO> list;


    public void viewReset() {
        this.arrayList = new ArrayList<>();
        this.customHandler = new CustomHandler(this);
        this.csp = new Custom_SharedPreferences(this);
        this.g = new Globar(this);

        this.gridView = findViewById(R.id.photoChoice_grid);
        this.titleV = findViewById(R.id.titlebar_title);
        this.optionBt = findViewById(R.id.titlebar_option);
    }

    private void listenerRegister() {
        this.gridView.setOnItemClickListener(this);
        this.optionBt.setOnClickListener(this);
    }


    private void getPhotoViewIntent(String code) {
        Intent content = new Intent(this,PhotoGalleryActivity.class);
        content.putExtra("code",code);
        startActivity(content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_choice);
        this.viewReset();
        this.listenerRegister();
        this.titleV.setText("포토갤러리");
        this.getPhotoListData();

        //resarch 데이터.
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<MenuDTO>>(){}.getType();
        this.list = gson.fromJson(csp.getValue("research",""),listType);
    }

    public void gridViewUpdate() {
        this.pca = new PhotoChoiceAdapter(this,getLayoutInflater(),this.arrayList);
        this.gridView.setAdapter(this.pca);
    }

    //photo List 가져오기
    private void getPhotoListData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Gson gson = new Gson();
                Message msg = customHandler.obtainMessage();
                try {
                    JsonElement je = g.getParser(g.baseUrl+g.urls.get("MainPhotoTitleList"));
                    Type listType = new TypeToken<ArrayList<MainPhotoDTO>>(){}.getType();
                    arrayList = gson.fromJson(je,listType);
                    msg.what = CustomHandler.MAIN_PHOTO_LIST;
                    customHandler.sendMessage(msg);

                } catch (Exception e) {
                    msg.obj = new MessageDTO("Failed to fetch data.(MainPhoto Error)",
                            e.toString());
                    msg.what = CustomHandler.ALERT_WINDOW_CODE;
                    customHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainPhotoDTO r = this.arrayList.get(position);
        this.getPhotoViewIntent(r.getCode());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_option:
                Intent intent = new Intent(this,SelectV.class);
                intent.putExtra("num", Integer.valueOf(2));
                startActivity(intent);
                break;
        }
    }
}
