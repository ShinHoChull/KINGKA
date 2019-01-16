package com.gc.modules.Adapters;

import android.content.Context;
import android.text.Html;
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

public class PhotoChoiceAdapter extends BaseAdapter {

    private ArrayList<MainPhotoDTO> items;
    private LayoutInflater inflater;
    private Globar g;
    private Context c;

    public PhotoChoiceAdapter(Context c , LayoutInflater inflater , ArrayList<MainPhotoDTO> items) {
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

            MainPhotoDTO r = this.items.get(position);

            convertView = this.inflater.inflate(R.layout.photo_choice_item,parent,false);
            TextView textView = (TextView)convertView.findViewById(R.id.photo_choice_text);

            ViewGroup.LayoutParams param = convertView.getLayoutParams();
            if(param == null) {
                param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            param.width = this.g.w(155);
            param.height = this.g.h(155);
            convertView.setLayoutParams(param);

            textView.setText(Html.fromHtml(r.getTitle()).toString());

        }

        return convertView;
    }




}
