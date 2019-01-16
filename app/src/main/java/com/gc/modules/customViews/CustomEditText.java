package com.gc.modules.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.gc.modules.common.Globar;
import com.gc.kingka.R;

public class CustomEditText extends AppCompatEditText {

    private int x,y,w,h;
    private Globar g;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context,attrs);
    }

    private void initView(Context c , AttributeSet a) {
        this.g = new Globar(c);
        TypedArray t = c.obtainStyledAttributes(a, R.styleable.CustomEditText);

        this.w = this.g.w(t.getInt(R.styleable.CustomEditText_w,0));
        this.h = this.g.h(t.getInt(R.styleable.CustomEditText_h,0));

        int fontSize =(int)this.g.setTextSize(t.getInt(R.styleable.CustomEditText_fontSize,12));
        this.setTextSize(fontSize);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
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



}
