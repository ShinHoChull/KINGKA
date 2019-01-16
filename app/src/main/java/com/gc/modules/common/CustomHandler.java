package com.gc.modules.common;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.gc.dto.MessageDTO;
import com.gc.kingka.InfoActivity;
import com.gc.kingka.IntroActivity;
import com.gc.kingka.MainActivity;
import com.gc.kingka.MenuActivity;
import com.gc.kingka.PhotoChoice;
import com.gc.kingka.PhotoGalleryActivity;
import com.gc.kingka.ResearchDetailActivity;
import com.gc.kingka.ScheduleActivity;
import com.gc.kingka.SettingActivity;

public class CustomHandler extends Handler {

    public static final int ALERT_WINDOW_CODE = 1;
    public static final int MAIN_SLIDE_CODE = 2;
    public static final int MAIN_TIMER_CODE = 3;
    public static final int MAIN_NOTICE_CODE = 12;
    public static final int MENU_TITLE_CHANGE_CODE = 4;
    public static final int SCHEDULE_CHANGE_CODE = 5;
    public static final int MAIN_PHOTO_LIST = 6;
    public static final int MAIN_GETPHOTO = 7;
    public static final int MAIN_SPONSOR_CODE = 8;
    public static final int MAIN_SPONSOR_SLIDE_CODE = 9;
    public static final int RESEARCH_PHOTO_GET_CODE = 10;
    public static final int SETTING_GET_CODE = 11;
    public static final int APPINFO_CODE = 13;
    public static final int TOKEN_CODE = 14;



    private Context c;

    public CustomHandler(Context c) {
        this.c = c;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        switch (msg.what) {
            case ALERT_WINDOW_CODE :
                MessageDTO mDTO = (MessageDTO)msg.obj;
                this.baseAlertMessage(mDTO.getSubject(),mDTO.getContent());
                break;

            case MAIN_SLIDE_CODE :
                ((MainActivity)this.c).slideView();
                break;

            case MAIN_SPONSOR_CODE :
                ((MainActivity)this.c).sponsorTimerStart();
                break;

            case MAIN_SPONSOR_SLIDE_CODE:
                ((MainActivity)this.c).sponsorSlideView();
                break;

            case MAIN_NOTICE_CODE:
                ((MainActivity)this.c).noticeUpdate();
                break;

            case MAIN_TIMER_CODE :
                ((MainActivity)this.c).timerStart();
                break;

            case MENU_TITLE_CHANGE_CODE:
                ((MenuActivity)this.c).listviewSet(3);
                break;

            case SCHEDULE_CHANGE_CODE:
                ((ScheduleActivity)this.c).chnageAdapter();
                break;

            case MAIN_PHOTO_LIST:
                ((PhotoChoice)this.c).gridViewUpdate();
                break;

            case MAIN_GETPHOTO:
                ((PhotoGalleryActivity)this.c).photoUpdate();
                break;

            case RESEARCH_PHOTO_GET_CODE:
                ((ResearchDetailActivity)this.c).photoUpdate();
                break;

            case SETTING_GET_CODE:
                ((SettingActivity)this.c).updateOption();
                break;

            case APPINFO_CODE:
                ((InfoActivity)this.c).updateInfoImage();
                break;
            case TOKEN_CODE:
                ((IntroActivity)this.c).moveMain();
                break;


        }
    }

    //기본알럿창
    private void baseAlertMessage (String subject , String content) {
        new MaterialDialog.Builder(this.c).title(subject)
                .content(content)
                .positiveText("OK").negativeText("cancel").theme(Theme.LIGHT).show();

    }

}
