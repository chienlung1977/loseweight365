package com.oli365.nc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void EncryPwd(View view){

        EditText txtWord = (EditText)findViewById(R.id.txtWord);
        EditText txtEncryWord = (EditText)findViewById(R.id.txtEncryWord);

        try{
            String s = Des3.encode(txtWord.getText().toString()) ;
            txtEncryWord.setText(s);
        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }




    }

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

}
