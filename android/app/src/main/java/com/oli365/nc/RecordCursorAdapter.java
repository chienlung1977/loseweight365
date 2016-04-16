package com.oli365.nc;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by alvinlin on 2016/4/3.
 */
public class RecordCursorAdapter extends CursorAdapter {
    public RecordCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        // View header = LayoutInflater.from(context).inflate(R.layout.record_header,null);

        // mList.addHeaderView(header);


        return LayoutInflater.from(context).inflate(R.layout.record_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {



        TextView txtCreateTime = (TextView) view.findViewById(R.id.txtCreateTime);
        TextView txtWeight = (TextView) view.findViewById(R.id.txtWeight);
        TextView txtFatRate = (TextView) view.findViewById(R.id.txtFatRate);

        String createTime =cursor.getString(cursor.getColumnIndexOrThrow("CREATE_TIME"));
        double weight =cursor.getDouble(cursor.getColumnIndexOrThrow("WEIGHT"));
        double fatrate =cursor.getDouble(cursor.getColumnIndexOrThrow("FAT_RATE"));


        //Date mydate =new Date(createTime);
        //SimpleDateFormat df =new SimpleDateFormat("yyyy-MM-dd HH:mm");


        txtCreateTime.setText(String.valueOf(createTime));
        txtWeight.setText(String.valueOf(weight));
        txtFatRate.setText(String.valueOf(fatrate));

    }


}
