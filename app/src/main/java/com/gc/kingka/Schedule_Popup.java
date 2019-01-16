package com.gc.kingka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.dto.ScheduleDTO;
import com.gc.modules.Adapters.SchedulePopAdapter;
import com.gc.modules.Adapters.SelectListViewAdapter;
import com.gc.modules.common.Globar;

import java.util.ArrayList;

public class Schedule_Popup extends Activity implements AdapterView.OnItemClickListener , View.OnClickListener {

    //pop title
    private TextView titleDate;
    //pop close
    private ImageView closeBt;
    //pop listView
    private ListView listView;
    //pop adapter
    private SchedulePopAdapter spa;

    //ScheduleDTO Array
    private ArrayList<ScheduleDTO> arrayList;

    //date
    private String date;

    private Globar g;

    public void viewReset() {
        this.g = new Globar(this);

        this.titleDate = findViewById(R.id.schdulePop_text);
        this.closeBt = findViewById(R.id.schdulePop_closeImgView);
        this.listView = findViewById(R.id.schdulePop_listView);
    }

    public void listenerRegster() {

        this.closeBt.setOnClickListener(this);
        this.listView.setOnItemClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_popup);
        this.viewReset();
        this.listenerRegster();

        Intent intent = new Intent(this.getIntent());
        this.date = intent.getStringExtra("date");
        this.titleDate.setText(this.date);
        this.arrayList = (ArrayList<ScheduleDTO>) intent.getSerializableExtra("DTOS");

        Log.d("DTOS", String.valueOf(this.arrayList));

        ViewGroup.LayoutParams params = this.listView.getLayoutParams();
        int height = this.g.h(45 * this.arrayList.size());
        if ( this.arrayList.size() > 5 ) {
            height = 100;
        }

        params.height =  height;
        this.listView.setLayoutParams(params);
        this.listView.requestLayout();


        this.spa = new SchedulePopAdapter(this,getLayoutInflater(),this.arrayList);
        this.listView.setAdapter(this.spa);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.schdulePop_closeImgView:
                finish();
                overridePendingTransition(0,0);
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ScheduleDTO r = this.arrayList.get(position);
        Intent content = new Intent(this,ContentsActivity.class);
        content.putExtra("paramUrl",this.g.urls.get("schduleView")+"?sid="+r.getSid());
        content.putExtra("title", "학술행사");
        content.putExtra("schedule",true);
        content.putExtra("sDTO",r);
        startActivity(content);
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
    }
}
