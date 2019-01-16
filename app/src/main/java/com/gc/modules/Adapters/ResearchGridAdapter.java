package com.gc.modules.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.dto.MenuDTO;
import com.gc.kingka.R;
import com.gc.modules.common.Globar;

import java.util.ArrayList;

public class ResearchGridAdapter extends BaseAdapter {

    private Globar g;
    private Context c;
    private LayoutInflater inflater;
    public int selectPostion;

    private ArrayList<MenuDTO> array;

    public ResearchGridAdapter(Context c, LayoutInflater inflater , ArrayList<MenuDTO> arr) {
        this.c = c;
        this.g = new Globar(c);
        this.inflater = inflater;
        this.array = arr;
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
        Log.v("selectPosition",String.valueOf(this.selectPostion));
        if ( convertView == null ) {

            MenuDTO r = (MenuDTO)array.get(position);

            convertView = this.inflater.inflate(R.layout.research_list_child,parent,false);
            TextView mainText = (TextView)convertView.findViewById(R.id.research_main_text);
            TextView subText = (TextView)convertView.findViewById(R.id.research_wait_text);

            ViewGroup.LayoutParams param = convertView.getLayoutParams();
            if(param == null) {
                param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            param.width = this.g.w(155);
            param.height = this.g.h(150);
            convertView.setLayoutParams(param);

            mainText.setText(Html.fromHtml(r.getName()).toString());
            mainText.setTextColor(Color.WHITE);

            if ( r.getMain().equals("Y") ) {
                subText.setVisibility(View.GONE);
            }

            convertView.setBackgroundResource(R.drawable.research_back);


        }

        return convertView;
    }
}
