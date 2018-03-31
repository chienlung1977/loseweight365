package com.oli365.nc;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oli365.nc.controller.Utility;
import com.oli365.nc.model.FoodModel;

import java.util.List;

/**
 * Created by alvinlin on 2016/12/29.
 */

public class AdapterFoodList extends ArrayAdapter<FoodModel> {

    public AdapterFoodList(Context context, int resource, List<FoodModel> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_food, null);
        }

       // RecordAvgModel item = getItem(position);

        FoodModel item =getItem(position);

        if(item!=null){
            ImageView iv_image=(ImageView)v.findViewById(R.id.iv_image);
            TextView tv_date = (TextView)v.findViewById(R.id.tv_date);
            TextView tv_food_name =(TextView)v.findViewById(R.id.tv_food_name);

            if(!item.Photo.equals("")){
                Uri uri = Uri.parse("file://" + Utility.getImagePath(v.getContext()) +  item.Photo);
                iv_image.setImageURI(uri);
            }
            tv_date.setText(item.CreateDate);
            tv_food_name.setText(item.FoodName);
        }

        return v;
       // return super.getView(position, convertView, parent);
    }
}
