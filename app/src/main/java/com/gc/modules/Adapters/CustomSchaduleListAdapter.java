package com.gc.modules.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gc.dto.ScheduleDTO;
import com.gc.modules.common.Globar;
import com.gc.kingka.R;

import java.util.ArrayList;

public class CustomSchaduleListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Globar g;
    private Context c;
    private ArrayList<ScheduleDTO> array;

    public CustomSchaduleListAdapter(Context c, LayoutInflater inflater, ArrayList<ScheduleDTO> array) {
        this.c = c;
        this.inflater = inflater;
        this.g = new Globar(c);
        this.array = array;
    }

    @Override
    public int getCount() {
        return this.array.size();
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

        Log.d("position!", "position" + position);

        convertView = this.inflater.inflate(R.layout.schadule_list_item, parent, false);
        TextView dayTv = (TextView) convertView.findViewById(R.id.schadule_list_day_item);
        TextView tv = (TextView) convertView.findViewById(R.id.schadule_list_tv_item);

        ViewGroup.LayoutParams param = convertView.getLayoutParams();
        if (param == null) {
            param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        param.height = this.g.h(50);
        convertView.setLayoutParams(param);
        if (this.array != null) {
            ScheduleDTO r = array.get(position);
            String[] sCut = r.getSdate().split("-");
            String[] eCut = r.getEdate().split("-");
            String sDate = sCut[sCut.length - 1];
            String eDate = eCut[eCut.length - 1];

            if ( eDate.equals("") == false ) {
                dayTv.setText(sCut[sCut.length - 1] + " ~ " +eCut[eCut.length - 1]);
            } else {
                dayTv.setText(sCut[sCut.length - 1]);
            }
            String cateText = r.getCategory().equals("1")? "〈국제〉 " : "〈국내〉 ";
            tv.setText(cateText+r.getSubject());
        }


        return convertView;
    }


}
