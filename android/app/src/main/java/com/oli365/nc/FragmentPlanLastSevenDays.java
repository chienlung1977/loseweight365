package com.oli365.nc;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.oli365.nc.controller.RecordDAO;
import com.oli365.nc.controller.SettingDAO;
import com.oli365.nc.model.RecordAvgModel;
import com.oli365.nc.model.UserDataModel;

import java.text.DecimalFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPlanLastSevenDays extends Fragment {


    public FragmentPlanLastSevenDays() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myview = inflater.inflate(R.layout.fragment_plan_lastsevendays, container,false);
        SettingDAO sd =new SettingDAO(getActivity());
        UserDataModel sm = sd.getSetting();
        RecordDAO rd =new RecordDAO(getActivity());
        //初始體重體脂重量
        TextView txtPlanInitWeight=(TextView)myview.findViewById(R.id.txtPlanInitWeight);
        txtPlanInitWeight.setText(sm.InitWeight);

        TextView txtPlanInitFatRate =(TextView)myview.findViewById(R.id.txtPlanInitFatRate);
        txtPlanInitFatRate.setText(sm.InitFatrate);

        //初始脂肪重量
        TextView txtPlanInitFatWeight =(TextView)myview.findViewById(R.id.txtPlanInitFatWeight);
        txtPlanInitFatWeight.setText(sm.InitFatweight);


        RecordAvgModel rm =rd.getLastSevenDaysTotalAvg();

        if(rm!=null){
            DecimalFormat df = new DecimalFormat("#0.#");

            TextView txtPlanWeight = (TextView)myview.findViewById(R.id.txtPlanWeight);
            txtPlanWeight.setText(df.format(rm.getWeight()));

            TextView txtPlanFatRate =(TextView)myview.findViewById(R.id.txtPlanFatRate);
            txtPlanFatRate.setText(df.format(rm.getFatRate()));

            TextView txtPlanFatWeight =(TextView)myview.findViewById(R.id.txtPlanFatWeight);
            txtPlanFatWeight.setText(df.format(rm.getFatweight()));



            //差異體重
            TextView txtPlanDiffWeight = (TextView)myview.findViewById(R.id.txtPlanDiffWeight);
            Double diffWeight = Double.parseDouble(txtPlanInitWeight.getText().toString()) - Double.parseDouble(txtPlanWeight.getText().toString());
            txtPlanDiffWeight.setText(df.format(diffWeight));

            //差異體脂
            TextView txtPlanDiffFatRate =(TextView)myview.findViewById(R.id.txtPlanDiffFatRate);
            Double diffFatrate = Double.parseDouble(txtPlanInitFatRate.getText().toString()) -  Double.parseDouble(txtPlanFatRate.getText().toString());
            txtPlanDiffFatRate.setText(df.format(diffFatrate));

            //差異脂肪重量
            TextView txtPlanDiffFatWeight =(TextView)myview.findViewById(R.id.txtPlanDiffFatWeight);
            Double diffFatweight = Double.parseDouble(txtPlanInitFatWeight.getText().toString()) -  Double.parseDouble(txtPlanFatWeight.getText().toString());
            txtPlanDiffFatWeight.setText(df.format(diffFatweight));

            //差異體重比例
            TextView txtPlanLoseWeightRate=(TextView)myview.findViewById(R.id.txtPlanLoseWeightRate);
            Double diffWeightrate = Double.parseDouble(txtPlanDiffWeight.getText().toString()) / Double.parseDouble(txtPlanInitWeight.getText().toString())*100;
            txtPlanLoseWeightRate.setText(df.format(diffWeightrate)+"%");


            //差異體脂比率
            TextView txtPlanLoseFatRate=(TextView)myview.findViewById(R.id.txtPlanLoseFatRate);
            Double diffFatratePercent = Double.parseDouble(txtPlanDiffFatRate.getText().toString()) / Double.parseDouble(txtPlanInitFatRate.getText().toString())*100;
            txtPlanLoseFatRate.setText(df.format(diffFatratePercent)+"%");


            //差異脂肪重量比率
            TextView txtPlanLoseFatWeightRate=(TextView)myview.findViewById(R.id.txtPlanLoseFatWeightRate);
            Double diffFatweightPercent = Double.parseDouble(txtPlanDiffFatWeight.getText().toString()) / Double.parseDouble(txtPlanInitFatWeight.getText().toString())*100;
            txtPlanLoseFatWeightRate.setText(df.format(diffFatweightPercent)+"%");

        }

        //清單內容
        ListView lv =(ListView)myview.findViewById(R.id.lst_plan_content);
        List<RecordAvgModel> objs = rd.getLastSevenDaysAvg();
        AdapterPlanSevenDays la = new AdapterPlanSevenDays(getActivity(),R.layout.item_plan_avg_day,objs);

        lv.setAdapter(la);

        return myview;
    }

}
