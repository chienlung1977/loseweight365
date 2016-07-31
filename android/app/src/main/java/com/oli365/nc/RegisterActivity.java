package com.oli365.nc;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getCanonicalName();
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //離開焦點時檢查email帳號是否已存在
        final EditText txtEmail =(EditText)findViewById(R.id.txtEmail);


        txtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && txtEmail.getText().length()>0) {
                    try {

                        //檢核email帳號規則
                        if(checkEmail()==false){

                            Toast.makeText(getApplicationContext(),"Email格式不正確", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            //檢查主機是否帳號已存在
                            new AsyncUtility().execute(txtEmail.getText().toString());
                        }



                    }
                    catch (Exception ex){
                        //Toast.makeText(getApplicationContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG,ex.getMessage());
                    }

                }
            }

        });


    }


    public void closeActivity(View view){
        finish();
    }



    private boolean checkEmail() {

        EditText txtEmail =(EditText)findViewById(R.id.txtEmail);
        String email = txtEmail.getText().toString();

       // org.apache.commons.validator.routines.EmailValidator
        EmailValidator ev = org.apache.commons.validator.routines.EmailValidator.getInstance();
        return ev.isValid(email);

    }

    //註冊新的帳號
    public void saveData(View view){

        try{


        EditText txtEmail =(EditText)findViewById(R.id.txtEmail);
        EditText txtPassword =(EditText)findViewById(R.id.txtPassword);
        EditText txtNickName =(EditText)findViewById(R.id.txtNickName);

        String email =txtEmail.getText().toString();
        String pwd = txtPassword.getText().toString();
        String encPwd = Des3.encode(pwd).toString();
        String newEncPwd = EncDes3.encode(pwd).toString();
        String nickName = txtNickName.getText().toString();

        if(email.length()==0){
           // Toast.makeText(getApplicationContext(), "請輸入新帳號Email", Toast.LENGTH_SHORT).show();
            txtEmail.setError("請輸入新帳號Email");
        }

        if(Utility.isEmail(email)==false){
            txtEmail.setError("Email格式不正確");
        }

        if(pwd.length()==0){
            txtPassword.setError("請輸入新密碼");
        }

            if(pwd.length()<6){
                txtPassword.setError("密碼長度至少六碼");
            }

        if(nickName.length()==0){
            txtNickName.setError("請輸入䁥稱");
        }



        //post data

            //寫入設定檔
            Utility.putKeyValue(this,"IS_LOGIN","NO");
            Utility.putKeyValue(this,"EMAIL",email);
            Utility.putKeyValue(this,"ENC_PWD",encPwd);
            Utility.putKeyValue(this,"NCIK_NAME",nickName);
            Utility.putKeyValue(this,"NEW_PWD",newEncPwd);


            String url =Utility.getHostUrl(getApplicationContext(),"register");
            new RegisterTask().execute(url,email,encPwd,nickName,newEncPwd);
        }
        catch(Exception ex){
            Log.e(TAG,ex.getMessage());
        }
    }

        /*
    public void GetRendom(View view){


        EditText txtRandomLength =(EditText)findViewById(R.id.txtRandomLength);
        TextView txtRandom =(TextView)findViewById(R.id.txtRandom);
        txtRandom.setText(Utility.getRandom(Integer.valueOf(txtRandomLength.getText().toString())));


    }
   */
        /*
    public void DecryPwd(View view){


        EditText txtEncryWord =(EditText)findViewById(R.id.txtEncryWord);
        TextView tv =(TextView)findViewById(R.id.txtDecryWord);

        try{
            String s = Des3.decode(txtEncryWord.getText().toString());
            tv.setText(s);
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }
*/

    //inner class
    private class RegisterTask extends AsyncTask<String, Void,String>{

        @Override
        protected String doInBackground(String... params) {

            String text = null;

            try {

                DefaultHttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(params[0]);

                JSONObject jobj = new JSONObject();
                jobj.put("email",params[1]);
                jobj.put("pwd",params[2]);
                jobj.put("nickname",params[3]);
                jobj.put("newpwd",params[4]);

                StringEntity entity = new StringEntity(jobj.toString(), HTTP.UTF_8);
                post.setEntity(entity);
                post.setHeader("Accept", "application/json");
                post.setHeader("content-type", "application/json");

                HttpResponse response = (HttpResponse)client.execute(post);
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(response);
               // int code = response.getStatusLine().getStatusCode();
                text = body;


            } catch (Exception ex) {
                Log.e(TAG,ex.getMessage());
            }


            return text;
        }


        protected void onPostExecute(String results) {
            if (results!=null) {

                try{

                    JSONObject jobj =new JSONObject(results);
                   // Toast.makeText(getApplicationContext(),results + ",result=" + jobj.getString("result"), Toast.LENGTH_SHORT).show();

                    String result =jobj.getString("result");
                    String msg = jobj.getString("msg");
                    Toast.makeText(getApplicationContext(),  msg, Toast.LENGTH_SHORT).show();

                    //finish();

                    if(result.trim().equals("1")){


                        //Intent intent =new Intent(RegisterActivity.this,LoginActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);



                        setResult(RESULT_OK);
                        //startActivity(intent);

                        finish();
                    }

                }
                catch(Exception ex){
                    Log.e(TAG,ex.getMessage());
                }


            }


        }
    }

//inner class
private class AsyncUtility extends AsyncTask<String, Void, String>
{


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

           // String host = "http://api.oli365.com:8180/users/" + params[0];


             //String url =host + "?key=" + Utility.getKey();

            String url = Utility.getHostUrl(getApplicationContext(), params[0]);

            //String url =host + "?key=" + Utility.getKey();
            HttpGet httpGet = new HttpGet(url);


            HttpResponse response = httpClient.execute(httpGet, localContext);


            HttpEntity entity = response.getEntity();


            text = getASCIIContentFromEntity(entity);


        } catch (Exception ex) {
            Log.e(TAG,ex.getMessage());
        }


        return text;
    }


    protected void onPostExecute(String results) {
        if (results!=null) {
            //EditText et = (EditText)findViewById(R.id.my_edit);
            if(results=="1"){
                Toast.makeText(getApplicationContext(), "Email帳號已經存在", Toast.LENGTH_SHORT).show();
            }

            // et.setText(results);
        }


        // Button b = (Button)findViewById(R.id.btnGetApi);


        //b.setClickable(true);
    }



}


}
