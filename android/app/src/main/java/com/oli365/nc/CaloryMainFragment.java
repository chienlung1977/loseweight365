package com.oli365.nc;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by alvinlin on 2016/8/1.
 */
public class CaloryMainFragment extends Fragment {

    private static final String TAG= CaloryMainFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_calory_main, container,false);
        ArrayList<Calory> items =new ArrayList<Calory>();

        CaloryDAO cd =new CaloryDAO(view.getContext());
        List<Calory> cc = cd.getLatest30();
        Calory mc =null;

        Calory c =null;
        for(long i =0;i>-30;i--){
            String date = Utility.getDate((int)i);
            boolean status = false;
            for(int j=0;j<cc.size();j++){
                mc =cc.get(j);
                Log.i(TAG,"mc.getCreatedate()=" + mc.getCreatedate());
                if(date.equals(Utility.getFormatShortDate(mc.getCreatedate()))){
                    items.add(mc);
                    status=true;
                }
            }

            if(status==false){
                c =new Calory(i,date,0,0,0,0,0,"");
                items.add(c);
            }



        }






        CaloryAdapter itemAdapter =new CaloryAdapter(view.getContext(), items);
        ListView listView = (ListView) view.findViewById(R.id.caloryList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //TextView txtDate = (TextView)view.findViewById(R.id.txtDate);

               // Toast.makeText(getContext(),"date=" + txtDate.getText() + ",id=" + String.valueOf(id) , Toast.LENGTH_SHORT).show();
                showCaloryView(view);
            }
        });


        listView.setAdapter(itemAdapter);

        return view;
       // return inflater.inflate(R.layout.fragment_calory_main, container, false);
    }



    public void showCaloryView(View v){

        FragmentManager fragmentMgr = getFragmentManager();
        FragmentCaloryNew fgClaoryNew =new FragmentCaloryNew();
        FragmentTransaction fragmentTrans = fragmentMgr.beginTransaction();
        // fragmentTrans.add(R.id.fg_calory_main,fgClaoryNew);
        fragmentTrans.addToBackStack(null);
        fragmentTrans.replace(R.id.fg_calory_content, fgClaoryNew);

        fragmentTrans.commit();

        //TextView txtDate = (TextView) v.findViewById(R.id.txtDate);



    }

}
