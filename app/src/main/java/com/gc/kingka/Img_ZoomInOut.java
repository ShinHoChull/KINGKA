package com.gc.kingka;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.gc.dto.MainGetPhotoDTO;
import com.gc.modules.common.Globar;
import com.gc.modules.common.OnSwipeTouchListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;

public class Img_ZoomInOut extends AppCompatActivity implements View.OnClickListener {

    private ImageView img;
    private ArrayList<MainGetPhotoDTO> photoList;
    private int choiceNum;
    private String url;

    //next & back button & close Bt
    private ImageView nBt, bBt, cBt;

    //private count Text
    private TextView countTv , photoSaveBt;
    // circle progress Bar
    CircleProgressView mCircleView;

    private Globar g;
    private Context c;

    //backView
    //private LinearLayout backViewTop, backViewBottom;


    private void viewReset() {
        this.g = new Globar(this);
        this.c = this;
        this.photoList = new ArrayList<>();

        this.photoSaveBt = findViewById(R.id.photo_save);
        this.img = findViewById(R.id.Zoom_img);
        this.nBt = findViewById(R.id.photo_detail_next);
        this.bBt = findViewById(R.id.photo_detail_back);
        this.cBt = findViewById(R.id.photo_detail_close);
        this.countTv = findViewById(R.id.photo_detail_text);
        this.mCircleView = findViewById(R.id.circleView);
        //this.backViewTop = findViewById(R.id.zoom_backView_top);
        //this.backViewBottom = findViewById(R.id.zoom_backView_bottom);

        this.mCircleView.setSpinningBarLength(180);
        this.mCircleView.setShowTextWhileSpinning(true); // Show/hide text in spinning mode
        this.mCircleView.setText("");
        this.mCircleView.setTextMode(TextMode.TEXT);
        this.mCircleView.setUnitVisible(false);
    }

    private void listenerRegister() {
        this.nBt.setOnClickListener(this);
        this.bBt.setOnClickListener(this);
        this.cBt.setOnClickListener(this);
        this.photoSaveBt.setOnClickListener(this);
//        this.backViewTop.setOnTouchListener(new OnSwipeTouchListener(Img_ZoomInOut.this) {
//            public void onSwipeRight() {
//                nextPhoto();
//            }
//            public void onSwipeLeft() {
//                backPhoto();
//            }
//        });
//        this.backViewBottom.setOnTouchListener(new OnSwipeTouchListener(Img_ZoomInOut.this) {
//            public void onSwipeRight() {
//                nextPhoto();
//            }
//            public void onSwipeLeft() {
//                backPhoto();
//            }
//        });
    }


    private void getPhoto() {
        mCircleView.setVisibility(View.VISIBLE);
        this.mCircleView.spin();
        Picasso.get().load(this.g.mainUrl + this.url).resize(this.g.w(360), 0).placeholder(R.drawable.main_placeholder).error(R.mipmap.ic_launcher).into(img, new Callback() {
            @Override
            public void onSuccess() {
                mCircleView.stopSpinning();
                mCircleView.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        this.changeText();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_zoom_in_out);
        this.viewReset();
        this.listenerRegister();
        Intent intent = new Intent(this.getIntent());
        this.url = intent.getStringExtra("nowUrl");
        this.choiceNum = intent.getIntExtra("choiceNum", 0);
        this.photoList = (ArrayList<MainGetPhotoDTO>) intent.getSerializableExtra("array");
        this.getPhoto();


        this.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                photoSave();
                return false;
            }
        });

    }
    private void photoSave () {
        new MaterialDialog.Builder(c).title("이미지")
                .content("이미지를 저장하시겠습니까?")
                .positiveText("확인").negativeText("취소").theme(Theme.LIGHT).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                FileOutputStream out = null;
                try {
                    Date d = new Date();
                    out = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/"+String.valueOf(d.getTime())+".png");
                    BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
                    String.valueOf(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", "descripton"));
                    g.msg("저장되었습니다.");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).show();
    }

    private void nextPhoto() {
        this.choiceNum = this.choiceNum + 1;
        if (this.choiceNum >= this.photoList.size()) {
            this.choiceNum = 0;
        }
        this.url = this.photoList.get(this.choiceNum).getUrl();
        this.getPhoto();
    }

    private void backPhoto() {
        this.choiceNum = this.choiceNum - 1;
        if (this.choiceNum < 0) {
            this.choiceNum = this.photoList.size() - 1;
        }
        this.url = this.photoList.get(this.choiceNum).getUrl();
        this.getPhoto();
    }

    private void changeText() {
        this.countTv.setText((this.choiceNum + 1) + "/" + this.photoList.size());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_detail_next:
                this.nextPhoto();
                break;
            case R.id.photo_detail_back:
                this.backPhoto();
                break;
            case R.id.photo_detail_close:
                finish();
                break;
            case R.id.photo_save:
                this.photoSave();
                break;
        }
    }

}
