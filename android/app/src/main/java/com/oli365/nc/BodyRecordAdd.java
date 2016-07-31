package com.oli365.nc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BodyRecordAdd extends AppCompatActivity {

    private final int TAKE_PICTURE = 102;
    private final int PICK_PICTURE = 103;
    String mCurrentPhotoPath="";

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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_record_add);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        initData();
    }

    private void initData(){

        TextView createtime =(TextView)findViewById(R.id.txtCreateTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        createtime.setText(sdf.format(now));


        txtBodyAge = (EditText)findViewById(R.id.txtBodyAge);
        Button btn =(Button)findViewById(R.id.btnSave);
        /*
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String value = txtBodyAge.getText().toString();
                if(value==null || value.trim().equals("")) {
                    txtBodyAge.setError("请输入内容！");
                    return;
                }
            }
        });
        */

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

    public void pickPic(View view){


 /*
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, PICK_PICTURE);
        */

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "選擇圖片"), PICK_PICTURE);

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
            Bitmap photo =Utility.resizeBitmap(this,uri);
            mCurrentPhotoPath = Utility.SaveIamge(photo);

            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imgLosePic = (ImageView) findViewById(R.id.imgLosePic);
            imgLosePic.setImageBitmap(photo);

        }


            // When an Image is picked
            if (requestCode == PICK_PICTURE && resultCode == RESULT_OK  && null != data) {

                Uri uri = data.getData();

                Bitmap photo = Utility.resizeBitmap(this,uri);
                mCurrentPhotoPath = Utility.SaveIamge(photo);

                ImageView imgLosePic = (ImageView) findViewById(R.id.imgLosePic);
                imgLosePic.setImageBitmap(photo);


            } else {
                //Toast.makeText(this, "請選擇圖檔",Toast.LENGTH_LONG).show();
            }



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

        //儲存資料前先將圖檔加入相薄中
        //galleryAddPic();

        Record record = new Record(0
                , txtCreateTime.getText().toString()
                , Double.valueOf(txtWeight.getText().toString())
                , Double.valueOf(txtFatRate.getText().toString())
                , Double.valueOf(txtBoneWeight.getText().toString())
                , Double.valueOf(txtBodyAge.getText().toString())
                , Double.valueOf(txtInsideFat.getText().toString())
                , Double.valueOf(txtMuscleWeight.getText().toString())
                , Double.valueOf(txtMuscleRate.getText().toString())
                , Double.valueOf(txtMetabolism.getText().toString())
                , mCurrentPhotoPath);
                //todo 要確認圖檔是否存在，若否則傳null
        RecordDAO dao = new RecordDAO(this);
        dao.insert(record);


        //新增記錄時更新設定中的體重
        UserUtility uu =new UserUtility(this);
        uu.setWeight(txtWeight.getText().toString());
        uu.setMetabolism(txtMetabolism.getText().toString());
        uu.setBodyFat(txtFatRate.getText().toString());

        //提示訊息
        Toast.makeText(this, "新增成功！", Toast.LENGTH_LONG).show();

        Intent intent = new Intent();
        //Bundle bundle =new Bundle();
        //bundle.putString("b", "b");
        //intent.putExtra("b","");

        setResult(RESULT_OK, intent);
        finish();
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
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BodyRecordAdd Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
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
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BodyRecordAdd Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.oli365.nc/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
