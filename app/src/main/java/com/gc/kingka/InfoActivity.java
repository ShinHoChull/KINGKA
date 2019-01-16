package com.gc.kingka;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.dto.InfoDTO;
import com.gc.dto.MainBannerDTO;
import com.gc.dto.MessageDTO;
import com.gc.modules.common.CustomHandler;
import com.gc.modules.common.Custom_SharedPreferences;
import com.gc.modules.common.Globar;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {

    private ViewPager viewPager;
    ArrayList<InfoDTO> arrayList;

    private LinearLayout info_backgroundV;

    //handler
    private CustomHandler customHandler;
    //share
    private Custom_SharedPreferences csp;
    private Globar g;

    private TextView closeBt;

    public void viewReset() {

        this.csp = new Custom_SharedPreferences(this);
        this.g = new Globar(this);
        this.customHandler = new CustomHandler(this);

        this.viewPager = findViewById(R.id.photos_viewpager);
        this.info_backgroundV = findViewById(R.id.info_backgroundV);
        this.info_backgroundV.setBackgroundColor(this.g.info_color[0]);
        this.closeBt = findViewById(R.id.info_closeBt);
        this.closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,R.anim.anim_slide_out_bottom_login);
            }
        });
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                info_backgroundV.setBackgroundColor(g.info_color[i]);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Log.d("onPageStateChanged",""+i);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        this.viewReset();

        //메인 배너 이미지 다운로드 받기
        new Thread() {
            @Override
            public void run() {
                super.run();
                Gson gson = new Gson();
                Message msg = customHandler.obtainMessage();
                try {
                    JsonElement je = g.getParser(g.baseUrl+g.urls.get("appInfo"));
                    Type listType = new TypeToken<ArrayList<InfoDTO>>(){}.getType();
                    ArrayList<InfoDTO> bannerArr =  gson.fromJson(je,listType);
                    arrayList = bannerArr;

                    msg.what = CustomHandler.APPINFO_CODE;
                    customHandler.sendMessage(msg);
                } catch (Exception e) {
                    msg.obj = new MessageDTO("Failed to fetch data.(AppInfo Error)",
                            e.toString());
                    msg.what = CustomHandler.ALERT_WINDOW_CODE;
                    customHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void updateInfoImage() {
        Adapter a = new Adapter(this.arrayList, this);
        this.viewPager.setAdapter(a);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(this.viewPager, true);
    }

    class Adapter extends PagerAdapter {

        Context context;
        ArrayList<InfoDTO> obj;
        private Globar g;

        Adapter(ArrayList<InfoDTO> res, Context context) {
            obj = res;
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = null;
            this.g = new Globar(this.context);
            InfoDTO r = this.obj.get(position);

            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.info_pager_adapter, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            Picasso.get().load(this.g.mainUrl+r.getFile1()).into(imageView);
            container.addView(view);

            return view;
        }

        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }


        public int getCount() {
            return obj.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
