package com.oli365.nc;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alvinlin on 2016/6/22.
 */
public class DatePickerFragment extends DialogFragment  implements DatePickerDialog.OnDateSetListener {


    private static final String TAG=DatePickerFragment.class.getName();

    private Boolean isSet=false;
    private int yy;
    private int mm;
    private int dd;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        if(!args.getString("target_date").equals("")){
            String dateString =args.getString("target_date");
            Log.i(TAG,"setArguments run target_date=" + dateString);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                Date d = sdf.parse(dateString);

                isSet=true;
                yy=d.getYear()+1900;
                mm=d.getMonth();
                dd=d.getDate();
                Log.i(TAG,"setArguments run yy=" + String.valueOf(yy) + ",mm=" + String.valueOf(mm) + ",dd=" + String.valueOf(dd));
            }
            catch (Exception ex){
                Log.w(TAG,ex.toString());
            }

        }
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG,"onCreateDialog run isSet=" + isSet.toString());
        // Use the current time as the default values for the picker
        if(isSet==false){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) +1;
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
         //   return new DatePickerDialog(getActivity(), (ActivitySettings)getActivity(), year, month, day);
            return null;
        }
        else{
            Log.i(TAG,"onCreateDialog run yy=" + String.valueOf(yy) + ",mm=" + String.valueOf(mm) + ",dd=" + String.valueOf(dd));
           // return new DatePickerDialog(getActivity(), (ActivitySettings)getActivity(), yy, mm, dd);
            return null;
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


        Log.d(TAG,"DatePickerFragment onDateSet,year=" + year + ",month=" + monthOfYear + ",day=" + dayOfMonth);
       // Toast.makeText(getActivity(), year, Toast.LENGTH_SHORT).show();
    }
}
