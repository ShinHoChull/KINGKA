
package com.gc.modules.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.gc.dto.MenuDTO;
import com.gc.kingka.LoginActivity;
import com.gc.kingka.MenuActivity;
import com.gc.kingka.R;
import com.gc.kingka.ScheduleActivity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;

public class Globar {

    public String mainUrl = "http://kgca.m2comm.co.kr/";
    public String baseUrl = "http://kgca.m2comm.co.kr/mobile/php";

    private double default_W = 360;
    private double default_H = 640;

    public int deviceW = 0;
    public int deviceH = 0;
    public int statusBar = 0;

    private Context a;
    private WindowManager wm;

    private String[] exts = {"pptx", "docx", "doc", "hwp", "xlsx"};
    private String[] imgExts = {"jpeg", "gif", "jpeg", "jpg", "png"};
    public int[] info_color = {
            Color.rgb(230, 242, 255),
            Color.rgb(255, 231, 233),
            Color.rgb(222, 240, 226),
            Color.rgb(229, 242, 255),
            Color.rgb(254, 231, 233),
            Color.rgb(222, 240, 226),
            Color.rgb(229, 242, 255),
            Color.rgb(230, 242, 255),
            Color.rgb(255, 231, 233),
            Color.rgb(222, 240, 226),
            Color.rgb(229, 242, 255),
            Color.rgb(254, 231, 233),
            Color.rgb(222, 240, 226),
            Color.rgb(229, 242, 255)
    };

    public boolean extPDFSearch(String ext) {
        if (ext.contains("pdf")) {
            return true;
        }
        return false;
    }

    public boolean extSearch(String ext) {
        for (String s : exts) {
            if (ext.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean imgExtSearch(String ext) {
        for (String s : imgExts) {
            if (ext.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public Globar(Context c) {
        this.a = c;
        wm = (WindowManager) c.getSystemService(c.WINDOW_SERVICE);
        this.deviceW = this.getDeviceW();
        this.deviceH = this.getDeviceH();
        this.statusBar = this.getStatusBarHeight();
    }

    private DisplayMetrics dM() {
        DisplayMetrics dm = new DisplayMetrics();
        this.wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    private int getDeviceW() {
        return this.dM().widthPixels;
    }

    private int getDeviceH() {
        return this.dM().heightPixels - this.getStatusBarHeight();
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = this.a.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.a.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int x(int x) {
        return (int) (this.deviceW * (x / default_W));
    }

    public int y(int x) {
        return (int) (this.deviceH * (x / default_H));
    }

    public int w(int x) {
        return (int) (this.deviceW * (x / default_W));
    }

    public int h(int x) {
        return (int) (this.deviceH * (x / default_H));
    }

    public int dpToPixel(int dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, a.getResources().getDisplayMetrics());
        return px;
    }

    //View get X , Y
    public Point getPointView(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new Point(location[0], location[1]);
    }

    public double setTextSize(int fontize) {
        double size_formatter = (double) fontize / this.default_W;
        double deviceW;
        if (this.deviceW > 720) {
            deviceW = (double) this.deviceW / 3;
        } else {
            deviceW = (double) this.deviceW / 2;
        }
        return (int) deviceW * size_formatter;
    }

    public void msg(String msg) {
        Toast.makeText(this.a, msg, Toast.LENGTH_SHORT).show();
    }


    public JsonElement getParser(String url) throws Exception {
        HttpConnection hc = new HttpConnection();
        String getData = hc.request(url);
        JsonParser json = new JsonParser();
        return json.parse(getData);
    }


    public ArrayList<ArrayList<MenuDTO>> menuSetting() {

        ArrayList<ArrayList<MenuDTO>> ar = new ArrayList<ArrayList<MenuDTO>>();
        ArrayList<MenuDTO> tempArr = new ArrayList<MenuDTO>();
        //학회소개
        tempArr.add(new MenuDTO("0", "인사말"));
        tempArr.add(new MenuDTO("0", "미션 & 비젼"));
        tempArr.add(new MenuDTO("0", "조직도"));
        tempArr.add(new MenuDTO("0", "임원진 및 상임이사진"));
        tempArr.add(new MenuDTO("0", "사무국안내"));
        ar.add(tempArr);

        tempArr = new ArrayList<MenuDTO>();
        //학술행사
        tempArr.add(new MenuDTO("0", "KINGCA"));
        tempArr.add(new MenuDTO("0", "행사일정"));
        ar.add(tempArr);

        tempArr = new ArrayList<MenuDTO>();
        //회원공간
        tempArr.add(new MenuDTO("0", "공지사항"));
        tempArr.add(new MenuDTO("0", "모집공고"));
        tempArr.add(new MenuDTO("0", "회원소식"));
        tempArr.add(new MenuDTO("0", "회원검색"));
        tempArr.add(new MenuDTO("0", "포토갤러리"));
        ar.add(tempArr);

        tempArr = new ArrayList<MenuDTO>();
        //산하연구회
        tempArr.add(new MenuDTO("0", "대한복강경위장관연구회"));
        tempArr.add(new MenuDTO("0", "대한위식도역류질환수술연구회"));
        tempArr.add(new MenuDTO("0", "대한위장관외과연구회"));
        tempArr.add(new MenuDTO("0", "대한외과위내시경연구회"));
        tempArr.add(new MenuDTO("0", "위암 환자 삶의 질 연구회"));
        ar.add(tempArr);

        tempArr = new ArrayList<MenuDTO>();
        //학회지
        tempArr.add(new MenuDTO("0", "학회지 JGC"));
        tempArr.add(new MenuDTO("0", "구독신청안내"));
        ar.add(tempArr);

        tempArr = new ArrayList<MenuDTO>();
        //일반자료실
        tempArr.add(new MenuDTO("0", "자료실"));
        tempArr.add(new MenuDTO("0", "보험관련"));
        tempArr.add(new MenuDTO("0", "진료권고안"));
        ar.add(tempArr);

        tempArr = new ArrayList<MenuDTO>();
        //교육자료
        tempArr.add(new MenuDTO("0", "지난초록보기"));
        tempArr.add(new MenuDTO("0", "교육자료"));
        ar.add(tempArr);

        return ar;
    }

    private String[][] url = {
            {"/contents/index.php", "", "/bbs/list.php?code=notice_web",
                    "/research/index.php", "/contents/journal.php", "/bbs/list.php?code=pds_web"},//Main
            {"/contents/index.php", "/contents/mission.php",
                    "/contents/organ.php", "/contents/executive.php", "/contents/map.php"},
            {"", ""},
            {"/bbs/list.php?code=notice_web", "/bbs/list.php?code=recruit_web", "/bbs/list.php?code=meminfo_web",
                    "/search/member.php", "/bbs/list.php?code="},//회원공간
            {"", "", "", "", ""},
            {"https://www.jgc-online.org/", "/contents/journal.php"},
            {"/bbs/list.php?code=pds_web", "/bbs/list.php?code=insure_web", "/bbs/list.php?code=guide_web"},
            {"/abstract/index.php", "/abstract/edu.php"}
    };

    public String linkUrl(int link_call_position, int position) {
        String tempUrl = "";

        if (link_call_position == 0) {  //Main Link
            tempUrl = this.url[link_call_position][position];
        } else { //Menu Link 학회소개
            tempUrl = this.url[link_call_position][position];
        }
        return tempUrl;
    }

    public HashMap<String, String> urls = new HashMap<String, String>() {
        {
            put("banner", "/banner.php");//main Banner
            put("researchList", "/research/list.php");
            put("researchIntroduce", "/research/introduce.php"); //산하연구회 소개
            put("researchRaw", "/research/raw.php");//산하연구회 회칙
            put("academicList", "/kingca.php"); //List
            put("schaduleList", "/schedule.php");
            put("schduleView", "/schedule_view.php");
            put("MainPhotoTitleList", "/photo/index.php");//MainPhoto title 리스트
            put("getMainPhoto", "/photo/list.php");
            put("sponsor", "/sponsor.php");
            put("researchPhoto", "/research/photo.php");
            put("getPush", "/get_push.php");
            put("setPush", "/set_push.php");
            put("getNoti", "/noti_list.php");
            put("notiView", "/bbs/view.php");
            put("search", "/search.php");
            put("appInfo", "/app_info.php");
            put("setToken", "/token.php");
        }
    };

    public String zeroPoint(String data) {
        data = data.trim();
        if (data.length() == 1) {
            data = "0" + data;
        }
        return data;
    }

    //기본알럿창
    public void baseAlertMessage(String subject, String content) {
        new MaterialDialog.Builder(this.a).title(subject)
                .content(content)
                .positiveText("확인").negativeText("취소").theme(Theme.LIGHT).show();
    }

    //로그인 Alert
    //기본알럿창
    public void loginAlertMessage(String subject, String content, final Activity activity) {
        new MaterialDialog.Builder(this.a).title(subject)
                .content(content)
                .positiveText("확인").negativeText("취소").theme(Theme.LIGHT).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Intent content = new Intent(a, LoginActivity.class);
                a.startActivity(content);
                activity.overridePendingTransition(R.anim.anim_slide_in_bottom_login, 0);
                (activity).finish();
            }
        }).show();
    }


}
