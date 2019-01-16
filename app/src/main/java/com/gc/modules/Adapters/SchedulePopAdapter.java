package com.gc.modules.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gc.dto.MenuDTO;
import com.gc.dto.ScheduleDTO;
import com.gc.kingka.R;
import com.gc.modules.common.Globar;

import java.util.ArrayList;

public class SchedulePopAdapter extends BaseAdapter {

    private ArrayList<ScheduleDTO> list;
    private LayoutInflater inflater;
    private Globar g;
    private Context c;


    public SchedulePopAdapter(Context c , LayoutInflater inflater , ArrayList<ScheduleDTO> items) {
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

            convertView = this.inflater.inflate(R.layout.schedule_pop_list_item,parent,false);
            TextView textView = (TextView)convertView.findViewById(R.id.schedule_pop_Text);

            ViewGroup.LayoutParams param = convertView.getLayoutParams();
            if(param == null) {
                param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            param.height = this.g.h(45);
            convertView.setLayoutParams(param);
            String cateText = list.get(position).getCategory().equals("1")? "〈국제〉 " : "〈국내〉 ";
            textView.setText(cateText+list.get(position).getSubject());

        }

        return convertView;
    }


}
