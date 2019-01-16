package com.gc.modules.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.modules.common.Globar;
import com.gc.kingka.R;

public class CustomGridViewAdapter extends BaseAdapter {

    private Integer[] mainIcon = {
            R.drawable.main_menu1,
            R.drawable.main_menu2,
            R.drawable.main_menu3,
            R.drawable.main_menu4,
            R.drawable.main_menu5,
            R.drawable.main_menu6,
    };

    private String[] names = {
            "학회소개",
            "학술행사",
            "회원공간",
            "산하연구회",
            "학회지 JGC",
            "일반자료실"
    };

    private Integer[] mainColor = {
            R.color.main_bt1,
            R.color.main_bt2,
            R.color.main_bt3,
            R.color.main_bt4,
            R.color.main_bt5,
            R.color.main_bt6,
    };

    private Globar g;
    private Context c;
    private LayoutInflater inflater;


    public CustomGridViewAdapter(Context c, LayoutInflater inflater) {
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

            convertView = this.inflater.inflate(R.layout.main_gridview_child,parent,false);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.main_gridview_img);
            TextView textView = (TextView)convertView.findViewById(R.id.main_gridview_text);

            ViewGroup.LayoutParams param = convertView.getLayoutParams();
            if(param == null) {
                param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            param.height = this.g.h(130);
            convertView.setLayoutParams(param);

            textView.setText(this.names[position]);
            imageView.setImageResource(this.mainIcon[position]);
            convertView.setBackgroundResource(this.mainColor[position]);
        }

        return convertView;
    }
}
