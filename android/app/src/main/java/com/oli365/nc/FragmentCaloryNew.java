package com.oli365.nc;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import com.oli365.nc.controller.CaloryDAO;
import com.oli365.nc.controller.DebugDAO;
import com.oli365.nc.controller.FoodDAO;
import com.oli365.nc.controller.LogDAO;
import com.oli365.nc.controller.UserDAO;
import com.oli365.nc.controller.Utility;
import com.oli365.nc.model.CaloryModel;
import com.oli365.nc.model.FoodModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCaloryNew.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCaloryNew#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCaloryNew extends android.app.Fragment {


    private static final String TAG = FragmentCaloryNew.class.getName();

    private String date ;


    private OnFragmentInteractionListener mListener;

    public FragmentCaloryNew() {
        // Required empty public constructor
        Log.i(TAG," FragmentCaloryNew constructor");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCaloryNew.
     */

    public static FragmentCaloryNew newInstance(String param1, String param2) {
        FragmentCaloryNew fragment = new FragmentCaloryNew();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view =inflater.inflate(R.layout.fragment_calory_new, container, false);

        //早餐按鈕
        Button btnbreakfast =(Button)view.findViewById(R.id.calory_btn_breakfast_add);
        btnbreakfast.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Log.i(TAG,"breakfast click");
                //final AlertDialog alertDialog = getAlertDialog(v,getResources().getString(R.string.calory_breakfast),"早餐卡路里合計","b");
                //alertDialog.show();
                //傳遞食物類別參數
                change2FoodList(FoodModel.MAIN_FOOD_TYPE.BREAKFAST,getString(R.string.uid_food_type_breakfast));



            }
        });

        //中餐
        Button btnlunch =(Button)view.findViewById(R.id.calory_btn_lunch_add);
        btnlunch.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Log.i(TAG,"breakfast click");
                //final AlertDialog alertDialog = getAlertDialog(v,getResources().getString(R.string.calory_lunch),"","l");
                //alertDialog.show();
                change2FoodList(FoodModel.MAIN_FOOD_TYPE.LUNCH,getString(R.string.uid_food_type_lunch));

            }
        });

        //晚餐
        Button btndinner =(Button)view.findViewById(R.id.calory_btn_dinner_add);
        btndinner.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Log.i(TAG,"breakfast click");
                //final AlertDialog alertDialog = getAlertDialog(v,getResources().getString(R.string.calory_dinner),"","d");
                //alertDialog.show();
                change2FoodList(FoodModel.MAIN_FOOD_TYPE.DINNER,getString(R.string.uid_food_type_dinner));

            }
        });


        //點心
        Button btnrefreshment =(Button)view.findViewById(R.id.calory_btn_refreshment_add);
        btnrefreshment.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Log.i(TAG,"breakfast click");
                final AlertDialog alertDialog = getAlertDialog(v,getResources().getString(R.string.calory_refreshment),"","r");
                alertDialog.show();


            }
        });

        //運動量
        Button btnsport =(Button)view.findViewById(R.id.calory_btn_sport_add);
        btnsport.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Log.i(TAG,"breakfast click");
                final AlertDialog alertDialog = getAlertDialog(v,getResources().getString(R.string.calory_sport),"","s");
                alertDialog.show();

            }
        });





        if(getArguments() != null){
            //取得傳入日期
            date = getArguments().getString("date");
            Log.d(TAG,"Query date =" + date + ",date!=null" + String.valueOf(date!=null) + ",date!=empty" + String.valueOf(date!=""));

            if(date!=null){
                Log.d(TAG,"date ！= null");
                //載入卡路里資料
                CaloryDAO cd =new CaloryDAO(view.getContext());
                //String formatDate =Utility.getFormatShortDate(date);
                CaloryModel c = cd.getByDate(date);

                Log.d(TAG,"CaloryModel==null :" + String.valueOf(c==null));

                    if(c!=null){
                    TextView calory_tv_breakfast = (TextView)view.findViewById(R.id.calory_tv_breakfast);
                    calory_tv_breakfast.setText(String.valueOf(c.getBreakfast()));
                    TextView calory_tv_lunch = (TextView)view.findViewById(R.id.calory_tv_lunch);
                    calory_tv_lunch.setText(String.valueOf(c.getLunch()));
                    TextView calory_tv_dinner = (TextView)view.findViewById(R.id.calory_tv_dinner);
                    calory_tv_dinner.setText(String.valueOf(c.getDinner()));
                    TextView calory_tv_refreshment = (TextView)view.findViewById(R.id.calory_tv_refreshment);
                    calory_tv_refreshment.setText(String.valueOf(c.getDessert()));
                    TextView calory_tv_sport = (TextView)view.findViewById(R.id.calory_tv_sport);
                    calory_tv_sport.setText(String.valueOf(c.getSport()));
                    TextView calory_et_memo = (EditText)view.findViewById(R.id.calory_et_memo);
                    calory_et_memo.setText(c.getMemo());


                }

                //卡路里計算
                TextView txtDailyCalory =(TextView)view.findViewById(R.id.txtDailyCalory);

                //每日可用卡路里
                UserDAO ud =new UserDAO(getActivity());
                txtDailyCalory.setText(ud.getDailyCalory());

                //計算剩餘卡路里
                reCountingRemainingCalory(view);
                /*
                int usingcalory =c.getBreakfast() + c.getLunch() + c.getDinner() + c.getDessert()-c.getSport();
                int remainingcalory=Integer.valueOf(ud.getDailyCalory()) - usingcalory;
                TextView txtRemainingCalory=(TextView)view.findViewById(R.id.txtRemainingCalory);
                txtRemainingCalory.setText(String.valueOf(remainingcalory));
*/
                Button btnRecountingCalory=(Button)view.findViewById(R.id.btnRecountingCalory);
                btnRecountingCalory.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View vv) {
                        reCountingRemainingCalory(view);
                    }
                });

            }
        }




        //新增食物卡路星
        Button btn_food_add =(Button)view.findViewById(R.id.btn_food_add);
        btn_food_add.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //final AlertDialog ad=getAddAlertDialog();
                //ad.show();
                //導到新增食物
                /*
                FragmentManager fm = getFragmentManager();
                FragmentFoodNew ffn  =new FragmentFoodNew();
                FragmentTransaction ft = fm.beginTransaction();
                // fragmentTrans.add(R.id.fg_calory_main,fgClaoryNew);

                ft.addToBackStack(null).replace(R.id.fg_calory_content,ffn).commit();

*/

            }
        });



        //取消按鈕
        Button btncancel = (Button)view.findViewById(R.id.btnCancel);
        btncancel.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
               // Toast.makeText(view.getContext(),"cancel click" , Toast.LENGTH_SHORT).show();
               // Log.d(TAG," button cancel click");
                /*
                FragmentManager fragmentMgr = getFragmentManager();
                FragmentCaloryMain fgClaoryMain =new FragmentCaloryMain();
                FragmentTransaction fragmentTrans = fragmentMgr.beginTransaction();
                // fragmentTrans.add(R.id.fg_calory_main,fgClaoryNew);
                fragmentTrans.addToBackStack(null);
                fragmentTrans.replace(R.id.fg_calory_content, fgClaoryMain);

                fragmentTrans.commit();

*/
            }
        });



        //確定按鈕
        Button btnOK = (Button)view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                TextView calory_tv_breakfast = (TextView)view.findViewById(R.id.calory_tv_breakfast);
                TextView calory_tv_lunch = (TextView)view.findViewById(R.id.calory_tv_lunch);
                TextView calory_tv_dinner = (TextView)view.findViewById(R.id.calory_tv_dinner);
                TextView calory_tv_refreshment = (TextView)view.findViewById(R.id.calory_tv_refreshment);
                TextView calory_tv_sport = (TextView)view.findViewById(R.id.calory_tv_sport);
                TextView calory_et_memo =(EditText)view.findViewById(R.id.calory_et_memo);



                int mbreakfast=0;
                int mlunch=0;
                int mdinner=0;
                int mdessert=0;
                int msport=0;
                String mmemo=calory_et_memo.getText().toString();

                if(calory_tv_breakfast.getText().toString()!=""){
                    mbreakfast= Integer.valueOf(calory_tv_breakfast.getText().toString());
                }

                if(calory_tv_lunch.getText().toString()!=""){
                   mlunch= Integer.valueOf(calory_tv_lunch.getText().toString());
                }

                if(calory_tv_dinner.getText().toString()!=""){
                  mdinner =  Integer.valueOf(calory_tv_dinner.getText().toString());
                }

                if(calory_tv_refreshment.getText().toString()!=""){
                    mdessert=Integer.valueOf(calory_tv_refreshment.getText().toString());
                }

                if(calory_tv_sport.getText().toString()!=""){
                    msport=Integer.valueOf(calory_tv_sport.getText().toString());
                }



                CaloryDAO cd =new CaloryDAO(getActivity());

                if(DebugDAO.IsDebugMode()==true){
                    cd.deleteAll();
                }
                //取得原資料
                CaloryModel c =cd.getByDate(date);
                if(c ==null){
                    c=new CaloryModel();
                    c.setId(0);
                }
                c.setCreatedate(date);
                c.setBreakfast(mbreakfast);
                c.setLunch(mlunch);
                c.setDinner(mdinner);
                c.setDessert(mdessert);
                c.setSport(msport);
                c.setMemo(mmemo);

                //將資料存入後導向回總卡路里頁面
                cd.update(c);

                /*
                FragmentManager fragmentMgr = getFragmentManager();
                FragmentCaloryMain fgClaoryMain =new FragmentCaloryMain();
                FragmentTransaction fragmentTrans = fragmentMgr.beginTransaction();
                fragmentTrans.addToBackStack(null);
                fragmentTrans.replace(R.id.fg_calory_content, fgClaoryMain);

                fragmentTrans.commit();

*/
            }
        });

        //常用食物表按鈕事件

        bindFoodAlertDialog(view,R.id.calory_tv_breakfast,R.id.btn_calory_private_breakfast);
        bindFoodAlertDialog(view,R.id.calory_tv_lunch,R.id.btn_calory_private_lunch);
        bindFoodAlertDialog(view,R.id.calory_tv_dinner,R.id.btn_calory_private_dinner);
        bindFoodAlertDialog(view,R.id.calory_tv_refreshment,R.id.btn_calory_private_refreshment);
        bindFoodAlertDialog(view,R.id.calory_tv_sport,R.id.btn_calory_private_sport);

        //檢查新增食物是否有回傳物件回來
        if(getArguments() != null){
           Log.d(TAG,"getArguments!=null");
            if(getArguments().getParcelable("FM")!=null){
                Log.d(TAG,"getArguments().getParcelable(\"FM\")!=null");
                FoodModel fm = getArguments().getParcelable("FM");
                Log.d(TAG,"fm return =" + fm.FoodName);
                //判斷是哪一個來源
                TextView calory_tv_breakfast = (TextView)view.findViewById(R.id.calory_tv_breakfast);
                calory_tv_breakfast.setText(fm.Calory);
            }
        }




        return view;
    }

    //重新計算剩餘卡路里
    private void reCountingRemainingCalory(View view){

        TextView calory_tv_breakfast = (TextView)view.findViewById(R.id.calory_tv_breakfast);
        TextView calory_tv_lunch = (TextView)view.findViewById(R.id.calory_tv_lunch);
        TextView calory_tv_dinner = (TextView)view.findViewById(R.id.calory_tv_dinner);
        TextView calory_tv_refreshment = (TextView)view.findViewById(R.id.calory_tv_refreshment);
        TextView calory_tv_sport = (TextView)view.findViewById(R.id.calory_tv_sport);

        //卡路里計算
        TextView txtDailyCalory =(TextView)view.findViewById(R.id.txtDailyCalory);

        //計算剩餘卡路里
        int usingcalory =0;
        if(calory_tv_breakfast.getText()!=""){
            usingcalory+=Integer.valueOf(calory_tv_breakfast.getText().toString());
        }
        if(calory_tv_lunch.getText()!=""){
            usingcalory+=Integer.valueOf(calory_tv_lunch.getText().toString());
        }
        if(calory_tv_dinner.getText()!=""){
            usingcalory+=Integer.valueOf(calory_tv_dinner.getText().toString());
        }
        if(calory_tv_refreshment.getText()!=""){
            usingcalory+=Integer.valueOf(calory_tv_refreshment.getText().toString());
        }
        if(calory_tv_sport.getText()!=""){
            usingcalory-=Integer.valueOf(calory_tv_sport.getText().toString());
        }


        int dailycalory =0;
        if(txtDailyCalory.getText()!=""){
            dailycalory=Integer.valueOf(txtDailyCalory.getText().toString());
        }

        int remainingcalory=dailycalory - usingcalory;
        TextView txtRemainingCalory=(TextView)view.findViewById(R.id.txtRemainingCalory);
        txtRemainingCalory.setText(String.valueOf(remainingcalory));

    }

    //顯下新增對話框
    private AlertDialog getAddAlertDialog(){

        AlertDialog.Builder ad= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ad.setTitle("新增食物卡路里");
        final View dialogView = inflater.inflate(R.layout.alert_food,null);

        ad.setNegativeButton("關閉" ,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                //不作任何動作
                //Utility.showMessage(ActivityRecordAdd.this,"關閉");
            }
        });

        //儲存資料
        ad.setPositiveButton("儲存",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {



                EditText txtalertfood = (EditText) dialogView.findViewById(R.id.txtalertfood);
                final String foodname = txtalertfood.getText().toString();
                //檢查不可為空白
                if (Utility.isEmpty(txtalertfood)) {
                    txtalertfood.setError("食物名稱請勿空白");
                    txtalertfood.requestFocus();
                    return;
                }

                EditText txtalertfoodcalory =(EditText)dialogView.findViewById(R.id.txtalertfoodcalory);
                final String foodcalory =txtalertfoodcalory.getText().toString();
                if (Utility.isEmpty(txtalertfoodcalory)) {
                    txtalertfoodcalory.setError("卡路里請勿空白");
                    txtalertfoodcalory.requestFocus();
                    return;
                }

                EditText txtalertfoodmemo =(EditText)dialogView.findViewById(R.id.txtalertfoodmemo);
                final String foodmemo = txtalertfoodmemo.getText().toString();

                //須先檢查是否有同名食物

                FoodDAO fd =new FoodDAO(getActivity());
                if(fd.hasFood(foodname)==true ){
                    Utility.showMessage(getActivity(),"已有同名的食物！不可新增");
                    return ;
                }

                FoodModel fm =new FoodModel();




                try{
                    fd.insert(fm);
                    Utility.showMessage(getActivity(),"儲存成功！");
                }
                catch (Exception ex){
                    LogDAO.LogError(getActivity(),TAG,ex);
                }


            }
        });

        ad.setView(dialogView);
        return ad.create();
    }



    private  AlertDialog getAlertDialog(View v,String title, String message,final String type){

        final View myview =v;
        //產生一個Builder物件
        AlertDialog.Builder builder = new AlertDialog.Builder(myview.getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View vv =inflater.inflate(R.layout.calory_new_dialog, null);

        builder.setView(vv);
        //設定Dialog的標題
        builder.setTitle(title);
        //設定Dialog的內容
       // builder.setMessage(message);
        //設定Positive按鈕資料
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //按下按鈕時顯示快顯

                EditText txtcalory_calory = (EditText)vv.findViewById(R.id.txtcalory_calory);
/*
                if(type=="b"){



                    TableRow irow =(TableRow) myview.getParent();
                    TextView calory_tv_breakfast = (TextView) irow.findViewById(R.id.calory_tv_breakfast);
                    calory_tv_breakfast.setText(txtcalory_calory.getText());
                    //calory_tv_breakfast.setText(txtcalory_calory.getText());
                    //Toast.makeText(myview.getContext(), "您按下OK按鈕" + txtcalory_calory.getText() + myview.getParent().toString(), Toast.LENGTH_LONG).show();
                }
                */
                TableRow irow =(TableRow) myview.getParent();
                switch(type){
                    case "b":
                        //早餐
                        TextView calory_tv_breakfast = (TextView) irow.findViewById(R.id.calory_tv_breakfast);
                        calory_tv_breakfast.setText(txtcalory_calory.getText());

                        break;
                    case "l":

                        //午餐
                        TextView calory_tv_lunch = (TextView) irow.findViewById(R.id.calory_tv_lunch);
                        calory_tv_lunch.setText(txtcalory_calory.getText());

                        break;
                    case "d":

                        //晚餐
                        TextView calory_tv_dinner = (TextView) irow.findViewById(R.id.calory_tv_dinner);
                        calory_tv_dinner.setText(txtcalory_calory.getText());

                        break;
                    case "r":

                        //點心
                        TextView calory_tv_refreshment = (TextView) irow.findViewById(R.id.calory_tv_refreshment);
                        calory_tv_refreshment.setText(txtcalory_calory.getText());

                        break;
                    case "s":

                        //運動量
                        TextView calory_tv_sport = (TextView) irow.findViewById(R.id.calory_tv_sport);
                        calory_tv_sport.setText(txtcalory_calory.getText());

                        break;
                }

                //重新計算剩餘卡路里
                //reCountingRemainingCalory(getActivity());
                //重新計算卡路里
                //reCountingRemainingCalory(vv);

            }
        });
        //設定Negative按鈕資料
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //按下按鈕時顯示快顯
                //Toast.makeText(myview.getContext(), "您按下Cancel按鈕", Toast.LENGTH_SHORT).show();
            }
        });


        //利用Builder物件建立AlertDialog
        return builder.create();
    }



    //帶入常用食物表
    private void bindFoodAlertDialog(View view,int resultviewResourceid ,int btnResourceid){

        Button mybtn =(Button)view.findViewById(btnResourceid);
        final TextView resultView =(TextView)view.findViewById(resultviewResourceid);

        if(mybtn!=null && resultView!=null){
            mybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FoodDAO fd =new FoodDAO(view.getContext());
                    final Cursor cr =fd.getCursorAll();
                    Log.d(TAG,"food count =" + String.valueOf(cr.getCount()));

                    AlertDialog.Builder selAlert = new AlertDialog.Builder(getActivity());
                    selAlert.setTitle("常用食物");
                    selAlert.setCursor(cr, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            if(cr!=null){
                                cr.moveToFirst();
                                cr.moveToPosition(which);   //指標移動到該row
                                Log.d(TAG,"指標移動到第"+ which + "筆");
                                //get column 3 是名稱
                                String s =cr.getString(4);
                                Log.d(TAG,"選單內容為"+ s );
                                //txtrecordmemo.setText(s);
                                resultView.setText(s);
                            }

                        }

                    },"FOOD_NAME");


                    selAlert.setPositiveButton("關閉",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });


                    selAlert.show();
                }
            });
        }

    }




    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }


    //region "common"

    private void change2FoodList(FoodModel.MAIN_FOOD_TYPE mainType, String typeuid){

        /*
        FragmentManager fragmentMgr = getFragmentManager();
        FragmentFoodList ffn =new FragmentFoodList();
        FragmentTransaction ft = fragmentMgr.beginTransaction();
        Bundle b =new Bundle();
        b.putString("DATE",date);
        b.putString("MAIN_TYPE",mainType.toString());
        b.putString("TYPE_UID",typeuid);
        ffn.setArguments(b);

        ft.addToBackStack(FragmentCaloryNew.class.getName());
        ft.replace(R.id.fg_calory_content, ffn);

        ft.commit();
        */
    }


    //end region



}
