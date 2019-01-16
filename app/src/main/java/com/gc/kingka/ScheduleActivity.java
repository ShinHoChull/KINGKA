package com.gc.kingka;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.dto.MessageDTO;
import com.gc.dto.ScheduleDTO;
import com.gc.modules.Adapters.CustomSchaduleListAdapter;
import com.gc.modules.Adapters.CustomSchduleGridViewAdapter;
import com.gc.modules.common.CustomHandler;
import com.gc.modules.common.Globar;
import com.gc.modules.common.OnSwipeTouchListener;
import com.gc.modules.common.SelectV;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.internal.Util;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView tv;
    //달력 우측 버튼
    private ImageView nextButton;
    //달력 좌측버튼
    private ImageView backButton;


    private Globar g;

    /*
     * 리스트 뷰
     * */
    private ListView listView;
    /*
     * 리스트 어댑터
     * */
    private CustomSchaduleListAdapter csl;

    /**
     * 연/월 텍스트뷰
     */
    private TextView tvDate, subTabText ,todayButton;

    /**
     * 그리드뷰 어댑터
     */
    private CustomSchduleGridViewAdapter gridAdapter;

    /**
     * 일 저장 할 리스트
     */
    private ArrayList<String> dayList;

    /**
     * 그리드뷰
     */
    private GridView gridView;
    /**
     * 캘린더 변수
     */
    private Calendar mCal;

    //행사일정 뷰 ,선택 창
    private LinearLayout calendar, bt1, bt2, subTab;

    //웹뷰
    private WebView wv;

    //현재 날짜 표시
    Date date, nDate;

    //handler
    private CustomHandler customHandler;
    //Schadule List
    private ArrayList<ScheduleDTO> schduleArray;

    //Schedule Map
    private HashMap<String, ArrayList<ScheduleDTO>> sMap;

    //titlebar option
    private ImageView titleBarOption , schedule_subTabImg;

    //Date
    private String realDate;



    private int defaultChoice = 0;
    private int schedule_listview_height = 0;

    private void viewReset() {
        this.g = new Globar(this);
        this.customHandler = new CustomHandler(this);
        this.dayList = new ArrayList<>();
        this.schduleArray = new ArrayList<>();
        this.sMap = new HashMap<>();

        this.gridView = findViewById(R.id.schadule_gridView);
        this.tv = findViewById(R.id.titlebar_title);
        this.tvDate = findViewById(R.id.schadule_mainDate);

        this.nextButton = findViewById(R.id.schadule_nextDateButton);
        this.backButton = findViewById(R.id.schadule_backDateButton);
        this.todayButton = findViewById(R.id.schadule_TodayButton);

        this.listView = findViewById(R.id.schadule_ListView);
        this.wv = findViewById(R.id.schadule_webView);
        this.calendar = findViewById(R.id.schadule_calendarV);
        this.bt1 = findViewById(R.id.schadule_one);
        this.bt2 = findViewById(R.id.schadule_two);

        this.subTabText = findViewById(R.id.schedule_subTabText);
        this.schedule_subTabImg = findViewById(R.id.schedule_subTabImg);
        this.subTab = findViewById(R.id.schedule_subTab);
        //Schedule에서는 option 버튼이 필요없어서 지움.
        this.titleBarOption = findViewById(R.id.titlebar_option);
        this.titleBarOption.setVisibility(View.GONE);
    }

    private void listenerRegster() {
        this.bt1.setOnClickListener(this);
        this.bt2.setOnClickListener(this);
        this.todayButton.setOnClickListener(this);
        this.nextButton.setOnClickListener(this);
        this.backButton.setOnClickListener(this);
        this.subTab.setOnClickListener(this);
        this.gridView.setOnItemClickListener(this);
        this.listView.setOnItemClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        this.viewReset();
        this.listenerRegster();
        this.tv.setText("학술행사");

        this.todayDate();
        this.defaultChoice = getIntent().getIntExtra("defaultNum",1);
        this.choiceBt();
    }

    private void resetChoiceBt() {
        this.clickColor(this.bt1, "#CFCFCF", "#ffffff");
        this.clickColor(this.bt2, "#CFCFCF", "#ffffff");

        this.wv.setVisibility(View.GONE);
        this.calendar.setVisibility(View.GONE);
    }

    private void choiceBt() {
        this.resetChoiceBt();

        if (this.defaultChoice == 0) { //KiNGCA
            this.wv.setVisibility(View.VISIBLE);
            this.clickColor(this.bt1, "#29469D", "#29469D");
            this.wv.loadUrl(this.g.baseUrl + this.g.urls.get("academicList"));

        } else if (this.defaultChoice == 1) { //행사일정
            this.calendar.setVisibility(View.VISIBLE);
            this.clickColor(this.bt2, "#29469D", "#29469D");
        }
    }

    private void clickColor(LinearLayout parent, String textColor, String vColor) {
        for (int i = 0, j = parent.getChildCount(); i < j; i++) {
            if ( i == 0 ) {
                TextView v = (TextView) parent.getChildAt(i);
                v.setTextColor(Color.parseColor(textColor));
            } else {
                LinearLayout v = (LinearLayout) parent.getChildAt(i);
                v.setBackgroundColor(Color.parseColor(vColor));
            }
        }
    }

    /**
     * 월 데이터 가져오기.
     */
    private void getMonthEvent(final String date) {

        if (this.sMap.containsKey(date) == true) {
            this.schduleArray = this.sMap.get(date);
            this.chnageAdapter();
            return;
        }

        new Thread() {
            @Override
            public void run() {
                super.run();
                Gson gson = new Gson();
                Message msg = customHandler.obtainMessage();
                try {
                    JsonElement je = g.getParser(g.baseUrl + g.urls.get("schaduleList") + "?datetime=" + date);
                    Type listType = new TypeToken<ArrayList<ScheduleDTO>>() {
                    }.getType();
                    schduleArray = gson.fromJson(je, listType);
                    sMap.put(date, (ArrayList<ScheduleDTO>) gson.fromJson(je, listType));
                    msg.what = CustomHandler.SCHEDULE_CHANGE_CODE;
                    customHandler.sendMessage(msg);

                } catch (Exception e) {
                    msg.obj = new MessageDTO("Failed to fetch data.(schedule Error)",
                            e.toString());
                    msg.what = CustomHandler.ALERT_WINDOW_CODE;
                    customHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void chnageAdapter() {
        if (this.schduleArray == null) this.schduleArray = new ArrayList<ScheduleDTO>();
        if (this.gridAdapter != null) this.gridAdapter.notifyDataSetChanged();
        this.gridAdapter = new CustomSchduleGridViewAdapter(this, getLayoutInflater(), this.mCal, this.nDate, this.dayList, this.schduleArray);
        this.gridView.setAdapter(this.gridAdapter);

        if (this.csl != null) this.csl.notifyDataSetChanged();
        this.csl = new CustomSchaduleListAdapter(this, getLayoutInflater(), this.schduleArray);
        this.listView.setAdapter(csl);
    }


    /**
     * Date Next 1 & Back -1
     *
     * @param temp
     */
    public void changeDate(int temp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        this.nDate = null;
        try {
            this.nDate = dateFormat.parse((String) this.realDate);

            Calendar cal = Calendar.getInstance();
            cal.setTime(this.nDate);
            cal.add(this.mCal.MONTH, temp);

            this.mCal = cal;

            //연,월,일을 따로 저장
            final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
            final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
            final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
            this.realDate = curYearFormat.format(cal.getTime()) + "." + curMonthFormat.format(cal.getTime()) + "." + curDayFormat.format(cal.getTime());
            this.tvDate.setText(curYearFormat.format(cal.getTime()) + "." + curMonthFormat.format(cal.getTime()));

            this.mCal.set(Integer.parseInt(curYearFormat.format(cal.getTime())), Integer.parseInt(curMonthFormat.format(cal.getTime())) - 1, 1);
            int dayNum = this.mCal.get(this.mCal.DAY_OF_WEEK);
            //1일 - 요일 매칭 시키기 위해 공백 add
            this.dayList.clear();
            for (int i = 1; i < dayNum; i++) {
                this.dayList.add("");
            }
            setCalendarDate(this.mCal.get(mCal.MONTH) + 1);

            this.getMonthEvent(curYearFormat.format(cal.getTime()) + "-" + curMonthFormat.format(cal.getTime()));


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void todayDate() {
        // 오늘에 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        this.date = new Date(now);

        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        //현재 날짜 텍스트뷰에 뿌려줌
        this.tvDate.setText(curYearFormat.format(this.date) + "." + curMonthFormat.format(this.date));
        this.realDate = curYearFormat.format(this.date) + "." + curMonthFormat.format(this.date) + "." + curDayFormat.format(this.date);
        this.changeDate(0);
    }

    /**
     * 해당 월에 표시할 일 수 구함
     *
     * @param month
     */
    private void setCalendarDate(int month) {
        this.mCal.set(this.mCal.MONTH, month - 1);
        for (int i = 0; i < this.mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            this.dayList.add("" + (i + 1));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.schadule_one:
                this.defaultChoice = 0;
                break;

            case R.id.schadule_two:
                this.defaultChoice = 1;
                break;

            case R.id.schadule_nextDateButton:
                this.changeDate(1);
                break;

            case R.id.schadule_backDateButton:
                this.changeDate(-1);
                break;

            case R.id.schadule_TodayButton:
                this.todayDate();
                break;

            case R.id.schedule_subTab:
                if ( this.subTabText.getText().equals("달력접기") ) {
                    this.subTabText.setText("달력펴기");
                    this.gridView.setVisibility(View.GONE);
                    this.schedule_subTabImg.setImageResource(R.drawable.btn_d_fold1);
                    this.schedule_listview_height = this.listView.getHeight();
                    ViewGroup.LayoutParams params = this.listView.getLayoutParams();
                    params.height = this.g.h(391);
                    this.listView.setLayoutParams(params);
                    this.listView.requestLayout();

                } else {

                    this.subTabText.setText("달력접기");
                    this.gridView.setVisibility(View.VISIBLE);
                    this.schedule_subTabImg.setImageResource(R.drawable.btn_d_unfold1);
                    ViewGroup.LayoutParams params = this.listView.getLayoutParams();
                    params.height = this.schedule_listview_height;
                    this.listView.setLayoutParams(params);
                    this.listView.requestLayout();
                }
                break;
        }
        this.choiceBt();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //this.schduleArray
        String touchDay = "";
        if ( parent.getId() == R.id.schadule_gridView )
            touchDay = this.g.zeroPoint((String) this.gridView.getItemAtPosition(position));
        else if ( parent.getId() == R.id.schadule_ListView ) {
            Intent content = new Intent(this, ContentsActivity.class);
            content.putExtra("paramUrl", this.g.urls.get("schduleView") + "?sid=" + this.schduleArray.get(position).getSid());
            content.putExtra("title", "학술행사");
            content.putExtra("schedule",true);
            content.putExtra("sDTO",this.schduleArray.get(position));
            startActivity(content);
            overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
            return;
        }

        if (touchDay.equals("")) return;

        ArrayList<ScheduleDTO> tempArr = new ArrayList<>();
        for (ScheduleDTO sd : schduleArray) {

            String[] date = sd.getSdate().split("-");
            String day = date[date.length - 1];

            int pointDay = Integer.parseInt(touchDay);
            String[] sDayArr = sd.getSdate().split("-");
            int sDay = Integer.parseInt(sDayArr[sDayArr.length - 1 ]);

            if ( sd.getEdate().equals("") == false ) {
                String[] eDayArr = sd.getEdate().split("-");
                int eDay = Integer.parseInt(eDayArr[eDayArr.length - 1 ]);
                if ( sDay <= pointDay && eDay >= pointDay ) {
                    tempArr.add(sd);
                }
            } else {
                if ( pointDay == sDay ) {
                    tempArr.add(sd);
                }
            }
        }

        if (tempArr.size() == 1) {
            Intent content = new Intent(this, ContentsActivity.class);
            content.putExtra("paramUrl", this.g.urls.get("schduleView") + "?sid=" + tempArr.get(0).getSid());
            content.putExtra("title", "학술행사");
            content.putExtra("schedule",true);
            content.putExtra("sDTO",tempArr.get(0));
            startActivity(content);
            overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);

        } else if (tempArr.size() >= 2) {
            //Popup 띄우기
            Intent intent = new Intent(this, Schedule_Popup.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("DTOS", tempArr);
            intent.putExtra("date", this.tvDate.getText()+"."+touchDay);
            startActivity(intent);
        }

    }
}















