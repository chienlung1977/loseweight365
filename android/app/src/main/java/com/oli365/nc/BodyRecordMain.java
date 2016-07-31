package com.oli365.nc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;

/**
 * Created by alvinlin on 2015/10/28.
 */
public class BodyRecordMain extends AppCompatActivity {


    private RecordCursorAdapter itemAdapter;
    private long  _id ;
    private final int PICK_IMAGE = 101;
    private final int ADD_RECORD=1;
    //private final int TAKE_PICTURE=102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body_record_main);

        /*
        RecordDAO item =new RecordDAO(getApplicationContext());
        if(item.getCount()==0){

            item.sample();
        }
        List<Record> items = item.getAll();
        ArrayAdapter<Record> itemAdapter = new ArrayAdapter<Record>(this,android.R.layout.simple_expandable_list_item_1 ,items);


        ListView listview =(ListView)findViewById(R.id.recordList);
        listview.setAdapter(itemAdapter);
        */

        // View header = getLayoutInflater().inflate(R.layout.record_header,null);

        // mList.addHeaderView(header);

        bindData();




    }

    /*
    private List<Record> buildData(){

        //DbHelper dbHelper =new DbHelper(BodyRecordMain.this,)

    }

    */

    private void bindData(TIME_PERIOD period){

        String queryString ="";

        switch (period){
            case MORNING:
                queryString="SELECT * FROM RECORD  WHERE cast(strftime('%H',CREATE_TIME) as integer)>=6 AND cast(strftime('%H',CREATE_TIME) as integer)<12 ORDER BY CREATE_TIME DESC";
                break;
            case AFTERNOON:
                queryString="SELECT * FROM RECORD  WHERE cast(strftime('%H',CREATE_TIME) as integer)>=12 AND cast(strftime('%H',CREATE_TIME) as integer)<18 ORDER BY CREATE_TIME DESC";
                break;
            case NIEGHT:
                queryString="SELECT * FROM RECORD  WHERE cast(strftime('%H',CREATE_TIME) as integer)>=18 AND cast(strftime('%H',CREATE_TIME) as integer) <=23 ORDER BY CREATE_TIME DESC";
                break;
            case DAWN:
                queryString="SELECT * FROM RECORD  WHERE cast(strftime('%H',CREATE_TIME) as integer)>=0 AND cast(strftime('%H',CREATE_TIME) as integer)<6 ORDER BY CREATE_TIME DESC";
                break;
           default:
               queryString="SELECT * FROM RECORD ORDER BY CREATE_TIME DESC";
               break;
        }

        SQLiteDatabase db =DbHelper.getDatabase(this);
        Cursor todoCursor =db.rawQuery(queryString, null);

        ListView mList =(ListView)findViewById(R.id.recordList);

        // View header = getLayoutInflater().inflate(R.layout.record_header,null);

        // mList.addHeaderView(header);

        itemAdapter =new RecordCursorAdapter(this,todoCursor);
        mList.setAdapter(itemAdapter);



        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                _id = id ;

                AlertDialog diaBox = AskOption();
                diaBox.show();

                //Toast.makeText(getApplicationContext(), "delete position=" + position + ",id=" + id, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //todo 帶到修改資料頁面
/*
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "選擇圖片"), PICK_IMAGE);
*/

            }
        });

        //mList.setOnLongClickListener();


        //simpleAdapter=new SimpleAdapter(this,,R.layout.body_record_main,)


        //String[] mNames =buildData(30,"Name");

        /*
        ListAdapter mAdapter =new ArrayAdapter<String>(this,R.layout.body_record_main,R.id.fr ,mNames);
        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        */
    }

    private enum TIME_PERIOD{
        ALL,MORNING,AFTERNOON,NIEGHT,DAWN
    }

    private void bindData(){
        bindData(TIME_PERIOD.ALL);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        //Toast.makeText(BodyRecordMain.this,"aaaaaaaaaaaa", Toast.LENGTH_LONG).show();

        switch (item.getItemId()){
            case R.id.btnAdd:
                Intent intent =new Intent(this,BodyRecordAdd.class);
                //startActivity(intent);
                startActivityForResult(intent, ADD_RECORD);
                return true;
            case R.id.btnView_1:
                bindData(TIME_PERIOD.MORNING);
                return true;
            case R.id.btnView_2:
                bindData(TIME_PERIOD.AFTERNOON);
                return true;
            case R.id.btnView_3:
                bindData(TIME_PERIOD.NIEGHT);
                return true;
            case R.id.btnView_4:
                bindData(TIME_PERIOD.DAWN);
                return true;
            case R.id.btnView_All:
                bindData(TIME_PERIOD.ALL);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            //
            if(requestCode == ADD_RECORD &&  resultCode==RESULT_OK){
                bindData();
            }

            if (requestCode == PICK_IMAGE && data != null) {

                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap
            }

            //String result = "requestCode=" + String.valueOf(requestCode) + "resultCode" + String.valueOf(resultCode);
            //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
        catch(Exception ex){

        }
        /*
        switch(resultCode){
            case RESULT_OK:
                //Toast.makeText(this,"ok",Toast.LENGTH_SHORT).show();
                bindData();
                break;
            case RESULT_CANCELED:
                //Toast.makeText(this,"cancel",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
        }


        bindData();
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.body_record, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }




    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                //.setTitle("Delete")
                .setMessage("確定要刪除？")
               // .setIcon(R.drawable.delete)

                .setPositiveButton("刪除", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        RecordDAO dao =new RecordDAO(getApplicationContext());
                        String msg ="";
                        if (dao.delete(_id)) {
                            msg ="刪除成功";
                        }
                        else{
                            msg ="刪除失敗";
                        }
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        bindData();
                    }

                })



                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(),"choice cancel",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }


}
