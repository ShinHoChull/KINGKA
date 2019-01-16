package com.gc.modules.common;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.gc.dto.MenuDTO;
import com.gc.kingka.ContentsActivity;
import com.gc.kingka.PhotoChoice;
import com.gc.kingka.R;
import com.gc.kingka.ResearchDetailActivity;
import com.gc.kingka.ScheduleActivity;
import com.gc.modules.Adapters.CustomListViewAdapter;
import com.gc.modules.Adapters.SelectListViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SelectV extends Activity implements View.OnClickListener ,AdapterView.OnItemClickListener {


    //Select ListView
    private ListView selectlist;

    //backGroundView
    private LinearLayout select_back;

    //menuDTO Array
    private ArrayList<MenuDTO> arrayList;

    //adapter
    private SelectListViewAdapter slv;

    private Globar g;
    private int getMenuPoint = 0;

    private String title;

    private Custom_SharedPreferences csp;
    ArrayList<MenuDTO> list;

    //login 처리
    private Boolean isLogin;

    public void viewReset() {
        this.arrayList = new ArrayList<>();
        this.csp = new Custom_SharedPreferences(this);

        this.selectlist = findViewById(R.id.select_listV);
        this.select_back = findViewById(R.id.select_back);
        this.select_back.setOnClickListener(this);
        this.g = new Globar(this);
    }

    private void listenerRegster() {
        this.selectlist.setOnItemClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectview);
        this.viewReset();
        this.listenerRegster();

        Intent intent = new Intent(this.getIntent());
        this.getMenuPoint = intent.getIntExtra("num",0);
        this.title = intent.getStringExtra("title");

        this.list = this.g.menuSetting().get(this.getMenuPoint);
        this.isLogin = this.csp.getValue("isLogin",false);
        if ( this.getMenuPoint == 3 ) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<MenuDTO>>(){}.getType();
            list = gson.fromJson(csp.getValue("research",""),listType);
        }

        ViewGroup.LayoutParams params = this.selectlist.getLayoutParams();
        int height = this.g.h(36 * this.list.size());
        params.height =  height;
        this.selectlist.setLayoutParams(params);
        this.selectlist.requestLayout();


        this.slv = new SelectListViewAdapter(this,getLayoutInflater(),list);
        this.selectlist.setAdapter(this.slv);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ContentsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //받은 데이터를 다시 넣어준다..
        //지금은 자식 뷰와 원할한 통신을 할줄 몰라서 일단 임시적으로 ..처리하자..
        if ( this.getMenuPoint == 3 ) {

            Intent content = new Intent(this,ResearchDetailActivity.class);
            content.putExtra("sid",this.list.get(position).getSid());
            content.putExtra("title", this.list.get(position).getName());
            content.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(content);
            overridePendingTransition(0,0);
            return;
        } else if ( this.getMenuPoint == 2 ) {

            if (position == 4) {
                //photo Gallery 처리
                Intent photo = new Intent(this, PhotoChoice.class);
                startActivity(photo);
                overridePendingTransition(0,0);
                return;
            } else {
                if ( this.isLogin == false) {
                    this.g.loginAlertMessage("로그인","로그인이 필요합니다.로그인하시겠습니까?",this);
                    return;
                }
            }
        } else if ( this.getMenuPoint == 5 || ( this.getMenuPoint == 6 && position == 1 ) ) {
            if ( this.isLogin == false) {
                this.g.loginAlertMessage("로그인","로그인이 필요합니다.로그인하시겠습니까?",this);
                return;
            }
        } else if ( this.getMenuPoint == 4 && position == 0 ) {
            Intent content = new Intent(Intent.ACTION_VIEW, Uri.parse(this.g.linkUrl(this.getMenuPoint + 1, position)));
            startActivity(content);
            return;
        }

        intent.putExtra("paramUrl", this.g.linkUrl(this.getMenuPoint+1,position));
        intent.putExtra("title", this.list.get(position).getName());
        intent.putExtra("num", String.valueOf(this.getMenuPoint));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
        overridePendingTransition(0,0);

        //((ContentsActivity)getApplicationContext()).moveUrl(this.g.linkUrl(this.getMenuPoint+1,position));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_back :
                this.finish();
                break;
        }


    }
}
