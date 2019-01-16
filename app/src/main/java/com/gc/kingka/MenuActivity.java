package com.gc.kingka;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.dto.MessageDTO;
import com.gc.dto.MenuDTO;
import com.gc.modules.Adapters.CustomListViewAdapter;
import com.gc.modules.Adapters.CustomMenu_GradViewAdapter;
import com.gc.modules.common.CustomHandler;
import com.gc.modules.common.Custom_SharedPreferences;
import com.gc.modules.common.Globar;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MenuActivity extends Activity implements AdapterView.OnItemClickListener , View.OnClickListener {

    private GridView gridLayout;
    private CustomMenu_GradViewAdapter cga;
    private ListView listView;
    private CustomListViewAdapter cla;
    private Globar g;
    /*menu Title & SubTitle */
    private TextView title ,subTitle, home_text;

    //select tab Position
    private int tabPosition = 0;

    //MenuTitle
    ArrayList<MenuDTO> ami;

    //handler
    CustomHandler customHandler;

    //share
    Custom_SharedPreferences csp;
    //closeBt
    private ImageView closeBt , homeBt;
    //loginName , notiText , infoText
    private TextView userName , notiText , infoText , settingText;

    //Login Check
    private boolean isLogin;

    private void viewReset() {
        this.g = new Globar(this);
        this.customHandler = new CustomHandler(this);
        this.ami = new ArrayList<MenuDTO>();
        this.csp = new Custom_SharedPreferences(this);

        this.gridLayout = (GridView) findViewById(R.id.menu_grid);
        this.cga = new CustomMenu_GradViewAdapter(this, getLayoutInflater());
        this.cga.selectPostion = csp.getValue("menuKey",0);
        this.gridLayout.setAdapter(cga);

        this.listView = (ListView) findViewById(R.id.menuListView);
        this.cla = new CustomListViewAdapter(this, getLayoutInflater(), this.g.menuSetting().get(0));
        this.listView.setAdapter(this.cla);

        this.title = findViewById(R.id.menu_title);
        this.subTitle = findViewById(R.id.menu_subtitle);

        this.closeBt = findViewById(R.id.menu_closeBt);
        this.home_text = findViewById(R.id.menu_home_text);
        this.homeBt = findViewById(R.id.menu_home_img);
        this.userName = findViewById(R.id.menu_userName);

        this.notiText = findViewById(R.id.menu_notiText);
        this.infoText = findViewById(R.id.menu_infoText);
        this.settingText = findViewById(R.id.menu_setting_text);
        this.tabPosition = csp.getValue("menuKey",0);
        this.titleChange(this.tabPosition);
        this.listviewSet(this.tabPosition);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu);
        this.viewReset();
        this.listenerRegster();
        this.isLogin = this.csp.getValue("isLogin",false);

        String name = this.csp.getValue("name","");
        if (this.isLogin == false) {
            this.userName.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void listenerRegster() {
        this.gridLayout.setOnItemClickListener(this);
        this.listView.setOnItemClickListener(this);
        this.closeBt.setOnClickListener(this);
        this.home_text.setOnClickListener(this);
        this.homeBt.setOnClickListener(this);
        this.infoText.setOnClickListener(this);
        this.notiText.setOnClickListener(this);
        this.settingText.setOnClickListener(this);
    }

    public void listviewSet(int i) {
        // position 3은 신하연구회 때문에 분기처리해놈..
        if (i == 3) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<MenuDTO>>() {
            }.getType();
            ArrayList<MenuDTO> list = gson.fromJson(csp.getValue("research", ""), listType);
            this.ami = list;
        } else {
            this.ami = this.g.menuSetting().get(i);
        }

        this.cla.notifyDataSetChanged();
        this.cla = new CustomListViewAdapter(this, getLayoutInflater(), this.ami);
        this.listView.setAdapter(this.cla);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_closeBt:
                finish();
                overridePendingTransition(0,R.anim.anim_slide_out_left);
                break;
            case R.id.menu_home_img:
            case R.id.menu_home_text:
                Intent mainCall = new Intent(this,MainActivity.class);
                mainCall.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(mainCall);
                overridePendingTransition(0,R.anim.anim_slide_out_left);
                break;

            case R.id.menu_notiText:
                Intent content = new Intent(this, ContentsActivity.class);
                content.putExtra("paramUrl",this.g.linkUrl(3,0));
                content.putExtra("title", "공지사항");
                content.putExtra("num", String.valueOf(2));
                startActivity(content);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
                break;

            case R.id.menu_infoText:
                Intent info = new Intent(this,InfoActivity.class);
                startActivity(info);
                overridePendingTransition(R.anim.anim_slide_in_bottom_login,0);
                finish();
                break;
            case R.id.menu_setting_text:
                Intent setting = new Intent(this,SettingActivity.class);
                startActivity(setting);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (parent.getId() == R.id.menu_grid) {
            this.cga.notifyDataSetChanged();
            this.cga.selectPostion = position;
            this.gridLayout.setAdapter(this.cga);
            this.titleChange(position);
            this.csp.put("menuKey",position);

            //TableView 작업..
            this.tabPosition = position;

            this.listviewSet(position);

        } else if (parent.getId() == R.id.menuListView) {

            if (this.tabPosition == 3) {
                Intent content = new Intent(this, ResearchDetailActivity.class);
                content.putExtra("sid", this.ami.get(position).getSid());
                content.putExtra("title", this.ami.get(position).getName());
                startActivity(content);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
                return;
            } else if ( this.tabPosition == 1 ) {
                //학술행사 앱으로..
                Intent content = new Intent(this,ScheduleActivity.class);

                if (position == 0) content.putExtra("defaultNum",0);

                startActivity(content);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
                return;

            } else if ( this.tabPosition == 2 ) {

                if ( position == 4 ) {
                    Intent photo = new Intent(this, PhotoChoice.class);
                    startActivity(photo);
                    overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
                    return;
                } else {
                    if ( this.isLogin == false) {
                        this.g.loginAlertMessage("로그인","로그인이 필요합니다.로그인하시겠습니까?",this);
                        return;
                    }
                }
            } else if ( this.tabPosition == 5 || ( this.tabPosition == 6 && position == 1 ) ) {
                if ( this.isLogin == false) {
                    this.g.loginAlertMessage("로그인","로그인이 필요합니다.로그인하시겠습니까?",this);
                    return;
                }
            } else if ( this.tabPosition == 4 && position == 0 ) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.g.linkUrl(this.tabPosition + 1, position)));
                startActivity(intent);
                return;
            }

            this.ami = this.g.menuSetting().get(this.tabPosition);
            Intent content = new Intent(this, ContentsActivity.class);
            content.putExtra("paramUrl", this.g.linkUrl(this.tabPosition + 1, position));
            content.putExtra("title", this.ami.get(position).getName());
            content.putExtra("num", String.valueOf(this.tabPosition));
            startActivity(content);
            overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
        }
    }

    private void titleChange(int index) {
        switch (index) {
            case 0:
                this.title.setText("학회소개");
                this.subTitle.setText("대한위암학회를 소개합니다.");
                break;
            case 1:
                this.title.setText("학술행사");
                this.subTitle.setText("KINGCA 및 다양한 학술행사 정보를 확인하세요.");
                break;
            case 2:
                this.title.setText("회원공간");
                this.subTitle.setText("대한위암학회의 회원을 위한 공간입니다.");
                break;
            case 3:
                this.title.setText("산하연구회");
                this.subTitle.setText("대한위암학회 산하에 설치되는 연구회 입니다.");
                break;
            case 4:
                this.title.setText("학회지 JGC");
                this.subTitle.setText("대한위암학회 학회지 JGC입니다.");
                break;
            case 5:
                this.title.setText("일반자료실");
                this.subTitle.setText("보험자료,진료권고안등 진료 및 연구에 유용한 컨텐츠를 제공해 드립니다.");
                break;
            case 6:
                this.title.setText("교육자료");
                this.subTitle.setText("지난초록보기, 교육자료 등 다양한 자료를 제공해 드립니다.");
                break;
        }
    }
}

