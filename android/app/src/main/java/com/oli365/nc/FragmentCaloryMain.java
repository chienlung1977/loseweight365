package com.oli365.nc;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.oli365.nc.controller.CaloryDAO;
import com.oli365.nc.controller.Utility;
import com.oli365.nc.model.CaloryModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by alvinlin on 2016/8/1.
 */
public class FragmentCaloryMain extends Fragment {

    private static final String TAG= FragmentCaloryMain.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_calory_main, container,false);
        ArrayList<CaloryModel> items =new ArrayList<CaloryModel>();

        CaloryDAO cd =new CaloryDAO(getActivity());
        List<CaloryModel> cc = cd.getLatest30();
        CaloryModel mc =null;

        CaloryModel c =null;
        for(long i =0;i>-30;i--){
            String date = Utility.getDate((int)i);
            boolean status = false;
            for(int j=0;j<cc.size();j++){
                mc =cc.get(j);
                String mcdate =mc.getCreatedate();
                //Log.i(TAG,"mc.getCreatedate()=" + mc.getCreatedate());
                if(date.equals(mcdate)){
                    c=new CaloryModel(i,date,mc.getBreakfast(),mc.getLunch(),mc.getDinner(),mc.getDessert(),mc.getSport(),mc.getMemo());
                    items.add(c);
                    status=true;
                }
            }

            if(status==false){
                c =new CaloryModel(i,date,0,0,0,0,0,"");
                items.add(c);
            }



        }



        CaloryAdapter itemAdapter =new CaloryAdapter(view.getContext(), items);
        ListView listView = (ListView) view.findViewById(R.id.caloryList);

        //短按時顯示編輯畫面
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //按下時顯示該日期的資料
                TextView txtDate = (TextView)view.findViewById(R.id.txtDate);
               // Log.i(TAG,"date=" + txtDate.getText() + ",id=" + String.valueOf(id));
               // Toast.makeText(getContext(),"date=" + txtDate.getText() + ",id=" + String.valueOf(id) , Toast.LENGTH_SHORT).show();
                showCaloryView(view,txtDate.getText().toString());
            }


        });

        //長按時跳出是否刪除的詢問對話框
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

               // _id = id ;
                AlertDialog diaBox = AskOption(l);
                diaBox.show();

                return true;
                //return false;
            }
        });


        listView.setAdapter(itemAdapter);

        return view;
       // return inflater.inflate(R.layout.fragment_calory_main, container, false);
    }


    private AlertDialog AskOption(long id)
    {

        final long _id =id;

        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                //.setTitle("Delete")
                .setMessage("確定要刪除？")
                // .setIcon(R.drawable.delete)

                .setPositiveButton("刪除", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        //上傳刪除記錄，接著才刪除本地端內容



                        /*
                        NetworkDAO nd =new NetworkDAO(ActivityRecordMain.this);
                        RecordDAO rd =new RecordDAO(ActivityRecordMain.this);
                        RecordModel rm = rd.get(_id);
                        if(rm!=null){
                            //開始上傳刪除記錄
                            rm.UploadStatus= Config.UPLOAD_STATUS.DELETE;
                            rm.Status="0";
                            try{
                                nd.uploadJson("user/record/update", rm, new NetworkDAO.downloadJosn() {
                                    @Override
                                    public void onCompleted(NetworkDAO.RETURN_CODE status, Exception ex, String result) {
                                        if(status== NetworkDAO.RETURN_CODE.SUCCESS){
                                            //真正刪除本地資料
                                            RecordDAO rd =new RecordDAO(ActivityRecordMain.this);
                                            rd.delete(_id);
                                            Utility.showMessage(ActivityRecordMain.this,"刪除成功");
                                        }
                                        else{
                                            Utility.showMessage(ActivityRecordMain.this,"上傳刪除失敗："+ result);
                                        }
                                    }
                                });
                            }
                            catch (Exception ex){
                                Log.d(TAG,ex.toString());
                                LogDAO.LogError(ActivityRecordMain.this,TAG,ex);
                            }


                        }


                        dialog.dismiss();
                        bindData();
*/
                        /*
                        if (dao.deleteR(_id)) {
                            msg ="刪除成功";

                            Gson gson = new Gson();
                            String jstr = gson.toJson(rm);
                            try{
                                NetworkDAO network =new NetworkDAO(ActivityRecordMain.this);
                                //network.uploadJson("/users/record/json",jstr);
                            }
                           catch (Exception ex){
                               LogDAO.LogError(ActivityRecordMain.this,TAG,ex);
                           }
                            //startUploadService();
                        }
                        else{
                            msg ="刪除失敗";
                        }
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        bindData();
                        */
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

    public void showCaloryView(View v,String date){

        FragmentManager fragmentMgr = getFragmentManager();
        FragmentCaloryNew fgClaoryNew =new FragmentCaloryNew();
        FragmentTransaction fragmentTrans = fragmentMgr.beginTransaction();
        // fragmentTrans.add(R.id.fg_calory_main,fgClaoryNew);
        Bundle b =new Bundle();
        //傳到calorynew頁面，才知道要抓哪一天的資料
        b.putString("date",date);
        fgClaoryNew.setArguments(b);

        /*
        fragmentTrans.addToBackStack(null);
        fragmentTrans.replace(R.id.fg_calory_content, fgClaoryNew);

        fragmentTrans.commit();

*/
        //TextView txtDate = (TextView) v.findViewById(R.id.txtDate);



    }

}
