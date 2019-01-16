package com.gc.modules.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gc.dto.MainPhotoDTO;
import com.gc.dto.MenuDTO;
import com.gc.kingka.R;
import com.gc.modules.common.Globar;

import java.util.ArrayList;

public class CustomPhotoSpinerAdapter extends BaseAdapter {

    private ArrayList<MainPhotoDTO> items;
    private LayoutInflater inflater;
    private Globar g;
    private Context c;

    public CustomPhotoSpinerAdapter(Context c , LayoutInflater inflater , ArrayList<MainPhotoDTO> items) {
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

            convertView = this.inflater.inflate(R.layout.nomar_spinner,parent,false);
            TextView textView = (TextView)convertView.findViewById(R.id.spinner_nomarText);

            ViewGroup.LayoutParams param = convertView.getLayoutParams();
            if(param == null) {
                param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            param.height = this.g.h(35);
            convertView.setLayoutParams(param);
            String text = items.get(position).getTitle();
            textView.setText(text);

        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(R.layout.spinner_dropdown, parent, false);
        }

        ViewGroup.LayoutParams param = convertView.getLayoutParams();
        if(param == null) {
            param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        param.height = this.g.h(40);
        convertView.setLayoutParams(param);

        //데이터세팅
        String text = items.get(position).getTitle();
        ((TextView)convertView.findViewById(R.id.spinner_customText)).setText(text);

        return convertView;
    }
}
