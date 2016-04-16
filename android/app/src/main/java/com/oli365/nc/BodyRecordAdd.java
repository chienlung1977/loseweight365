package com.oli365.nc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


// TODO: 2016/4/4 v1.2須加入日期並可選擇先前日期做新增 
public class BodyRecordAdd extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_record_add);
    }



    public void closeView(View view){
        Intent intent = new Intent();
        //Bundle bundle =new Bundle();
        //bundle.putString("b", "b");
        //intent.putExtra("b","");

        setResult(RESULT_CANCELED,intent);
        finish();
        //Toast.makeText(this, "cancel click", Toast.LENGTH_LONG).show();
    }

    public void saveData(View view){



       // Toast.makeText(this, "save click", Toast.LENGTH_LONG).show();
       // String currentDateTime = DateFormat.getDateTimeInstance().format((new Date()));
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        EditText txtWeight =(EditText) findViewById(R.id.txtWeight);
        EditText txtFatRate = (EditText)findViewById(R.id.txtFatRate);

        //檢查輸入的值是否為空
        if(txtWeight.getText().toString().equals("")){
            Toast.makeText(this,"體重請勿空白" , Toast.LENGTH_SHORT).show();
            txtWeight.requestFocus();
            return ;
        }

        if(txtFatRate.getText().toString().equals("")){
            Toast.makeText(this,"體脂肪請勿空白" , Toast.LENGTH_SHORT).show();
            txtFatRate.requestFocus();
            return ;
        }

        Record record=new Record(0,currentDateTime,Double.valueOf(txtWeight.getText().toString()),Double.valueOf(txtFatRate.getText().toString()));

        RecordDAO dao =new RecordDAO(this);
        dao.insert(record);

        Toast.makeText(this,"新增成功！" , Toast.LENGTH_LONG).show();

        Intent intent = new Intent();
        //Bundle bundle =new Bundle();
        //bundle.putString("b", "b");
        //intent.putExtra("b","");

        setResult(RESULT_OK,intent);
        finish();
    }

}
