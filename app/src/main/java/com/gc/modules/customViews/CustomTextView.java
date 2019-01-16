package com.gc.modules.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.gc.modules.common.Globar;
import com.gc.kingka.R;

public class CustomTextView extends AppCompatTextView {

    private int x,y,w,h;
    private Globar g;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context,attrs);
    }

    private void initView(Context c , AttributeSet a) {
        this.g = new Globar(c);
        TypedArray t = c.obtainStyledAttributes(a, R.styleable.CustomTextView);

        this.w = this.g.w(t.getInt(R.styleable.CustomTextView_w,0));
        this.h = this.g.h(t.getInt(R.styleable.CustomTextView_h,0));
        int fontSize =(int)this.g.setTextSize(t.getInt(R.styleable.CustomTextView_fontSize,12));
        boolean isPhoto = (boolean) t.getBoolean(R.styleable.CustomTextView_isPhoto,false);//this.g.setTextSize();
        if ( isPhoto == true ) {
            this.setTextSize(16);
        } else {
            this.setTextSize(fontSize);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        if(this.w>0) {
            widthMeasureSpec = this.w;
            getLayoutParams().width = this.w;
        }

        if(this.h > 0) {
            heightMeasureSpec = this.h;
            getLayoutParams().height = this.h;
        }
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//    }
}
