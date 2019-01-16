package com.gc.kingka;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.gc.dto.ScheduleDTO;
import com.gc.modules.common.ChromeclientPower;
import com.gc.modules.common.Custom_SharedPreferences;
import com.gc.modules.common.Download;
import com.gc.modules.common.Globar;
import com.gc.modules.common.SelectV;
import com.gc.modules.customViews.CustomWebview;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ContentsActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView wv;
    private String paramUrl = "";
    private Globar g;

    public String title = "";//Content 제목
    private TextView tv, scheduleBt;

    //titleBar Option
    private ImageView titlebarOption, titlebarBack, searchBt;
    private String choiceNum;

    private ScheduleDTO sdto;
    private Custom_SharedPreferences csp;
    private ChromeclientPower chromeclient;

    private boolean isCal = false;

    private long cid = 0;
    private Context c = this;
    private boolean isPdf = false;
    private boolean isLoaded = false;

    //Content
    public void viewReset() {
        this.csp = new Custom_SharedPreferences(this);

        this.wv = findViewById(R.id.content_Webview);
        this.tv = findViewById(R.id.titlebar_title);
        this.scheduleBt = findViewById(R.id.schedule_calendar_bt);
        this.searchBt = findViewById(R.id.status_bar_search);

        this.titlebarOption = findViewById(R.id.titlebar_option);
        this.titlebarBack = findViewById(R.id.titlebar_back);

        this.g = new Globar(this);
        this.chromeclient = new ChromeclientPower(this,getApplicationContext(),this.wv);

        this.wv.setWebViewClient(new WebviewCustomClient());
        //this.wv.setWebChromeClient(this.chromeclient);
        this.wv.getSettings().setUseWideViewPort(true);
        this.wv.getSettings().setJavaScriptEnabled(true);
        this.wv.getSettings().setLoadWithOverviewMode(true);
        this.wv.getSettings().setDefaultTextEncodingName("utf-8");
        this.wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.wv.getSettings().setSupportMultipleWindows(false);
        this.wv.getSettings().setDomStorageEnabled(true);
        this.wv.getSettings().setBuiltInZoomControls(true);
        this.wv.getSettings().setDisplayZoomControls(false);
//        this.wv.setFocusable(false);
//        this.wv.setClickable(false);
        this.wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        wv.setLongClickable(true);
        wv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                WebView.HitTestResult hitTestResult = wv.getHitTestResult();

                switch (hitTestResult.getType()) {

                    case WebView.HitTestResult.IMAGE_TYPE:
                        final String typeUrl = hitTestResult.getExtra();
                        String[] temps = typeUrl.split("/");
                        final String fileNames = temps[temps.length - 1];
                        new MaterialDialog.Builder(c).title("이미지")
                                .content("다운받으시겠습니까?")
                                .positiveText("확인").negativeText("취소").theme(Theme.LIGHT).onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                new Download(typeUrl,c,fileNames);
                            }
                        }).show();
                        break;
                }
                return false;
            }
        });
    }

    private void listenerRegster() {
        this.titlebarOption.setOnClickListener(this);
        this.titlebarBack.setOnClickListener(this);
        this.scheduleBt.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
        this.viewReset();
        this.listenerRegster();

        Intent intent = new Intent(this.getIntent());
        this.paramUrl = intent.getStringExtra("paramUrl");
        boolean isSchedule = intent.getBooleanExtra("schedule", false);
        if (isSchedule) {
            this.searchBt.setVisibility(View.GONE);
            this.scheduleBt.setVisibility(View.VISIBLE);
            this.sdto = (ScheduleDTO) intent.getSerializableExtra("sDTO");

            String val = csp.getValue("calendarId", "");
            if (val.equals("") == false) {
                String[] temps = val.split(",");
                for (String s : temps) {
                    String[] t = s.split("/");
                    if (t[0].equals(this.sdto.getSid())) {
                        this.cid = Long.parseLong(t[1]);
                        this.isCal = true;
                        this.btChange();
                    }
                }
            }
        }
        this.wv.loadUrl(this.urlSetting(this.paramUrl));
    }

    private void btChange() {
        if (this.isCal == true) {
            this.scheduleBt.setText("캘린더취소");
            this.scheduleBt.setTextColor(Color.WHITE);
            this.scheduleBt.setBackgroundResource(R.drawable.calenderline);
        } else {
            this.scheduleBt.setText("캘린더등록");
            this.scheduleBt.setTextColor(Color.WHITE);
            this.scheduleBt.setBackgroundColor(Color.rgb(228, 145, 0));
        }
    }

    //Intent 초기화
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.wv.onResume();
        if (this.isPdf == false) {
            Intent intent = new Intent(this.getIntent());
            this.tv.setText(intent.getStringExtra("title"));
            this.choiceNum = intent.getStringExtra("num");
            if (this.choiceNum == null || this.choiceNum.equals("")) {
                this.titlebarOption.setVisibility(View.GONE);
            }

            this.paramUrl = intent.getStringExtra("paramUrl");

            this.wv.loadUrl(this.urlSetting(this.paramUrl));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.wv.onPause();
    }

    public String urlSetting(String paramUrl) {
        String deviceid = csp.getValue("deviceid","");

        String url = this.g.baseUrl + paramUrl;
        if (paramUrl.startsWith("http") || paramUrl.startsWith("https")) {
            url = paramUrl;
        }
        if ( paramUrl.contains("?") )url += "&";
        else url += "?";
        url += "deviceid="+deviceid+"&device=android";

        return url;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_option:
                Intent intent = new Intent(this, SelectV.class);
                intent.putExtra("num", Integer.valueOf(this.choiceNum));
                intent.putExtra("title", this.tv.getText());
                startActivity(intent);
                break;
            case R.id.titlebar_back:
                if (this.wv.canGoBack()) {
                    this.wv.goBack();
                } else {
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                }
                break;

            case R.id.schedule_calendar_bt:
                if (this.isCal == false) {
                    long id = this.insertEvent();
                    Log.d("Schedule_eventID",""+id);
                    this.cid = id;
                    if (id > 0) {
                        String nowVal = csp.getValue("calendarId", "");
                        String val = nowVal + this.sdto.getSid() + "/" + id + ",";
                        Log.d("insert_val",val);
                        this.g.msg("캘린더에 등록되었습니다.");
                        csp.put("calendarId", val);
                        this.isCal = true;
                        this.btChange();
                    }
                } else {
                    this.isCal = false;
                    ContentResolver cr = getContentResolver();
                    ContentValues values = new ContentValues();
                    Uri deleteUri = null;
                    deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, this.cid);
                    int rows = getContentResolver().delete(deleteUri, null, null);
                    this.g.msg("캘린더에 삭제되었습니다.");
                    this.btChange();

                    String val = csp.getValue("calendarId", "");
                    String temp = "";
                    if (val.equals("") == false) {
                        String[] temps = val.split(",");
                        for (String s : temps) {
                            String[] t = s.split("/");
                            if (t[0].equals(this.sdto.getSid()) == false) {
                                temp += s+",";
                            }
                        }
                    }
                    csp.put("calendarId", temp);
                }
                break;
        }
    }

    private long insertEvent() {

        // 디폴트 타임존 구하기
        //String timezone = TimeZone.getDefault().getID();// Asia/Korea
        // 이벤트 추가

        long calID = 1;
        long startMillis = 0;
        long endMillis = 0;

        Log.d("startDate", this.sdto.getSdate());
        String[] s = this.sdto.getSdate().split("-");
        String[] e = (this.sdto.getEdate() != null && !this.sdto.getEdate().equals("")) ? this.sdto.getEdate().split("-") : this.sdto.getSdate().split("-");

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(Integer.parseInt(s[0]), Integer.parseInt(s[1]) - 1, Integer.parseInt(s[2]), 9, 0);
        startMillis = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.set(Integer.parseInt(e[0]), Integer.parseInt(e[1]) - 1, Integer.parseInt(e[2]), 18, 0);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, this.sdto.getSubject());
        values.put(CalendarContract.Events.DESCRIPTION, this.sdto.getSubject());
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Korea");
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
        return eventID;
    }

    @Override
    public void onBackPressed() {
        if (this.wv.canGoBack()) {
            this.wv.goBack();
        } else {
            finish();
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void api21Code() {

        //21레벨 api에서 꼭필요한 코드.
        // https -> http 로 전송할때 cancle되지 않도록..
        this.wv.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(this.wv, true);
    }

    public class WebviewCustomClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            String[] urlCut = url.split("/");

            Log.d("NowUrl",url);
            if (url.startsWith(g.mainUrl) == false) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            } else if ( g.extPDFSearch(urlCut[urlCut.length - 1]) ) {
                isPdf = true;
                Intent content = new Intent(getApplicationContext(),Download_PDFViewerActivity.class);
                content.putExtra("url", url);
                content.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(content);
                overridePendingTransition(R.anim.anim_slide_in_bottom_login,0);
                // view.loadUrl(doc);
                return true;
            } else if ( g.extSearch(urlCut[urlCut.length - 1]) ) { //기타 문서 Search
                new Download(url,getApplicationContext(),urlCut[urlCut.length - 1]);
                return true;
            } else if ( g.imgExtSearch(urlCut[urlCut.length - 1]) ) { //이미지 Search
                Intent content = new Intent(c,PopWebviewActivity.class);
                content.putExtra("paramUrl", url);
                content.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(content);
                overridePendingTransition(R.anim.anim_slide_in_bottom_login,0);
                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("onPageStarted",url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.d("onLoadResource",url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("onPageFinished",url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Toast.makeText(getApplicationContext(), "서버와 연결이 끊어졌습니다", Toast.LENGTH_SHORT ).show();
            view.loadUrl("about:blank");
        }


    }


}
