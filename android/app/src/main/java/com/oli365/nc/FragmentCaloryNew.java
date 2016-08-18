package com.oli365.nc;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCaloryNew.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCaloryNew#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCaloryNew extends android.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = FragmentCaloryNew.class.getName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


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
    // TODO: Rename and change types and number of parameters
    public static FragmentCaloryNew newInstance(String param1, String param2) {
        FragmentCaloryNew fragment = new FragmentCaloryNew();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Log.i(TAG," FragmentCaloryNew onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG," FragmentCaloryNew onCreateView");
        final View view =inflater.inflate(R.layout.fragment_calory_new, container, false);

        //早餐按鈕
        Button btnbreakfast =(Button)view.findViewById(R.id.calory_btn_breakfast_add);
        btnbreakfast.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG,"breakfast click");
                final AlertDialog alertDialog = getAlertDialog(v,getResources().getString(R.string.calory_breakfast),"早餐卡路里合計","b");
                alertDialog.show();
            }
        });

        //中餐
        Button btnlunch =(Button)view.findViewById(R.id.calory_btn_lunch_add);
        btnlunch.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Log.i(TAG,"breakfast click");
                final AlertDialog alertDialog = getAlertDialog(v,getResources().getString(R.string.calory_lunch),"","l");
                alertDialog.show();
            }
        });

        //晚餐
        Button btndinner =(Button)view.findViewById(R.id.calory_btn_dinner_add);
        btndinner.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Log.i(TAG,"breakfast click");
                final AlertDialog alertDialog = getAlertDialog(v,getResources().getString(R.string.calory_dinner),"","d");
                alertDialog.show();
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

        //取消按鈕
        Button btncancel = (Button)view.findViewById(R.id.btnCancel);
        btncancel.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
               // Toast.makeText(view.getContext(),"cancel click" , Toast.LENGTH_SHORT).show();
               // Log.d(TAG," button cancel click");
                FragmentManager fragmentMgr = getFragmentManager();
                CaloryMainFragment fgClaoryMain =new CaloryMainFragment();
                FragmentTransaction fragmentTrans = fragmentMgr.beginTransaction();
                // fragmentTrans.add(R.id.fg_calory_main,fgClaoryNew);
                fragmentTrans.addToBackStack(null);
                fragmentTrans.replace(R.id.fg_calory_content, fgClaoryMain);

                fragmentTrans.commit();

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
                EditText calory_et_memo =(EditText)view.findViewById(R.id.calory_et_memo);

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



                //將資料存入後導向回總卡路里頁面
                Calory c =new Calory();
                c.setCreatedate(Utility.getToday());
                c.setBreakfast(mbreakfast);
                c.setLunch(mlunch);
                c.setDinner(mdinner);
                c.setDessert(mdessert);
                c.setSport(msport);
                c.setMemo(mmemo);

                CaloryDAO cd =new CaloryDAO(view.getContext());
                cd.insert(c);



                FragmentManager fragmentMgr = getFragmentManager();
                CaloryMainFragment fgClaoryMain =new CaloryMainFragment();
                FragmentTransaction fragmentTrans = fragmentMgr.beginTransaction();
                fragmentTrans.addToBackStack(null);
                fragmentTrans.replace(R.id.fg_calory_content, fgClaoryMain);

                fragmentTrans.commit();

            }
        });


        Log.i(TAG,btncancel.getText().toString());

        //載入卡路里資料
        bindData();

        return view;
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

                    //todo 要自訂事件回傳到父層

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


                //重新載入記錄
                bindData();
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



    //帶入卡路里記錄
    private void bindData(){


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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
