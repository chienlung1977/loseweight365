package com.oli365.nc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oli365.nc.controller.UserDAO;
import com.oli365.nc.model.CaloryModel;

import java.util.List;

/**
 * Created by alvinlin on 2016/5/6.
 */
public class CaloryAdapter extends ArrayAdapter<CaloryModel>  {

    private int resource;
    private List<CaloryModel> items;
    private static final String TAG= CaloryAdapter.class.getName();

    public CaloryAdapter(Context context,  List<CaloryModel> caloryModels) {
        super(context, 0, caloryModels);

        //this.items=calorys;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // return super.getView(position, convertView, parent);

        LinearLayout itemView;

        final CaloryModel item = getItem(position);
        if(convertView==null){

            convertView=LayoutInflater.from(getContext()).inflate(R.layout.item_calory, parent, false);
            //itemView =new LinearLayout(getContext());
            //String inflater = Context.LAYOUT_INFLATER_SERVICE;
            //LayoutInflater li =(LayoutInflater) getContext().getSystemService(inflater);
            //li.inflate(resource,itemView,true);

        }
       // else{
       //     itemView =(LinearLayout)convertView;
       // }

        //日期
        TextView txtDate = (TextView)convertView.findViewById(R.id.txtDate);
        txtDate.setText(item.getCreatedate());



        //每日可用卡路里
        TextView txtDailyCalory=(TextView) convertView.findViewById(R.id.txtDailyCalory);

        UserDAO ud =new UserDAO(convertView.getContext());
        txtDailyCalory.setText(ud.getDailyCalory());

        //已用卡路里
        TextView txtCalory = (TextView)convertView.findViewById(R.id.txtCalory);
        int totalCalory = item.getBreakfast()+item.getLunch()+item.getDinner()+item.getDessert()-item.getSport();
        txtCalory.setText(String.valueOf(totalCalory));

        //剩餘卡路里
        TextView txtRemainingCalory =(TextView)convertView.findViewById(R.id.txtRemainingCalory);
        int remaining = Integer.valueOf(ud.getDailyCalory()) - totalCalory;
        txtRemainingCalory.setText(String.valueOf(remaining));

        return convertView;
    }


    public void set(int index,CaloryModel item){
        if(index >=0 && index < items.size()){
            items.set(index,item);
            notifyDataSetChanged();
        }
    }




    public CaloryModel get(int index){
        return items.get(index);
    }

}
