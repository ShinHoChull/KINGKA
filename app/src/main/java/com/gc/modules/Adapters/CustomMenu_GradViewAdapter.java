package com.gc.modules.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.modules.common.Globar;
import com.gc.kingka.R;

public class CustomMenu_GradViewAdapter extends BaseAdapter {

    private Integer[] mainIcon = {
            R.drawable.menu_sidem1,
            R.drawable.menu_sidem2,
            R.drawable.menu_sidem3,
            R.drawable.menu_sidem4,
            R.drawable.menu_sidem5,
            R.drawable.menu_sidem6,
            R.drawable.menu_sidem7,
    };

    private String[] names = {
            "학회소개",
            "학술행사",
            "회원공간",
            "산하연구회",
            "학회지 JGC",
            "일반자료실",
            "교육자료"
    };

    private Globar g;
    private Context c;
    private LayoutInflater inflater;
    public int selectPostion;


    public CustomMenu_GradViewAdapter(Context c, LayoutInflater inflater) {
        this.c = c;
        this.g = new Globar(c);
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return this.mainIcon.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if ( convertView == null ) {
            convertView = this.inflater.inflate(R.layout.menu_gridview_child,parent,false);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.menu_gridview_img);
            TextView textView = (TextView)convertView.findViewById(R.id.menu_gridview_text);

            ViewGroup.LayoutParams param = convertView.getLayoutParams();
            if(param == null) {
                param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            param.width = this.g.w(80);
            param.height = this.g.h(78);
            convertView.setLayoutParams(param);
            textView.setText(this.names[position]);
            imageView.setImageResource(this.mainIcon[position]);
            if ( this.selectPostion == position ) {
                convertView.setBackgroundResource(R.color.main_color_blue);
                imageView.setColorFilter(Color.WHITE);
                textView.setTextColor(Color.WHITE);
            } else {
                imageView.setColorFilter(this.c.getResources().getColor(R.color.main_color_navy));
                textView.setTextColor(this.c.getResources().getColor(R.color.main_color_navy));
                convertView.setBackgroundResource(R.color.main_color_gray);
            }

        }

        return convertView;
    }

}
