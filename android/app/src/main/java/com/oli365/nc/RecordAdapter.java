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
 * Created by alvinlin on 2016/4/3.
 */
public class RecordAdapter extends ArrayAdapter<Record> {

    private int resource;
    private List<Record> items;

    public RecordAdapter(Context context, int resource, List<Record> objects) {
        super(context, resource, objects);
        this.resource=resource;
        this.items=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // return super.getView(position, convertView, parent);

        LinearLayout itemView;

        final Record item = getItem(position);
        if(convertView==null){

            itemView =new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li =(LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource,itemView,true);

        }
        else{
            itemView =(LinearLayout)convertView;
        }

        TextView txtWeight = (TextView)itemView.findViewById(R.id.txtWeight);
        TextView txtFatRate = (TextView)itemView.findViewById(R.id.txtFatRate);

        return itemView;
    }


    public void set(int index,Record item){
        if(index >=0 && index < items.size()){
            items.set(index,item);
            notifyDataSetChanged();
        }
    }

    public Record get(int index){
        return items.get(index);
    }
}
