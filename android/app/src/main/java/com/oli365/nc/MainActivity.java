package com.oli365.nc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class MainActivity extends AppCompatActivity {

    private ShareActionProvider mShareActionProvider;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TextView v =(TextView) findViewById(R.id.myid);
        SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(this);

        v.setText(prefs.getString("weight","0"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
       // mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        Intent intent;

        switch (item.getItemId()){
            case R.id.action_mymenu_settings:
                openSettings();
            case R.id.action_mymenu_calory:
                //openSettings();
                return true;
            case R.id.action_mymenu_weight:
                openBodyRecord();
                return true;
            case R.id.action_customer_query:
                //openCalory();
                return true;
            case R.id.action_customer_product:
                //openCalory();
                return true;
            case R.id.action_customer_weight:
                //openCalory();
                return true;
            case R.id.action_about_us:

                openAboutUs();
                return true;
            case R.id.action_get_api:
                intent=new Intent(this,GetApiActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_menu_register:
                intent=new Intent(this,RegisterActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_menu_login:
                intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void openAboutUs(){

        Intent i =new Intent(this,AboutUs.class);
        startActivity(i);

    }


    public void openBodyRecord(){

        Intent i =new Intent(this,BodyRecordMain.class);
        startActivity(i);
    }

    public void openCustomer(){

       // inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void openSettings(){

        Intent i =new Intent(this,SettingsActivity.class);
        startActivity(i);

/*
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment fragment1 = new SettingsFragment();
        fragmentTransaction.replace(android.R.id.content, fragment1);
        //fragmentTransaction.replace(android.R.id.,fragment1);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
*/
    }

    public void openBase(){
/*
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment fragment1 = new SettingsFragment();
        fragmentTransaction.replace(android.R.id.content, fragment1);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
*/
    }

    public void openCalory(){}


    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }


}
