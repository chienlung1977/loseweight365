package com.oli365.nc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oli365.nc.model.RecordAvgModel;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by alvinlin on 2016/12/7.
 */

public class AdapterPlanSevenDays extends ArrayAdapter<RecordAvgModel> {

    public AdapterPlanSevenDays(Context context, int resource, List<RecordAvgModel> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_plan_avg_day, null);
        }

        RecordAvgModel item = getItem(position);

        if(item!=null){
            TextView tv_plan_createdate=(TextView)v.findViewById(R.id.tv_plan_createdate);
            TextView tv_plan_weight=(TextView)v.findViewById(R.id.tv_plan_weight);
            TextView tv_plan_fatrate =(TextView)v.findViewById(R.id.tv_plan_fatrate);
            TextView tv_plan_fatweight=(TextView)v.findViewById(R.id.tv_plan_fatweight);

            DecimalFormat df=new DecimalFormat("#0.0#");
            tv_plan_createdate.setText(item.getCreateTime());
            tv_plan_weight.setText(df.format(item.getWeight()));
            tv_plan_fatrate.setText(df.format(item.getFatRate()));
            tv_plan_fatweight.setText(df.format(item.getFatweight()));
        }


        return v;
    }
/*
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_plan_avg_day, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tv_plan_createdate=(TextView)view.findViewById(R.id.tv_plan_createdate);
        TextView tv_plan_weight=(TextView)view.findViewById(R.id.tv_plan_weight);
        TextView tv_plan_fatrate =(TextView)view.findViewById(R.id.tv_plan_fatrate);
        TextView tv_plan_fatweight=(TextView)view.findViewById(R.id.tv_plan_fatweight);

        tv_plan_createdate.setText(cursor.getString(cursor.getColumnIndexOrThrow("body")));
        tv_plan_weight.setText(cursor.getString(cursor.getColumnIndexOrThrow("body")));
        tv_plan_fatrate.setText(cursor.getString(cursor.getColumnIndexOrThrow("body")));
        tv_plan_fatweight.setText(cursor.getString(cursor.getColumnIndexOrThrow("body")));
    }

    */
}
