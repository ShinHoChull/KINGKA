package com.gc.modules.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gc.kingka.R;

public class TopV extends RelativeLayout implements View.OnTouchListener{

    private RelativeLayout r;
    private ImageView menuBt;
    private ImageView logo;
    private ImageView searchBt;
    public Globar g;


    public TopV(Context context) {
        super(context);
        this.g = new Globar((Activity) context);
        this.init();
    }

    public TopV(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TopV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void viewReset() {
        inflate(getContext(),R.layout.top_v,this);
        this.r = findViewById(R.id.top_r);
        this.menuBt = findViewById(R.id.menu);
        this.logo = findViewById(R.id.logo);
        this.searchBt = findViewById(R.id.search);
    }

    public void init() {
        this.viewReset();

        this.setBackgroundColor(Color.WHITE);
        this.r.setX(-2);
        this.r.setY(0);
        this.r.getLayoutParams().width = g.w(g.w(362));
        this.r.getLayoutParams().height = g.h(g.h(50));
        this.r.setBackground(getResources().getDrawable(R.drawable.top_back));

        this.menuBt.setId(new Integer(1));
        this.menuBt.setX(g.x(18));
        this.menuBt.setY(g.y(17));
        this.menuBt.getLayoutParams().width = g.w(15);
        this.menuBt.getLayoutParams().height = g.h(15);
        this.menuBt.setImageResource(R.drawable.main_btnhamber);
        this.menuBt.setScaleType(ImageView.ScaleType.FIT_XY);
        this.menuBt.setOnTouchListener(this);


        this.logo.setId(new Integer(2));
        this.logo.setX(g.x(128));
        this.logo.setY(g.y(11));
        this.logo.getLayoutParams().width = g.w(104);
        this.logo.getLayoutParams().height = g.h(27);
        this.logo.setImageResource(R.drawable.main_toplogo);
        this.logo.setScaleType(ImageView.ScaleType.FIT_XY);
        this.logo.setOnTouchListener(this);



        this.searchBt.setId(new Integer(3));
        this.searchBt.setX(g.x(326));
        this.searchBt.setY(g.y(16));
        this.searchBt.getLayoutParams().width = g.w(17);
        this.searchBt.getLayoutParams().height = g.h(17);
        this.searchBt.setImageResource(R.drawable.main_btnsearch);
        this.searchBt.setScaleType(ImageView.ScaleType.FIT_XY);
        this.searchBt.setOnTouchListener(this);


    }


    @Override
    public boolean onTouch(View v , MotionEvent event) {

        switch (v.getId()) {
            case 1:
                Toast.makeText(getContext(),"menu",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getContext(),"logo",Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(getContext(),"search",Toast.LENGTH_SHORT).show();
                break;
        }

        return false;
    }

}
