package com.oli365.nc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oli365.nc.controller.LogDAO;
import com.oli365.nc.controller.NetworkDAO;
import com.oli365.nc.controller.PhraseDAO;
import com.oli365.nc.controller.RecordDAO;
import com.oli365.nc.controller.UserDAO;
import com.oli365.nc.controller.Utility;
import com.oli365.nc.model.Config;
import com.oli365.nc.model.PhraseModel;
import com.oli365.nc.model.RecordModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ActivityRecordAdd extends AppCompatActivity {

    private static final String TAG=ActivityRecordAdd.class.getName();

    private final int TAKE_PICTURE = 102;
    private final int PICK_PICTURE = 103;
    String mCurrentPhotoPath="";
    String mFilename ="";
    String mPath ="";
    boolean mIsPic =false;  //是否有選取圖片
    private ArrayList mSelectedItems;

    private EditText txtBodyAge;
    private TextView txtCreateTime;
    private EditText txtWeight;
    private EditText txtFatRate;
    private EditText txtBoneWeight;
    private EditText txtInsideFat;
    private EditText txtMuscleWeight;
    private EditText txtMuscleRate;
    private EditText txtMetabolism;
    private ImageView txtPhoto;
    private EditText txtrecordmemo;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_add);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

       // initPage();
        initData();

    }

    private void initPage(){

       // Log.d(TAG,"USER LEVEL =" + UserDAO.getUserLevel(this));

        TextInputLayout til_weight = (TextInputLayout) findViewById(R.id.til_weight);   //體重
        TextInputLayout til_fatrate = (TextInputLayout) findViewById(R.id.til_fatrate);   //體脂
        TextInputLayout til_insidefat = (TextInputLayout) findViewById(R.id.til_insidefat);   //內脂
        TextInputLayout til_muscleweight = (TextInputLayout) findViewById(R.id.til_muscleweight);   //肌肉量
        TextInputLayout til_boneweight = (TextInputLayout) findViewById(R.id.til_boneweight);   //骨量
        TextInputLayout til_metabolism = (TextInputLayout) findViewById(R.id.til_metabolism);   //新陳代謝
        TextInputLayout til_bodyage = (TextInputLayout) findViewById(R.id.til_bodyage);   //體年齡
        TextInputLayout til_bodywater = (TextInputLayout) findViewById(R.id.til_bodywater);   //體水份

        /*
        if(UserDAO.getUserLevel(this).equals("2")){
            til_insidefat.setVisibility(View.GONE);
            til_muscleweight.setVisibility(View.GONE);
            til_boneweight.setVisibility(View.GONE);
            til_metabolism.setVisibility(View.GONE);
            til_bodyage.setVisibility(View.GONE);
            til_bodywater.setVisibility(View.GONE);
        }
        else if(UserDAO.getUserLevel(this).equals("1")){
            til_fatrate.setVisibility(View.GONE);
            til_insidefat.setVisibility(View.GONE);
            til_muscleweight.setVisibility(View.GONE);
            til_boneweight.setVisibility(View.GONE);
            til_metabolism.setVisibility(View.GONE);
            til_bodyage.setVisibility(View.GONE);
            til_bodywater.setVisibility(View.GONE);
        }
        */

    }

    private void initData(){

        //現在日期與時間
        TextView createtime =(TextView)findViewById(R.id.txtCreateTime);
        createtime.setText(Utility.getToday());

        //備註欄位
       final EditText txtrecordmemo = (EditText) findViewById(R.id.txtrecordmemo);

        //新增片語
        Button btnMemoAdd =(Button)findViewById(R.id.btnMemoAdd);
        btnMemoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog ad=getAddAlertDialog();
                ad.show();
            }
        });

        //選擇片語
        Button btnMemoSel =(Button)findViewById(R.id.btnMemoSel);
        btnMemoSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhraseDAO pd =new PhraseDAO(ActivityRecordAdd.this);

                final Cursor cr =pd.getCursorAll();
                AlertDialog.Builder selAlert = new AlertDialog.Builder(ActivityRecordAdd.this);
                selAlert.setTitle("選擇片語");
                selAlert.setCursor(cr, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        if(cr!=null){
                            cr.moveToFirst();
                            cr.moveToPosition(which);   //指標移動到該row
                            Log.d(TAG,"指標移動到第"+ which + "筆");
                            //get column 3 是名稱
                            String s =cr.getString(cr.getColumnIndex("MEMO"));
                            Log.d(TAG,"選單內容為"+ s );
                            txtrecordmemo.setText(s);
                        }

                    }

                },"MEMO");


                selAlert.setPositiveButton(getString(R.string.close),new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                selAlert.show();
            }
        });

        //刪除片語
        Button btnMemoDel =(Button)findViewById(R.id.btnMemoDel);
        btnMemoDel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final PhraseDAO pd =new PhraseDAO(ActivityRecordAdd.this);

                final Cursor cr =pd.getCursorAll();
                AlertDialog.Builder delAlert = new AlertDialog.Builder(ActivityRecordAdd.this);
                delAlert.setTitle("刪除片語");

                mSelectedItems = new ArrayList();


                delAlert.setMultiChoiceItems(cr, "MEMO","MEMO",
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(cr.getString(cr.getColumnIndex("MEMO")));
                                    Log.d(TAG,"選取的資料=" + cr.getString(cr.getColumnIndex("MEMO")));
                                } else if(!isChecked){
                                    if (mSelectedItems.contains(which)) {
                                        mSelectedItems.remove(Integer.valueOf(which));
                                    }
                                }
                            }
                        });


                //刪除所選取得的內容
                delAlert.setPositiveButton(getString(R.string.delete),new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {

                        NetworkDAO nd =new NetworkDAO(ActivityRecordAdd.this);

                        for(int i =0;i<mSelectedItems.size();i++){
                            final String itemname = mSelectedItems.get(i).toString();
                            PhraseModel pm = pd.get(itemname);
                            if(pm!=null){

                                if(pd.delete(itemname)){
                                    Utility.showMessage(ActivityRecordAdd.this,"刪除片語" + itemname +"成功");
                                }
                                else{
                                    Utility.showMessage(ActivityRecordAdd.this,"刪除本機片語" + itemname +"失敗");
                                }

                                /*
                                Log.d(TAG,"itemname=" + itemname + ",phraseuid =" + pm.PhraseUid);
                                pm.UploadStatus= Config.UPLOAD_STATUS.DELETE;
                                pm.Status="0";
                                try{
                                    nd.uploadJson("user/phrase/del", pm, new NetworkDAO.downloadJosn() {
                                        @Override
                                        public void onCompleted(NetworkDAO.RETURN_CODE status, Exception ex, String result) {
                                           if(status== NetworkDAO.RETURN_CODE.SUCCESS || status == NetworkDAO.RETURN_CODE.NOT_FOUND){

                                               if(pd.delete(itemname)){
                                                  Utility.showMessage(ActivityRecordAdd.this,"刪除片語" + itemname +"成功");
                                              }
                                               else{
                                                  Utility.showMessage(ActivityRecordAdd.this,"刪除本機片語" + itemname +"失敗");
                                              }


                                           }
                                           else{
                                               Utility.showMessage(ActivityRecordAdd.this,"刪除主機片語" + itemname +"失敗");
                                           }
                                        }
                                    });
                                }
                                catch (Exception ex){

                                    Log.d(TAG,ex.toString());
                                    LogDAO.LogError(ActivityRecordAdd.this,TAG,ex);
                                }
                                */

                            }


                        }

                        mSelectedItems.clear();
                    }
                });

                delAlert.setNegativeButton(getString(R.string.close),new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                delAlert.show();
            }
        });

    }

    //顯下新增對話框
    private AlertDialog getAddAlertDialog(){
        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        ad.setTitle("新增片語");
        final View dialogView = inflater.inflate(R.layout.alert_record_memo,null);

        ad.setNegativeButton(getString(R.string.close) ,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
               //不作任何動作
                //Utility.showMessage(ActivityRecordAdd.this,"關閉");
            }
        });

        //儲存資料
        ad.setPositiveButton(getString(R.string.save),new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                EditText txtalertmemo = (EditText) dialogView.findViewById(R.id.txtalertmemo);
                final String memo = txtalertmemo.getText().toString();
                //檢查不可為空白
                if (Utility.isEmpty(txtalertmemo)) {
                    txtalertmemo.setError("片語請勿空白");
                    txtalertmemo.requestFocus();
                    return;
                }
                //須先檢查是否有同名資料
                PhraseDAO pd =new PhraseDAO(ActivityRecordAdd.this);
                if(pd.hasMemo(memo)==true ){
                    Utility.showMessage(ActivityRecordAdd.this,"已有同名的片語！不可新增");
                    return ;
                }

                //PhraseModel pm =new PhraseModel(0,Utility.getToday(), PhraseModel.PHRASE_TYPE.PRIVATE,memo);
                PhraseModel pm =new PhraseModel();
                pm.CreateDate=Utility.getToday();
                pm.PhraseUid=Utility.getUUID();
                pm.UserUid = UserDAO.getUserUid(ActivityRecordAdd.this);
                pm.PhraseType= Config.PHRASE_TYPE.PRIVATE;
                pm.Memo=memo;
                pm.UploadStatus=Config.UPLOAD_STATUS.INSERT;
                pm.Source= Config.SOURCE_TYPE.CLIENT;
                pm.Status="1";


                try{
                    pd.insert(pm);

                    if(pm.id>0){

                        Utility.showMessage(ActivityRecordAdd.this,"新增片語成功！");
                        /*
                        final long myid = pm.id;
                        //處理資料上傳
                        NetworkDAO nd=new NetworkDAO(ActivityRecordAdd.this);
                        nd.uploadJson("user/phrase", pm, new NetworkDAO.downloadJosn() {
                            @Override
                            public void onCompleted(NetworkDAO.RETURN_CODE status, Exception ex, String result) {
                                if(status== NetworkDAO.RETURN_CODE.SUCCESS){

                                    //將狀態改為已更新
                                    PhraseDAO pd1 = new PhraseDAO(ActivityRecordAdd.this);
                                    PhraseModel pm1 = pd1.get(myid);
                                    pm1.UploadStatus= Config.UPLOAD_STATUS.NONE;
                                    pd1.update(pm1);

                                    Utility.showMessage(ActivityRecordAdd.this,"新增片語成功！");
                                }
                                else{
                                    Utility.showMessage(ActivityRecordAdd.this,"新增片語失敗！");
                                    return ;
                                }
                            }
                        });

                        */



                    }


                }
                catch (Exception ex){
                    Log.d(TAG,ex.toString());
                    LogDAO.LogError(ActivityRecordAdd.this,TAG,ex);
                }


            }
        });

        ad.setView(dialogView);
        return ad.create();
    }

    //建立圖檔
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);

        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    //選取圖檔
    public void pickPic(View view){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.choice_image)), PICK_PICTURE);

        /*
        Intent takePictureIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, PICK_PICTURE);
            }


        }
        */
        /*
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_PICTURE);
        */
    }


    public void takePic(View view) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, TAKE_PICTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //拍照
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            mPath = Utility.getImagePath(this) ;
            Bitmap photo = Utility.resizeBitmap(this,uri);
            mFilename = Utility.SaveIamge(mPath,photo);

            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imgLosePic = (ImageView) findViewById(R.id.imgLosePic);
            imgLosePic.setImageBitmap(photo);

            mIsPic=true;
        }

            //選擇圖片
            if (requestCode == PICK_PICTURE && resultCode == RESULT_OK  && null != data) {

                Uri uri = data.getData();
                mPath =Utility.getImagePath(this) ;
                Log.d(TAG,"PICK PICTURE PATH=" + mPath);
                Bitmap photo = Utility.resizeBitmap(this,uri);
                mFilename = Utility.SaveIamge(mPath,photo);

                ImageView imgLosePic = (ImageView) findViewById(R.id.imgLosePic);
                imgLosePic.setImageBitmap(photo);

                mIsPic=true;

            } else {
                //Toast.makeText(this, "請選擇圖檔",Toast.LENGTH_LONG).show();
                //Utility.showMessage(this,"請選擇圖檔");
            }


    }


    //上傳圖檔
    private boolean uploadImage(){

        try{
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(getString(R.string.firebase_storage_url));
            String urlPath = UserDAO.getUserEmail(this) + "/images";
            Log.d(TAG,"firebase upload path :" + urlPath);
            StorageReference spaceRef = storageRef.child(urlPath);
            mCurrentPhotoPath = mPath + mFilename;
            Log.d(TAG,"local path=" + mCurrentPhotoPath);
            Uri file = Uri.fromFile(new File(mCurrentPhotoPath));

            UploadTask uploadTask = spaceRef.putFile(file);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.e(TAG,"Upload file fail : " + exception.getMessage() );
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Log.i(TAG,"Upload file success , downloadUrl :" + downloadUrl.toString());
                }
            });
        }
        catch (Exception ex ){
            LogDAO.LogError(this,TAG,ex);
            return false;
        }

        return true;
    }

    public void closeView(View view) {
        Intent intent = new Intent();
        //Bundle bundle =new Bundle();
        //bundle.putString("b", "b");
        //intent.putExtra("b","");

        setResult(RESULT_CANCELED, intent);
        finish();
        //Toast.makeText(this, "cancel click", Toast.LENGTH_LONG).show();
    }

    private void checkData(){


    }




    public void saveData(View view) {

        // Toast.makeText(this, "save click", Toast.LENGTH_LONG).show();
        // String currentDateTime = DateFormat.getDateTimeInstance().format((new Date()));
        // String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        txtCreateTime =(TextView)findViewById(R.id.txtCreateTime);
        txtWeight = (EditText) findViewById(R.id.txtWeight);
        txtFatRate = (EditText) findViewById(R.id.txtFatRate);
        txtBoneWeight = (EditText) findViewById(R.id.txtBoneWeight);
        txtBodyAge = (EditText)findViewById(R.id.txtBodyAge);
        txtInsideFat = (EditText)findViewById(R.id.txtInsideFat);
        txtMuscleWeight =(EditText)findViewById(R.id.txtMuscleWeight);
        txtMuscleRate = (EditText)findViewById(R.id.txtMuscleRate);
        txtMetabolism = (EditText)findViewById(R.id.txtMetabolism);
        txtrecordmemo = (EditText)findViewById(R.id.txtrecordmemo);

        //檢查輸入的值是否為空
        if (Utility.isEmpty(txtWeight)) {
            txtWeight.setError("體重請勿空白");
            txtWeight.requestFocus();
            return;
        }



        if (Utility.isEmpty(txtFatRate)) {
            txtFatRate.setError("體脂肪請勿空白");
            txtFatRate.requestFocus();
            return;
        }


        if (Utility.isEmpty(txtBoneWeight)) {
            txtBoneWeight.setError("骨量請勿空白");
            txtBoneWeight.requestFocus();
            return;
        }

        if (Utility.isEmpty(txtBodyAge)) {
            txtBodyAge.setError("身體年齡請勿空白");
            txtBodyAge.requestFocus();
            return;
        }

        if (Utility.isEmpty(txtInsideFat)) {
            txtInsideFat.setError("內臟脂肪請勿空白");
            txtInsideFat.requestFocus();
            return;
        }

        if (Utility.isEmpty(txtMuscleWeight)) {
            txtMuscleWeight.setError("肌肉量請勿空白");
            txtMuscleWeight.requestFocus();
            return;
        }

        if (Utility.isEmpty(txtMuscleRate)) {
            txtMuscleRate.setError("肌肉率請勿空白");
            txtMuscleRate.requestFocus();
            return;
        }
        if (Utility.isEmpty(txtMetabolism)) {
            txtMetabolism.setError("基礎代謝率請勿空白");
            txtMetabolism.requestFocus();
            return;
        }



/*
        String isUpload ="0";   //是否已上傳
        String status ="1"; //狀態
        String email =UserDAO.getUserEmail(this);
        String uid =UserDAO.getUserUid(this);


        if(uploadImage()){
            isUpload="1";
        }
        */
        //儲存資料前先將圖檔加入相薄中
        //galleryAddPic();
        RecordDAO dao = new RecordDAO(this);
        RecordModel recordModel=new RecordModel();
        recordModel.CreateDate=Utility.getToday();
        recordModel.RecordUid=Utility.getUUID();
        recordModel.UserUid=UserDAO.getUserUid(this);
        recordModel.Weight=Double.valueOf(txtWeight.getText().toString());
        recordModel.FatRate=Double.valueOf(txtFatRate.getText().toString());
        recordModel.BoneWeight=Double.valueOf(txtBoneWeight.getText().toString());
        recordModel.BodyAge=Double.valueOf(txtBodyAge.getText().toString());
        recordModel.InsideFat= Double.valueOf(txtInsideFat.getText().toString());
        recordModel.MuscleWeight=Double.valueOf(txtMuscleWeight.getText().toString());
        recordModel.BodyWater=Double.valueOf(txtMuscleRate.getText().toString());
        recordModel.Metabolism=Double.valueOf(txtMetabolism.getText().toString());

        recordModel.Photo=mFilename;
        recordModel.UploadStatus= "I";
        recordModel.Status="1";
        recordModel.Memo=txtrecordmemo.getText().toString();
        recordModel.CalculateMetabolism=dao.getMetabolism(String.valueOf(recordModel.Weight)); //計算的代謝率
        recordModel.CalculateExpenditure=dao.getExpenditure(String.valueOf(recordModel.Weight));
        dao.insert(recordModel);

        if(recordModel.Id>0){

            Utility.showMessage(ActivityRecordAdd.this,"新增成功");
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            //處理個人的設定檔更新
            /*
            SettingDAO sd =new SettingDAO(this);
            int count =dao.getCount();
            if(count==1){
                //只有第一筆更新設定檔
                sd.setInitWeight(String.valueOf(recordModel.Weight));
                sd.setInitFatrate(String.valueOf(recordModel.FatRate));
                sd.setInitFatweight(dao.getFatWeight(recordModel.Weight,recordModel.FatRate));
            }

*/
            //接著每次新增記錄，都要更新的設定檔的資料
            /*
            sd.setWeight(String.valueOf(recordModel.Weight));
            sd.setFatrate(String.valueOf(recordModel.FatRate));
            sd.setFatweight(dao.getFatWeight(recordModel.Weight,recordModel.FatRate));
            sd.setMetabolism(dao.getMetabolism(String.valueOf(recordModel.Weight)));
            sd.setExpenditure(dao.getExpenditure(String.valueOf(recordModel.Weight)));
*/
            //上傳更新的記錄檔
             /*
            NetworkDAO network =new NetworkDAO(this);

            SettingModel sm=sd.getSetting();
            try{
                network.uploadJson("user/setting", sm, new NetworkDAO.downloadJosn() {
                    @Override
                    public void onCompleted(NetworkDAO.RETURN_CODE status, Exception ex, String result) {
                        if(status== NetworkDAO.RETURN_CODE.SUCCESS){
                            Utility.showMessage(ActivityRecordAdd.this,"更新設定檔成功");
                        }
                        else{
                            Utility.showMessage(ActivityRecordAdd.this,"更新設定檔失敗：" + result,Toast.LENGTH_LONG);
                        }
                    }
                });
            }
            catch(Exception ex){
                Log.d(TAG,ex.toString());
                LogDAO.LogError(this,TAG,ex);
            }

*/

            final String recorduid =recordModel.RecordUid;


            //上傳圖檔
            /*
            network.uploadImage(mFilename);

            try{
                network.uploadJson("user/record", recordModel, new NetworkDAO.downloadJosn() {
                    @Override
                    public void onCompleted(NetworkDAO.RETURN_CODE status, Exception ex, String result) {
                        if(status== NetworkDAO.RETURN_CODE.SUCCESS){
                            RecordDAO rd =new RecordDAO(ActivityRecordAdd.this);
                            rd.setUpload(recorduid);
                            Utility.showMessage(ActivityRecordAdd.this,"新增成功");
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();

                        }
                        else{
                            Utility.showMessage(ActivityRecordAdd.this,"新增失敗："+result,Toast.LENGTH_LONG);
                        }
                    }
                });
            }
            catch (Exception ex){
                Log.d(TAG,ex.toString());
                LogDAO.LogError(this,TAG,ex);
            }

*/


        }


    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "ActivityRecordAdd Page",

                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),

                Uri.parse("android-app://com.oli365.nc/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "ActivityRecordAdd Page",

                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),

                Uri.parse("android-app://com.oli365.nc/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
