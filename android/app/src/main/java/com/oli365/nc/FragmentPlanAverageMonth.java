package com.oli365.nc;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.oli365.nc.controller.RecordDAO;
import com.oli365.nc.model.RecordAvgMonthModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPlanAverageMonth extends Fragment {


    public FragmentPlanAverageMonth() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.fragment_plan_average_month, container,false);
        RecordDAO rd =new RecordDAO(getActivity());
        ListView lv =(ListView)myview.findViewById(R.id.lst_plan_content);

        List<RecordAvgMonthModel> objs = rd.getMonthAvg();
        AdapterPlanMonthAverage la = new AdapterPlanMonthAverage(getActivity(),R.layout.item_plan_avg_month,objs);

        lv.setAdapter(la);

        return myview;
    }

}
