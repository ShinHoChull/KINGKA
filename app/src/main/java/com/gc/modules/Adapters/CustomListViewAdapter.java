package com.gc.modules.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gc.dto.MenuDTO;
import com.gc.modules.common.Globar;
import com.gc.kingka.R;

import java.util.ArrayList;

public class CustomListViewAdapter extends BaseAdapter {

    private ArrayList<MenuDTO> items;
    private LayoutInflater inflater;
    private Globar g;
    private Context c;

    public CustomListViewAdapter(Context c , LayoutInflater inflater , ArrayList<MenuDTO> items) {
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

            convertView = this.inflater.inflate(R.layout.menu_tableview_child,parent,false);
            TextView textView = (TextView)convertView.findViewById(R.id.menu_listview_text);

            ViewGroup.LayoutParams param = convertView.getLayoutParams();
            if(param == null) {
                param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            param.height = this.g.h(40);
            convertView.setLayoutParams(param
            );
            textView.setText(items.get(position).getName());
            Log.v("listView Adapter",items.get(position).getName());
        }

        return convertView;
    }
}
