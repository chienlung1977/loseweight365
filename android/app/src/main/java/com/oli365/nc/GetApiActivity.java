package com.oli365.nc;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;


public class GetApiActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_api);

        findViewById(R.id.btnGetApi).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Button b = (Button)findViewById(R.id.btnGetApi);
        b.setClickable(false);


        //與主機溝通
        new LongRunningGetIO().execute();
    }




    //innner class

    private class LongRunningGetIO extends AsyncTask<Void, Void, String> {
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


        protected String doInBackground(Void... params) {

            String text = null;

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                //String key = Utility.getKey("!qaz2wsx");
                //String realKey = Des3.encode(key).toString();
                String url ="http://api.oli365.com:8180/users?key=" + "123456";
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
                EditText et = (EditText)findViewById(R.id.my_edit);


                et.setText(results);


            }


            Button b = (Button)findViewById(R.id.btnGetApi);


            b.setClickable(true);
        }


    }


}
