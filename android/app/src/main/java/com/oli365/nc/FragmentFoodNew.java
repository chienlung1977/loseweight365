package com.oli365.nc;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oli365.nc.controller.FoodTypeDAO;
import com.oli365.nc.controller.UserDAO;
import com.oli365.nc.controller.Utility;
import com.oli365.nc.model.FoodModel;
import com.oli365.nc.model.FoodTypeModel;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFoodNew extends Fragment {

    private static final String TAG=FragmentFoodNew.class.getName();
    private String typeUid;
    private final int TAKE_PICTURE = 102;
    View view ;
    private String fullPath ;
    private String fileName;
    private String path;

    TextView tv_food_type;   //子類別
    TextView tv_main_type;  //主類別
    FoodModel.MAIN_FOOD_TYPE mainType;  //前頁傳入值
    String date;





    private ArrayList<FoodTypeModel> mSelectedItems;

    public FragmentFoodNew() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //前頁傳入值
            date =getArguments().getString("DATE");
            typeUid= getArguments().getString("TYPE_UID");
            mainType = FoodModel.MAIN_FOOD_TYPE.valueOf(getArguments().getString("MAIN_TYPE"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_food_new, container, false);

        tv_food_type = (TextView)view.findViewById(R.id.tv_food_type);
        tv_main_type =(TextView)view.findViewById(R.id.tv_main_type);
        String mainTypeName="";

        if(getArguments() != null){
            if(mSelectedItems==null){
                mSelectedItems=new ArrayList<FoodTypeModel>();
            }

            boolean isdefault = false;
            for(int i =0;i<mSelectedItems.size();i++){
                if(mSelectedItems.get(i).TypeUid.toUpperCase().equals(typeUid.toUpperCase())){
                    isdefault=true;
                }
            }

            FoodTypeDAO fd =new FoodTypeDAO(view.getContext());
            if(isdefault==false){
                mSelectedItems.add(fd.getItem(typeUid));
                //帶出預設類別名稱
                FoodTypeModel fm = fd.getItem(typeUid);
                if(fm!=null){
                    tv_food_type.setText(fm.TypeName);
                }


            }

            switch (mainType){
                case BREAKFAST:
                    mainTypeName="早餐";
                    break;
                case LUNCH:
                    mainTypeName="午餐";
                    break;
                case DINNER:
                    mainTypeName="晚餐";
                    break;
                case DESSERT:
                    mainTypeName="點心";
                    break;
                case SPORT:
                    mainTypeName="運動";
                    break;
            }
            tv_main_type.setText(mainTypeName);


        }

        //拍照按鈕
        Button btnTakePic =(Button)view.findViewById(R.id.btnTakePic);
        btnTakePic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                takePic(view);
            }
        });

        Button btnFoodType=(Button)view.findViewById(R.id.btnFoodType);
        btnFoodType.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showFoodTypeAlert();
            }
        });

        Button btnCancel =(Button)view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                go2Back();
            }
        });

        Button btnSave =(Button)view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SaveData();
            }
        });



        return view;
    }

    //顯示食物類別選取
    private void showFoodTypeAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
        alert.setTitle("選擇食物類別");

        FoodTypeDAO ftd =new FoodTypeDAO(view.getContext());
        //改為物件寫法
        final ArrayList<FoodTypeModel> al = ftd.getAll();

       // final Cursor cr = ftd.getList();
        if(mSelectedItems==null){
            mSelectedItems=new ArrayList<FoodTypeModel>();
        }

        int count = al.size();

        //此處必須處理有預設選取的項目
        String[] itemuid =new String[count];
        String[] itemname =new String[count];
        boolean[] checked=new boolean[count];
        /*
        String[] itemuid =new String[cr.getCount()];
        String[] itemname =new String[cr.getCount()];
        boolean[] checked=new boolean[cr.getCount()];
        cr.moveToFirst();
        */
        for(int i=0;i<count;i++){
            FoodTypeModel fm = al.get(i);
            itemuid[i]=fm.TypeUid;
            itemname[i]=fm.TypeName;
           // itemname[i]=String.valueOf(typeUid.toUpperCase().equals(itemuid[i].toUpperCase()));
            checked[i]=false;
//            if(itemuid[i].toUpperCase().equals(typeUid.toUpperCase())){
//                checked[i]=true;
//            }

            for(int j=0;j<mSelectedItems.size();j++){
                if(itemuid[i].toUpperCase().equals(mSelectedItems.get(j).TypeUid.toUpperCase())){
                    checked[i]=true;
                }
            }

        }

        alert.setMultiChoiceItems(itemname, checked,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            mSelectedItems.add(al.get(which));
                           // Log.d(TAG,"選取的資料=" + cr.getString(which));
                        } else if(!isChecked){

                            FoodTypeModel fm = al.get(which);
                            for(int i =0;i<mSelectedItems.size();i++){
                                //Log.d(TAG,fm.getTypeUid().toUpperCase() + "=" + mSelectedItems.get(i).getTypeUid().toUpperCase());
                                if(fm.TypeUid.toUpperCase().equals(mSelectedItems.get(i).TypeUid.toUpperCase())){
                                    mSelectedItems.remove(i);
                                    break;
                                }
                            }
                           /*
                            if (mSelectedItems.contains(al.get(which))) {
                               // mSelectedItems.remove(Integer.valueOf(which));
                                mSelectedItems.remove(al.get(which));
                            }
                            */
                        }
                    }
                });



        alert.setPositiveButton(getString(R.string.btnSubmit),new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {

                TextView tv_food_type = (TextView) view.findViewById(R.id.tv_food_type);
                String foodtypename ="";

                for(int i =0;i<mSelectedItems.size();i++){
                    FoodTypeModel fm = mSelectedItems.get(i);
                    if(foodtypename==""){
                        foodtypename= fm.TypeName;

                    }
                    else{
                        foodtypename+="," + fm.TypeName;
                    }


                }

                tv_food_type.setText(foodtypename);
               // mSelectedItems.clear();




            }
        });

        //關閉
        /*
        alert.setNegativeButton(getString(R.string.close),new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {

            }
        });
*/


        alert.show();

    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        //拍照
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            Bitmap photo = Utility.resizeBitmap(view.getContext(),uri);
            path = Utility.getImagePath(getActivity());
            fileName = Utility.SaveIamge(path,photo);

            fullPath = path + fileName;
            Log.d(TAG,"完整路徑FULLPATH=" + fullPath);
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView iv_food_pic = (ImageView) view.findViewById(R.id.iv_food_pic);
            iv_food_pic.setImageBitmap(photo);

        }
    }


    //region common

    //返回
    private void go2Back(){

        /*
        FragmentManager fm  = getFragmentManager();
        FragmentCaloryNew fcn =new FragmentCaloryNew();
        FragmentTransaction ft = fm.beginTransaction();
       // ft.addToBackStack(null);
        ft.replace(R.id.fg_calory_content, fcn);

        ft.commit();
    */
    }

    //儲存資料
    private void SaveData(){

        //先檢核基本資料
        ImageView iv_food_pic = (ImageView)view.findViewById(R.id.iv_food_pic);
        TextView tv_food_type=(TextView)view.findViewById(R.id.tv_food_type);
        EditText edt_food_name =(EditText)view.findViewById(R.id.edt_food_name);
        EditText edt_food_calory=(EditText)view.findViewById(R.id.edt_food_calory);
        EditText et_food_perunit=(EditText)view.findViewById(R.id.et_food_perunit);

        String spic="";

        if(Utility.isEmpty(tv_food_type.getText().toString())){
            Utility.showMessage(getActivity(),"請選擇食物類別");
            return ;
        }

        if(Utility.isEmpty(edt_food_name)){
            edt_food_name.setError("食物名稱請勿空白");
            edt_food_name.requestFocus();
            return ;
        }

        if(Utility.isEmpty(edt_food_calory)){
            edt_food_calory.setError("食物卡路里請勿空白");
            edt_food_calory.requestFocus();
            return ;
        }

        if(Utility.isNumeric(edt_food_calory.getText().toString().trim())==false){
            edt_food_calory.setError("食物卡路里必須為整數數字");
            edt_food_calory.requestFocus();
            return ;
        }

        if(Utility.isEmpty(et_food_perunit)){
            et_food_perunit.setError("每份克數請勿空白");
            et_food_perunit.requestFocus();
            return ;
        }

        if(Utility.isNumeric(et_food_perunit.getText().toString().trim())==false){
            et_food_perunit.setError("每份克數必須為整數數字");
            et_food_perunit.requestFocus();
            return ;
        }

        if(iv_food_pic.getDrawable()!=null){
            spic = fileName;
        }

        String sfoodtypeuid ="";
        for(int i =0;i<mSelectedItems.size();i++){
            FoodTypeModel ft = mSelectedItems.get(i);
            if(sfoodtypeuid==""){
                sfoodtypeuid=ft.TypeUid;
            }
            else{
                sfoodtypeuid+="," + ft.TypeUid;
            }
        }

        FoodModel fm =new FoodModel();
        fm.FoodTypeUid=Utility.getUUID();
        fm.FoodId=Utility.getId("FD");
        fm.CreateDate=Utility.getToday();
        fm.UserUid=UserDAO.getUserUid(getActivity());

        /*
        fm.setFoodUid("");
        fm.setCreateDate(Utility.getToday());
        fm.setPhoto(spic);
        fm.setUpload("0");
        fm.setStatus("1");
        fm.setFoodName(edt_food_calory.getText().toString().trim());
        fm.setCalory(edt_food_calory.getText().toString());
        fm.setPerunit(et_food_perunit.getText().toString().trim());
        fm.setMemo("");
        fm.setMainFoodType(mainType);

        FoodDAO fd =new FoodDAO(getActivity());
        fd.insert(fm);

*/






        //儲存停留在原頁面

        /*
        FragmentManager fmanager  = getFragmentManager();
        FragmentCaloryNew fcn =new FragmentCaloryNew();
        FragmentTransaction ft = fmanager.beginTransaction();
        // ft.addToBackStack(null);
        Bundle b =new Bundle();
        b.putParcelable("FM",fm);

        fcn.setArguments(b);

        ft.replace(R.id.fg_calory_content, fcn);

        ft.commit();
        */

       // go2Back();
    }

    public void takePic(View view) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, TAKE_PICTURE);
        }
    }

    //end region

}
