package com.gc.modules.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.gc.dto.MainBannerDTO;
import com.gc.dto.SponsorDTO;
import com.gc.kingka.LoginActivity;
import com.gc.kingka.PopWebviewActivity;
import com.gc.kingka.R;
import com.gc.modules.common.Globar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SponsorPagerAdapter extends PagerAdapter {

    private Context c;
    private Globar g;
    private LayoutInflater inflater;
    private ArrayList<SponsorDTO> bannerArray;
    private Activity activity;

    public SponsorPagerAdapter(Context c, LayoutInflater inflater , ArrayList<SponsorDTO> bannerArray , Activity activity) {
        this.c = c;
        this.g = new Globar(c);
        this.inflater = LayoutInflater.from(c);
        this.bannerArray = bannerArray;
        this.activity = activity;
    }

    //ViewPager가 현재 보여질 Item(View객체)를 생성할 필요가 있는 때 자동으로 호출
    // //쉽게 말해, 스크롤을 통해 현재 보여져야 하는 View를 만들어냄.
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final int realPos = position % this.bannerArray.size();
        SponsorDTO r = (SponsorDTO)this.bannerArray.get(realPos);
        View view = null;
        view = this.inflater.inflate(R.layout.viewpaper_child,null);
        ImageView img = (ImageView)view.findViewById(R.id.img_viewpager_child);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SponsorDTO r = bannerArray.get(realPos);
                if ( r.getLinkurl().equals("") == false ) {
                    Intent content = new Intent(c,PopWebviewActivity.class);
                    content.putExtra("paramUrl", r.getLinkurl());
                    content.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    c.startActivity(content);
                    activity.overridePendingTransition(R.anim.anim_slide_in_bottom_login,0);
                }
            }
        });

        Picasso.get().load(this.g.mainUrl+r.getFile()).into(img);

        ViewGroup.LayoutParams param = container.getLayoutParams();
        if(param == null) {
            param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        //param.height = g.h(50);

        container.setLayoutParams(param);
        container.addView(view);

        return view;
    }


    @Override
    public float getPageWidth(int position) {
        return 0.28f;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        if ( this.bannerArray.size() >= 3) {
            return Integer.MAX_VALUE;
        } else {
            return this.bannerArray.size();
        }

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


}
