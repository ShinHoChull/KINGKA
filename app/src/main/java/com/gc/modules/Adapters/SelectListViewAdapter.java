package com.gc.modules.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gc.dto.MenuDTO;
import com.gc.kingka.R;
import com.gc.modules.common.Globar;

import java.util.ArrayList;

public class SelectListViewAdapter extends BaseAdapter {


    private ArrayList<MenuDTO> list;
    private LayoutInflater inflater;
    private Globar g;
    private Context c;


    public SelectListViewAdapter(Context c , LayoutInflater inflater , ArrayList<MenuDTO> items) {
        this.c = c;
        this.inflater = inflater;
        this.g = new Globar(c);
        this.list = items;
    }


    @Override
    public int getCount() {
        return this.list.size();
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

            convertView = this.inflater.inflate(R.layout.select_list_item,parent,false);
            TextView textView = (TextView)convertView.findViewById(R.id.select_Text);

            ViewGroup.LayoutParams param = convertView.getLayoutParams();
            if(param == null) {
                param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            param.height = this.g.h(35);
            convertView.setLayoutParams(param);
            textView.setText("· "+list.get(position).getName());

        }

        return convertView;
    }
}
