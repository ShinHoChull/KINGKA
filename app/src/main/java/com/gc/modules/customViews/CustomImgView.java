package com.gc.modules.customViews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.gc.kingka.ContentsActivity;
import com.gc.modules.common.Globar;
import com.gc.kingka.MainActivity;
import com.gc.kingka.MenuActivity;
import com.gc.kingka.R;
import com.gc.kingka.SettingActivity;

public class CustomImgView extends android.support.v7.widget.AppCompatImageView implements View.OnClickListener {

    private int x,y,w,h;
    private Context c;
    private Globar g;

    public CustomImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context,attrs);
    }

    private void initView(Context c , AttributeSet a) {
        this.g = new Globar(c);
        this.c = c;
        TypedArray t = c.obtainStyledAttributes(a, R.styleable.CustomImgView);

        int color = t.getColor(R.styleable.CustomImgView_imgDefaultColor, 0);
        this.w = this.g.w(t.getInt(R.styleable.CustomImgView_w,0));
        this.h = this.g.h(t.getInt(R.styleable.CustomImgView_h,0));

        int width = g.w(t.getInt(R.styleable.CustomImgView_imgW,0));
        int height = g.h(t.getInt(R.styleable.CustomImgView_imgH,0));
        if ( width > 0 && height > 0) {
            Bitmap b = BitmapFactory.decodeResource(getResources(),t.getResourceId(R.styleable.CustomImgView_img,R.mipmap.ic_launcher));
            b = Bitmap.createScaledBitmap(b,width,height,true);
            this.setImageBitmap(b);
            this.setScaleType(ScaleType.CENTER_INSIDE);
        }

        if(color != 0) {
            this.setColorFilter(color);
        }

        boolean touch = t.getBoolean(R.styleable.CustomImgView_touch,false);
        if ( touch == true ) this.setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        if(this.w>0)
            widthMeasureSpec=w;
        if(this.h>0)
            heightMeasureSpec=h;
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    public void onClick(View v) {
        Activity a = (Activity)this.c;
        switch (v.getId()) {

            case R.id.status_bar_menu :
                Intent menuCall= new Intent(this.c,MenuActivity.class);
                menuCall.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                a.startActivity(menuCall);
                a.overridePendingTransition(R.anim.anim_slide_in_left,0);
                break;

            case R.id.status_bar_logo :
                Intent logo_mainCall = new Intent(this.c,MainActivity.class);
                logo_mainCall.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                a.startActivity(logo_mainCall);
                a.overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
                break;

            case R.id.status_bar_search :
                Intent Searchcontent = new Intent(this.c,ContentsActivity.class);
                Searchcontent.putExtra("paramUrl",this.g.urls.get("search"));
                Searchcontent.putExtra("title", "검색");
                Searchcontent.putExtra("num", "");
                a.startActivity(Searchcontent);
                a.overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
                break;

            case R.id.titlebar_back:
                //MainCall
                Intent back_mainCall= new Intent(this.c,MainActivity.class);
                back_mainCall.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                a.startActivity(back_mainCall);
                a.overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
                break;

            case R.id.menu_setting:
                a.startActivity(new Intent(this.c,SettingActivity.class));
                a.overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
                break;

        }

    }

}
