package com.oli365.nc;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.oli365.nc.controller.FoodDAO;
import com.oli365.nc.model.FoodModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFoodList extends Fragment {

    private View view ;
    private ListView lv_food_list; //結果值
    private String typeUid;
    private FoodModel.MAIN_FOOD_TYPE mainType;  //前頁傳入值
    private String date;

    private TextView tv_date;
    private TextView tv_main_food_type;

    private Activity mActivity;
    AppCompatActivity mAppCompatActivity;

    public FragmentFoodList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //前頁傳入值
            date =getArguments().getString("DATE");
            typeUid= getArguments().getString("TYPE_UID");
            mainType = FoodModel.MAIN_FOOD_TYPE.valueOf(getArguments().getString("MAIN_TYPE"));
        }

        mActivity =getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setHasOptionsMenu(true);
        view =inflater.inflate(R.layout.fragment_food_list, container, false);

        initControls();

        //檢查是否已有資料
        bindingListView(date,mainType);



      //  mAppCompatActivity =(AppCompatActivity)mActivity;
      //  mAppCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
      //  mAppCompatActivity.setTitle("食物清單");

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_food, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_food_goback:
                goBack();
                //Utility.showMessage(getActivity(),"返回");
               return true;
            case R.id.menu_food_new:
                goForward();
                //Utility.showMessage(getActivity(),"新增");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }

    private void initControls(){
        tv_date= (TextView)view.findViewById(R.id.tv_date);
        tv_main_food_type=(TextView)view.findViewById(R.id.tv_main_food_type);
        tv_date.setText(date);
        String typeName ="";
        switch (mainType){
            case BREAKFAST:
                typeName="早餐";
                break;
            case LUNCH:
                typeName="午餐";
                break;
            case DINNER:
                typeName="晚餐";
                break;
            case DESSERT:
                typeName="點心";
                break;
            case SPORT:
                typeName="運動";
                break;
        }
        tv_main_food_type.setText(typeName);
    }


    private void bindingListView(String date, FoodModel.MAIN_FOOD_TYPE mainFoodType){
        lv_food_list = (ListView)view.findViewById(R.id.lv_food_list);

        FoodDAO fd =new FoodDAO(getActivity());
        List<FoodModel> al =fd.getList(date,mainFoodType);

        AdapterFoodList af =new AdapterFoodList(getActivity(),R.layout.item_food,al);
        lv_food_list.setAdapter(af);
    }


    private void goForward(){

        FragmentManager fm  = getFragmentManager();
        FragmentFoodNew ffn =new FragmentFoodNew();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle b =new Bundle();
        b.putString("DATE",date);
        b.putString("MAIN_TYPE",mainType.toString());
        b.putString("TYPE_UID",typeUid);
        ffn.setArguments(b);
        ft.replace(R.id.fg_calory_content, ffn);
        ft.commit();

    }

    private void goBack(){

        getFragmentManager().popBackStack();
        /*
        FragmentManager fm  = getFragmentManager();

        FragmentCaloryNew fcn =new FragmentCaloryNew();
        FragmentTransaction ft = fm.beginTransaction();
        // ft.addToBackStack(null);
        ft.replace(R.id.fg_calory_content, fcn);

        ft.commit();
*/
    }

}
