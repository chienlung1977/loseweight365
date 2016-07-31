package com.oli365.nc;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by alvinlin on 2016/5/3.
 */
public class AsyncUtility extends AsyncTask<String, Void, String>
{

    public String result ;
    public String getResult() {
        return result;
    }


    protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
        InputStream in = entity.getContent();

        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n>0) {
            byte[] b = new byte[4096];
            n =  in.read(b);


            if (n>0) out.append(new String(b, 0, n));
        }

        return out.toString();
    }


    @Override


    protected String doInBackground(String... params) {

        String text = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            String host = "http://api.oli365.com:8180";
            //String key = Utility.getKey("!qaz2wsx");
            //String realKey = Des3.encode(key).toString();
           // String url ="http://api.oli365.com:8180/users?key=" + "123456";
            String url =host + params[0] + "?key=" + Utility.getKey();
            HttpGet httpGet = new HttpGet(url);


            HttpResponse response = httpClient.execute(httpGet, localContext);


            HttpEntity entity = response.getEntity();


            text = getASCIIContentFromEntity(entity);


        } catch (Exception e) {
            return e.getLocalizedMessage();
        }


        return text;
    }


    protected void onPostExecute(String results) {
        if (results!=null) {
            //EditText et = (EditText)findViewById(R.id.my_edit);
            this.result = results;
           // et.setText(results);
        }


       // Button b = (Button)findViewById(R.id.btnGetApi);


        //b.setClickable(true);
    }



}
