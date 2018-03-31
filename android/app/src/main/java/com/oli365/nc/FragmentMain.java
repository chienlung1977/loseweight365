package com.oli365.nc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.oli365.nc.controller.DebugDAO;
import com.oli365.nc.controller.RecordCalculationDAO;
import com.oli365.nc.model.TodayDataModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentMain extends Fragment {

    private static final String TAG =ActivityMain.class.getName();
    public FragmentMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_main, container, false);

        //載入google廣告
        loadAdv(view);

        //載入資料
        bindingData(view);


        return view;
    }



    //region 載入廣告

    private void loadAdv(View view){

        String advid =getResources().getString(R.string.banner_ad_unit_id);

        if(DebugDAO.IsDebugMode()){
            advid=getResources().getString(R.string.banner_ad_unit_id_debug);
        }

        MobileAds.initialize(this.getContext().getApplicationContext(), advid);
        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


    //endregion

    //region 顯示首頁數據

    private void bindingData(View view){

        TextView fm_init_weight = (TextView)view.findViewById(R.id.fm_init_weight); //初始體重
        TextView fm_today_weight = (TextView)view.findViewById(R.id.fm_today_weight); //今日體重及增減
        TextView fm_week_weight = (TextView)view.findViewById(R.id.fm_week_weight); //這一週增減
        TextView fm_month_weight = (TextView)view.findViewById(R.id.fm_month_weight); //這一月增減

        TextView fm_init_fatrate =(TextView)view.findViewById(R.id.fm_init_fatrate);
        TextView fm_today_fatrate =(TextView)view.findViewById(R.id.fm_today_fatrate);
        TextView fm_week_fatrate =(TextView)view.findViewById(R.id.fm_week_fatrate);
        TextView fm_month_fatrate =(TextView)view.findViewById(R.id.fm_month_fatrate);




        //現在體重、體脂差異
        RecordCalculationDAO rcd =new RecordCalculationDAO(this.getContext());


        TodayDataModel tdm = rcd.getTodayData();
        fm_init_weight.setText(tdm.InitWeight);     //初始體重
        fm_init_fatrate.setText(tdm.InitFatRate);   //初始體脂
        fm_today_weight.setText(tdm.Weight + "/" + tdm.WeightDiff);    //現在體重
        fm_today_fatrate.setText(tdm.FatRate + "/" + tdm.FatRateDiff);  //現在體脂

        fm_week_weight.setText(tdm.ThisWeekWeight + "/" + tdm.ThisWeekWeightDiff);    //本週第一天體重
        fm_week_fatrate.setText(tdm.ThisWeekFatRate + "/" + tdm.ThisWeekFatRateDiff);  //本週第一天體脂

        fm_month_weight.setText(tdm.ThisMonthWeight + "/" + tdm.ThisMonthWeightDiff);    //本月第一天體重
        fm_month_fatrate.setText(tdm.ThisMonthFatRate + "/" + tdm.ThisMonthFatRateDiff);  //本月第一天體脂


        //剩餘的脂肪重量
        /*
        TextView tv_main_remaining_fatweight= (TextView) view.findViewById(R.id.tv_main_remaining_fatweight);
        if(tv_main_remaining_fatweight!=null ){
            SettingDAO sd =new SettingDAO(this.getContext());
            UserDataModel um = sd.getSetting();
            if(um!=null && !"".equals(um.BodyFatRate) && !"".equals(um.TargetFatweight)){
                DecimalFormat df =new DecimalFormat("0.#");
                String msg ="剩餘要減去的脂肪重量："  + String.valueOf( df.format(Double.valueOf(um.BodyFatWeight)-Double.valueOf( um.TargetFatweight))) + "KG";
                tv_main_remaining_fatweight.setText(msg);
            }

        }
        */

    }


    //endregion


}
