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

/**
 * Created by alvinlin on 2015/10/28.
 */
public class BodyRecordMain extends AppCompatActivity {


    private RecordCursorAdapter itemAdapter;
    private long  _id ;

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


    private void bindData(){


        SQLiteDatabase db =DbHelper.getDatabase(this);
        Cursor todoCursor =db.rawQuery("SELECT * FROM RECORD ORDER BY CREATE_TIME DESC", null);


        ListView mList =(ListView)findViewById(R.id.recordList);


       // View header = getLayoutInflater().inflate(R.layout.record_header,null);

       // mList.addHeaderView(header);


        itemAdapter =new RecordCursorAdapter(this,todoCursor);

        mList.setAdapter(itemAdapter);





        // TODO: 2016/4/4 補上修改和刪除的函式
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

                //Toast.makeText(getApplicationContext(), "edit position=" + position + ",id=" + id, Toast.LENGTH_SHORT).show();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        //Toast.makeText(BodyRecordMain.this,"aaaaaaaaaaaa", Toast.LENGTH_LONG).show();

        switch (item.getItemId()){
            case R.id.btnAdd:
                Intent intent =new Intent(this,BodyRecordAdd.class);
                //startActivity(intent);
                startActivityForResult(intent,1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
