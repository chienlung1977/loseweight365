package com.oli365.nc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

public class CaloryMain extends AppCompatActivity {

    private static final String TAG= CaloryMain.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calory_main);


        if (findViewById(R.id.fg_calory_content) != null) {
            if(savedInstanceState!=null){
                return ;
            }
            CaloryMainFragment fg  = new CaloryMainFragment();
            fg.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction()
                    .add(R.id.fg_calory_content, fg).commit();

        }

        //初始fragment
        /*
        if (savedInstanceState == null) {
            CaloryMainFragment newFragment = new CaloryMainFragment();
            FragmentTransaction ft = getSupportFragmentManager().get();
            ft.replace(R.id.fg_calory_content, newFragment,"testfragment").commit();

        }

*/
       //bindingData();
    }

    /*
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
*/

    /*
    private void bindingData(){

        ArrayList<Calory> items =new ArrayList<Calory>();



        Calory c =null;

        for(long i =0;i>-30;i--){
            String date = Utility.getDate((int)i);
            c =new Calory(i,date,0,0,0,0,0,"");
            items.add(c);
        }




        Fragment fg = (Calory_Main_Fragment)getFragmentManager().findFragmentByTag("testfragment");
        LinearLayout ll = (LinearLayout) fg.getView().findViewById(R.id.ll_calory_main);
        CaloryAdapter itemAdapter =new CaloryAdapter(this, items);
       // Log.i(TAG,getFragmentManager().findFragmentById(R.id.fg_calory_main));
        ListView listView = (ListView) ll.findViewById(R.id.caloryList);

       // listView.setAdapter(itemAdapter);

    }
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.daily_calory, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

}
