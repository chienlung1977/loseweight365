package com.oli365.nc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alvinlin on 2016/5/6.
 */
public class CaloryAdapter extends ArrayAdapter<Calory>  {

    private int resource;
    private List<Calory> items;
    private static final String TAG= CaloryAdapter.class.getName();

    public CaloryAdapter(Context context,  List<Calory> calorys) {
        super(context, 0, calorys);

        //this.items=calorys;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // return super.getView(position, convertView, parent);

        LinearLayout itemView;

        final Calory item = getItem(position);
        if(convertView==null){

            convertView=LayoutInflater.from(getContext()).inflate(R.layout.calory_item, parent, false);
            //itemView =new LinearLayout(getContext());
            //String inflater = Context.LAYOUT_INFLATER_SERVICE;
            //LayoutInflater li =(LayoutInflater) getContext().getSystemService(inflater);
            //li.inflate(resource,itemView,true);

        }
       // else{
       //     itemView =(LinearLayout)convertView;
       // }

        TextView txtDate = (TextView)convertView.findViewById(R.id.txtDate);
        TextView txtCalory = (TextView)convertView.findViewById(R.id.txtCalory);

        txtDate.setText(item.createdate);
        int totalCalory = item.getBreakfast()+item.getLunch()+item.getDinner()+item.getDessert()-item.getSport();
        txtCalory.setText(String.valueOf(totalCalory));

        return convertView;
    }


    public void set(int index,Calory item){
        if(index >=0 && index < items.size()){
            items.set(index,item);
            notifyDataSetChanged();
        }
    }




    public Calory get(int index){
        return items.get(index);
    }

}
