package com.gc.modules.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.gc.dto.MainBannerDTO;
import com.gc.modules.common.Globar;
import com.gc.kingka.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomPagerAdapter extends PagerAdapter {

    private Context c;
    private Globar g;
    private LayoutInflater inflater;
    private ArrayList<MainBannerDTO> bannerArray;

    public CustomPagerAdapter(Context c, LayoutInflater inflater , ArrayList<MainBannerDTO> bannerArray) {
        this.c = c;
        this.g = new Globar(c);
        this.inflater = LayoutInflater.from(c);
        this.bannerArray = bannerArray;
    }

    //ViewPager가 현재 보여질 Item(View객체)를 생성할 필요가 있는 때 자동으로 호출
    // //쉽게 말해, 스크롤을 통해 현재 보여져야 하는 View를 만들어냄.
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        MainBannerDTO r = this.bannerArray.get(position);

        View view = null;
        view = this.inflater.inflate(R.layout.viewpaper_child,null);
        ImageView img = (ImageView)view.findViewById(R.id.img_viewpager_child);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainBannerDTO r = bannerArray.get(position);
                if ( r.getLinkurl().equals("") == false ) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(r.getLinkurl()));
                    c.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        });

        Picasso.get().load(this.g.mainUrl+r.getFile1()).into(img);

        ViewGroup.LayoutParams param = container.getLayoutParams();
        if(param == null) {
            param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int h = g.h(264);
        param.height = h;

        container.setLayoutParams(param);
        container.addView(view);

        return view;
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
        return this.bannerArray.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }



}
