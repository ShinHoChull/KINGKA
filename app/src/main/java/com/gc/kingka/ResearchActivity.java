package com.gc.kingka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.dto.MenuDTO;
import com.gc.modules.Adapters.CustomMenu_GradViewAdapter;
import com.gc.modules.Adapters.ResearchGridAdapter;
import com.gc.modules.common.Custom_SharedPreferences;
import com.gc.modules.common.SelectV;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ResearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener , View.OnClickListener{

    private TextView tv;
    //gridview
    private GridView gd;

    //share
    private Custom_SharedPreferences csp;

    //adapter
    private ResearchGridAdapter rga;
    ArrayList<MenuDTO> list;

    //titleBar Option
    private ImageView titlebarOption;

    private void viewReset() {
        this.csp = new Custom_SharedPreferences(this);

        this.tv = (TextView) findViewById(R.id.titlebar_title);
        this.gd = (GridView) findViewById(R.id.research_grid);
        this.titlebarOption = findViewById(R.id.titlebar_option);
    }

    private void listenerRegster() {
        this.titlebarOption.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);
        this.viewReset();
        this.listenerRegster();
        this.tv.setText("산하연구회");

        //resarch 데이터.
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<MenuDTO>>(){}.getType();
        this.list = (ArrayList<MenuDTO>) gson.fromJson(csp.getValue("research",""),listType);

        this.rga = new ResearchGridAdapter(this, getLayoutInflater(),this.list);
        this.gd.setAdapter(rga);
        this.gd.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.titlebar_option :

                Intent intent = new Intent(this,SelectV.class);
                intent.putExtra("num", Integer.valueOf(3));
                intent.putExtra("title", this.tv.getText());
                startActivity(intent);

                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent content = new Intent(this,ResearchDetailActivity.class);
        content.putExtra("sid",this.list.get(position).getSid());
        content.putExtra("title",this.list.get(position).getName());
        startActivity(content);
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
    }
}
