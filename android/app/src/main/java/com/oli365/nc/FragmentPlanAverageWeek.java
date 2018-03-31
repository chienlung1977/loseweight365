package com.oli365.nc;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.oli365.nc.controller.RecordDAO;
import com.oli365.nc.model.RecordAvgWeekModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPlanAverageWeek extends Fragment {


    public FragmentPlanAverageWeek() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.fragment_plan_average_week, container,false);

        RecordDAO rd =new RecordDAO(getActivity());
        ListView lv =(ListView)myview.findViewById(R.id.lst_plan_content);
        List<RecordAvgWeekModel> objs = rd.getWeekAvg();
        AdapterPlanWeekAverage la = new AdapterPlanWeekAverage(getActivity(),R.layout.item_plan_avg_week,objs);

        lv.setAdapter(la);

        return myview;
    }

}
