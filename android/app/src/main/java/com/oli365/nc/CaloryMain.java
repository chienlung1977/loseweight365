package com.oli365.nc;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class CaloryMain extends AppCompatActivity {

    private static final String TAG="CaloryMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calory_main);


        //初始fragment終
        if (savedInstanceState == null) {
            Fragment newFragment = new Calory_Main_Fragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(android.R.id.content, newFragment).commit();
        }

        bindingData();
    }

    public static class Calory_Main_Fragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
           // EditText v = new EditText(getActivity());
            //v.setText("Hello Fragment!");
           // return v;
            return inflater.inflate(R.layout.fragment_calory_main, container, false);
        }
    }


    private void bindingData(){

        ArrayList<Calory> items =new ArrayList<Calory>();

        //todo 此處要取得calory清單，後加入arraylist

        Calory c =null;

        for(long i =0;i>-30;i--){
            String date = Utility.getDate((int)i);
            c =new Calory(i,date,0,0,0,0,0,"");
            items.add(c);
        }




        CaloryAdapter itemAdapter =new CaloryAdapter(this, items);
        ListView listView = (ListView) findViewById(R.id.caloryList);
        listView.setAdapter(itemAdapter);

    }

    public void showCaloryView(View v){
        //todo 須要使用fragment切換到當日卡路里


        FragmentManager fragmentMgr = getFragmentManager();
        android.app.Fragment fgClaoryNew =new FragmentCaloryNew();
       // android.app.Fragment fgClaoryNew =fragmentMgr.findFragmentById(R.id.fg_calory_main) ;
        FragmentTransaction fragmentTrans = fragmentMgr.beginTransaction();
        fragmentTrans.add(R.id.fg_calory_main,fgClaoryNew);
        fragmentTrans.addToBackStack(null);
        //fragmentTrans.replace(R.id.frameLay, myFragmentB, "My fragment B");

        fragmentTrans.commit();
        fragmentMgr.executePendingTransactions();
        //TextView txtDate = (TextView) v.findViewById(R.id.txtDate);


        //Log.i(TAG, "click :" + txtDate.getText());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.daily_calory, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

}
