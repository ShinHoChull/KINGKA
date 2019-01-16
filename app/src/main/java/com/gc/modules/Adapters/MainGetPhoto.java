package com.gc.modules.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.dto.MainGetPhotoDTO;
import com.gc.dto.MainPhotoDTO;
import com.gc.kingka.R;
import com.gc.modules.common.Globar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainGetPhoto extends BaseAdapter {

    private ArrayList<MainGetPhotoDTO> items;
    private LayoutInflater inflater;
    private Globar g;
    private Context c;

    public MainGetPhoto(Context c , LayoutInflater inflater , ArrayList<MainGetPhotoDTO> items) {
        this.c = c;
        this.inflater = inflater;
        this.g = new Globar(c);
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
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


            MainGetPhotoDTO r = items.get(position);
            Log.d("mainGetPhoto_Url",this.g.mainUrl+r.getUrl());

            convertView = this.inflater.inflate(R.layout.main_getphoto,parent,false);
            ImageView img = convertView.findViewById(R.id.getMainPhoto_img);

            Picasso.get().load(this.g.mainUrl+r.getUrl()).resize(this.g.w(100),0).placeholder(R.drawable.main_placeholder).error(R.mipmap.ic_launcher).into(img);

            ViewGroup.LayoutParams param = convertView.getLayoutParams();
            if(param == null) {
                param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            param.width = this.g.w(100);
            param.height = this.g.h(105);
            convertView.setLayoutParams(param);

        }

        return convertView;
    }


}
