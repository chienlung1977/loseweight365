package com.oli365.nc.controller;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;

/**
 * Created by alvinlin on 2016/11/10.
 */

public class ActionDAO {

    Context context ;

    public ActionDAO(Context context){
        this.context =context;
    }

    //打指定電話
    public void Call(String phoneNumber){

        String number ="tel:" + phoneNumber;
        String permission = "android.permission.CALL_PHONE";
        int res =context.checkCallingOrSelfPermission(permission);
        if(res == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
            context.startActivity(intent);
        }
        else{
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
            context.startActivity(intent);

        }

    }

    //地圖(範本)
    public void openMap(View v){

        /*
        Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);


        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
        boolean isIntentSafe = activities.size() > 0;


        if (isIntentSafe) {
            startActivity(mapIntent);
        }
*/

    }

    //預設開啓註冊網站
    public void openWeb(){

        openWeb("http://srv.oli365.com/Account/Register_Member.aspx");
    }

    public void openWeb(String url ){

        Uri webpage = Uri.parse(url);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
    }


    //email
    public void openEmail(String subject,String content){

        Intent intent = new Intent(Intent.ACTION_SEND);
        //intent.setType("text/plain");
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"chienlung1977@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        Intent mailer = Intent.createChooser(intent, null);
        context.startActivity(mailer);
/*
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// The intent does not have a URI, so declare the "text/plain" MIME type
        emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jon@example.com"}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
// You can also attach multiple items by passing an ArrayList of Uris
*/

    }

    //日曆
    public void openCalendar(View v){
/*
        Intent calendarIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
        Calendar beginTime = Calendar.getInstance().set(2012, 0, 19, 7, 30);
        Calendar endTime = Calendar.getInstance().set(2012, 0, 19, 10, 30);
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.Events.TITLE, "Ninja class");
        calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Secret dojo");
        */

    }
}
