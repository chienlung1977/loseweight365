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

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {


    private static final String TAG = "LoginActivity";
    private static final int REGISTER_ACCOUNT=103;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
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
        // Set up the login form.
        //mEmailView = (AutoCompleteTextView) findViewById(R.id.txtEmail);
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
            if(!Utility.getKeyValue(this,"USER_EMAIL").equals("")){
                mEmailView.setText(Utility.getKeyValue(this,"USER_EMAIL"));
            }

            mRemember = (android.widget.CheckBox)findViewById(R.id.chkIsRemember);
            if(Utility.getKeyValue(this,"USER_IS_REMEMBER").equals("1")){
                mRemember.setChecked(true);
                mPasswordView.setText(Des3.decode(Utility.getKeyValue(this,"USER_PWD")));
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


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();

        try{
            //加密後密碼
            String pwd =mPasswordView.getText().toString();
            String password = Des3.encode(pwd);
            //新加密密碼(讓網站用)
            String newpassword = EncDes3.encode(pwd);

            Log.i(TAG,"old password=" + password + ",new password=" + newpassword);

            boolean cancel = false;
            View focusView = null;

            // Check for a valid password, if the user entered one.
            if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }

            // Check for a valid email address.
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
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.

                String isRemember="0";
                if(mRemember !=null){
                    if(mRemember.isChecked()==true){
                        isRemember="1";
                    }
                }

                //送給主機前先記錄帳號和加密密碼，供下次自動登入使用
                Utility.putKeyValue(this,"USER_EMAIL",email);
                Utility.putKeyValue(this,"USER_PWD",password);
                Utility.putKeyValue(this,"USER_IS_REMEMBER",isRemember);
                Utility.putKeyValue(this,"USER_NEW_PWD",newpassword);
                //開始發送查詢
                showProgress(true);
                String url =Utility.getHostUrl(getApplicationContext(),"users/login");
                mAuthTask = new UserLoginTask(email, password,url,newpassword);
                //mAuthTask.get(2000, TimeUnit.MILLISECONDS);
                mAuthTask.execute((Void)null);
            }
        }
        catch (Exception ex){
            Log.e(TAG,"Err:" + ex.toString());
        }

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
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
                new ArrayAdapter<>(LoginActivity.this,
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


    private void startLogin(String email,String encPassword){

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

            Utility.putKeyValue(this,"IS_LOGIN","YES");


            Intent intent =new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }

    }

    public void showRegister(View view){

        //SharedPreferences pref =this.getApplicationContext().getSharedPreferences("nc.oli365.com",this.getApplicationContext().MODE_PRIVATE);
        //SharedPreferences.Editor editor=pref.edit();
       // editor.putString("TEST_LOGIN","YES2");
        //editor.commit();

       // Utility.putKeyValue(this,"TEST_LOGIN","YES3");


       // Utility.putKeyValue(this,"TEST_LOGIN","YES");
        //Toast.makeText(this,"TEST_LOGIN=" + this.getSharedPreferences("nc.oli365.com",MODE_PRIVATE).getString("TEST_LOGIN","000000") , Toast.LENGTH_SHORT).show();

        Intent intent =new Intent(this,RegisterActivity.class);
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
            // TODO: attempt authentication against a network service.

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

            // TODO: register the new account here.

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
                        Utility.processLogin(LoginActivity.this,true,uid);

                        Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        //setResult(RESULT_OK);
                        //startActivity(intent);

                        //finish();
                    }
                    else{
                        Utility.processLogin(LoginActivity.this,false,uid);
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

