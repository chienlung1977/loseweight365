package com.oli365.nc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oli365.nc.model.RecordAvgMonthModel;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by alvinlin on 2016/12/7.
 */

public class AdapterPlanMonthAverage extends ArrayAdapter<RecordAvgMonthModel> {

    public AdapterPlanMonthAverage(Context context, int resource, List<RecordAvgMonthModel> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_plan_avg_month, null);
        }

        RecordAvgMonthModel item = getItem(position);

        if(item!=null){
            TextView tv_plan_item_ym=(TextView)v.findViewById(R.id.tv_plan_item_ym);
            TextView tv_plan_item_weight =(TextView)v.findViewById(R.id.tv_plan_item_weight);
            TextView tv_plan_item_fatrate=(TextView)v.findViewById(R.id.tv_plan_item_fatrate);
            TextView tv_plan_item_fatweight=(TextView)v.findViewById(R.id.tv_plan_item_fatweight);

            DecimalFormat df=new DecimalFormat("#0.0#");
            tv_plan_item_ym.setText(item.getYm());
            tv_plan_item_weight.setText(df.format(item.getAvgweight()));
            tv_plan_item_fatrate.setText(df.format(item.getAvgfatrate()));
            tv_plan_item_fatweight.setText(df.format(item.getAvgfatweight()));
        }

        return v;
    }
}
