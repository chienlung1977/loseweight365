package com.oli365.nc;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oli365.nc.controller.NetworkDAO;
import com.oli365.nc.controller.UserDAO;
import com.oli365.nc.controller.UserDataDAO;
import com.oli365.nc.controller.Utility;
import com.oli365.nc.model.UserDataModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class ActivityLogin extends AppCompatActivity implements LoaderCallbacks<Cursor> {


    private static final String TAG = ActivityLogin.class.getName();
    private static final int REGISTER_ACCOUNT=103;
    private static final int REQUEST_READ_CONTACTS = 0;

    private UserLoginTask mAuthTask = null;

    // UI references.
    //private AutoCompleteTextView mEmailView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private android.widget.CheckBox mRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmailView = (EditText) findViewById(R.id.txtEmail);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.txtPassword);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        initData();
    }




    private void initData(){

        try{


            mRemember = (android.widget.CheckBox)findViewById(R.id.chkIsRemember);
            if(UserDAO.getUserIsRemember(this)){
                mRemember.setChecked(true);
                if(!UserDAO.getUserEmail(this).equals("")){
                    mEmailView.setText(UserDAO.getUserEmail(this));
                }

            }
            else{
                mRemember.setChecked(false);
            }

        }
        catch(Exception ex){

        }

    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    //登入檢查
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();

        try{
            //加密後密碼
            String pwd =mPasswordView.getText().toString();
            String encpassword = EncDes3.encode(pwd);

            Log.i(TAG,"password =" + pwd + ",encode password=" + encpassword);

            boolean cancel = false;
            View focusView = null;

            if (!TextUtils.isEmpty(pwd) && !isPasswordValid(pwd)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }

            if (TextUtils.isEmpty(email)) {
                mEmailView.setError(getString(R.string.error_field_required));
                focusView = mEmailView;
                cancel = true;
            } else if (!Utility.isEmail(email)) {
                Log.i(TAG,"IS_EMAIL=" + Utility.isEmail(email));
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                cancel = true;
            }

            if (cancel) {
                focusView.requestFocus();
            } else {

                if(mRemember !=null){
                    if(mRemember.isChecked()==true){
                        UserDAO.setUserIslogin(this,true);
                    }
                    else{
                        UserDAO.setUserIslogin(this,false);
                    }
                }


                //開始發送查詢
                showProgress(true);
               final NetworkDAO nd =new NetworkDAO(this);
              //  UserDataDAO udd =new UserDataDAO(this);
                //取得本機的使用者資料
              //  UserDataModel udm = udd.getUser(email,encpassword);

                //UserDAO.initLoginData(ActivityLogin.this,udm);

                //一律檢查線上資料庫以確定資料同步
                /*
                JsonObject jobj =new JsonObject();
                jobj.addProperty("email",email);
                jobj.addProperty("pwd",encpassword);
                nd.uploadJson("user/getuser", jobj, new NetworkDAO.downloadJosn() {
                    @Override
                    public void onCompleted(NetworkDAO.RETURN_CODE status, Exception ex, String result) {
                        //android.os.Debug.waitForDebugger();
                        Log.d(TAG,"Return code=" + status.toString());
                        if(status== NetworkDAO.RETURN_CODE.SUCCESS){
                            try{
                                JSONArray obj = new JSONArray(result);
                                Log.d(TAG,"json array " + obj.toString());
                            }
                            catch (JSONException je){
                                Log.d(TAG,"轉換失敗:" + je.toString());
                            }
                            UserDataModel udm1=new UserDataModel();
                        }
                    }
                });



               final long id = udm.Id;
               udm.LastLogintime=Utility.getToday();
               udm.Status=Config.ACCOUNT_STATUS.DISABLED;



                Log.d(TAG,"UserDataModel boject=" + udm.toString());
                */
                final UserDataDAO udd =new UserDataDAO(ActivityLogin.this);
                final UserDataModel udm=new UserDataModel();
                udm.Email=email;
                udm.Password=encpassword;
                udm.LastLogin=Utility.getToday();  //最後登入時間

                    //上傳登入狀態
                    nd.uploadJson("user/login", udm, new NetworkDAO.downloadJosn() {
                        @Override
                        public void onCompleted(NetworkDAO.RETURN_CODE status, Exception ex, String result) {

                            Log.d(TAG,"nodejs return code=" + status.toString() + ",result=" + result);
                            //如果驗證成功就初始化關鍵資料
                            if(status== NetworkDAO.RETURN_CODE.SUCCESS){

                                //主機回傳資料
                                Log.d(TAG,result);

                                String email="";
                                String userid ="";
                                String nickname ="";
                               // String level ="";
                                String updatedate="";

                                try{
                                    JSONArray jsonarr = new JSONArray(result);
                                    JSONObject jo = jsonarr.getJSONObject(0);

                                    email = jo.getString("EMAIL");
                                    userid=jo.getString("USER_ID");
                                    nickname = jo.getString("NICK_NAME");
                                    updatedate =jo.getString("UPDATE_DATE");

                                    //初始化基本關鍵資料
                                    UserDAO.initLoginData(ActivityLogin.this,email,userid,nickname,updatedate);

                                    UserDAO.setUserIsRemember(ActivityLogin.this,mRemember.isChecked());
                                    showProgress(false);
                                    Intent intent =new Intent(ActivityLogin.this,ActivityMain.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    startActivity(intent);


                                }
                                catch (Exception e){

                                    Log.d(TAG,e.toString());
                                }



                            }
                            else{
                                showProgress(false);
                                try{
                                    JSONObject jo = new JSONObject(result);
                                    String msg = jo.getString("MSG");
                                    Utility.showMessage(ActivityLogin.this,"登入失敗:" + msg);
                                    return;
                                }
                                catch(Exception e){
                                    Utility.showMessage(ActivityLogin.this,"JSON轉換失敗" );
                                    return ;
                                }


                            }
                        }
                    });





                //String url =Utility.getHostUrl(getApplicationContext(),"users/login");
                //mAuthTask = new UserLoginTask(email, password,url,newpassword);
                //mAuthTask.get(2000, TimeUnit.MILLISECONDS);
                //mAuthTask.execute((Void)null);
            }
        }
        catch (Exception ex){
            Log.e(TAG,"Err:" + ex.toString());
        }

    }

    //region 線上函式




    //endregion


    private boolean isEmailValid(String email) {

        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(ActivityLogin.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

      //  mEmailView.setAdapter(adapter);
    }

    public void checkLogin(View view){




        EditText txtEmail = (EditText)findViewById(R.id.txtEmail);
        EditText txtPassword =(EditText)findViewById(R.id.txtPassword);
        if(txtEmail.getText().length()<5){
            Toast.makeText(this,"請輸入您的Email帳號" , Toast.LENGTH_SHORT).show();
            txtEmail.requestFocus();
            return ;
        }

        if(txtPassword.getText().length()==0){
            Toast.makeText(this,"請輸入您的登入密碼" , Toast.LENGTH_SHORT).show();
            txtPassword.requestFocus();
            return ;
        }

        try{


        //開始檢核帳號密碼
        //String random = Utility.getRandom(5);
        //String key = Utility.getKey(random);
        String account = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String encPassword = Des3.encode(password);

            //開始登入
           // startLogin(account,encPassword);

        }
        catch (Exception ex){
            Toast.makeText(this,ex.getMessage() , Toast.LENGTH_SHORT).show();
        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Toast.makeText(this,"isLogin=" + Utility.getKeyValue(this,"IS_LOGIN") , Toast.LENGTH_SHORT).show();
        //回傳情況
        if(requestCode==REGISTER_ACCOUNT && resultCode==RESULT_OK){

            //自動幫忙登入

            //註冊完直接登入


            //Toast.makeText(this,"isLogin=" + Utility.getKeyValue(this,"IS_LOGIN") , Toast.LENGTH_SHORT).show();

            //Utility.putKeyValue(this,"IS_LOGIN","YES");
            UserDAO.setUserIslogin(this,true);

            Intent intent =new Intent(this,ActivityMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }

    }

    public void showRegister(View view){

        //先以網站方式顯示以保持同步
       // ActionDAO adao =new ActionDAO(this);
       // adao.openWeb();

        Intent intent =new Intent(this,ActivityRegister.class);
        startActivityForResult(intent,REGISTER_ACCOUNT);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mUrl;
        private String mBody;
        private final String mNewPassword;

        UserLoginTask(String email, String password,String url,String newpassword) {
            mEmail = email;
            mPassword = password;
            mUrl =url;
            mNewPassword= newpassword;
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            /*
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }



            */

            String text = null;

            try {

                DefaultHttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(mUrl);

                JSONObject jobj = new JSONObject();
                jobj.put("email",mEmail);
                jobj.put("pwd",mPassword);
                jobj.put("newpwd",mNewPassword);


                StringEntity entity = new StringEntity(jobj.toString(), HTTP.UTF_8);
                post.setEntity(entity);
                post.setHeader("Accept", "application/json");
                post.setHeader("content-type", "application/json");

                HttpResponse response = (HttpResponse)client.execute(post);
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(response);
                // int code = response.getStatusLine().getStatusCode();
                mBody = body;


            } catch (Exception ex) {
               // Log.e(TAG,ex.getMessage());
                return false;
            }


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

                try{

                    JSONObject jobj =new JSONObject(mBody);


                    String result =jobj.getString("result");
                    String msg = jobj.getString("msg");
                    String uid  = jobj.getString("uid");


                   // Toast.makeText(getApplicationContext(), ",mBody=" + mBody, Toast.LENGTH_SHORT).show();

                    //登入成功
                    if(result.trim().equals("1")){


                        //Intent intent =new Intent(RegisterActivity.this,LoginActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        //記錄已驗證狀態
                        UserDAO.processLogin(ActivityLogin.this,true,uid);

                        Intent intent =new Intent(ActivityLogin.this,ActivityMain.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        //setResult(RESULT_OK);
                        //startActivity(intent);

                        //finish();
                    }
                    else{
                        UserDAO.processLogin(ActivityLogin.this,false,uid);
                        Toast.makeText(getApplicationContext(),  msg, Toast.LENGTH_SHORT).show();
                    }

                }
                catch(Exception ex){
                    //Log.e(TAG,ex.getMessage());
                }


            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

