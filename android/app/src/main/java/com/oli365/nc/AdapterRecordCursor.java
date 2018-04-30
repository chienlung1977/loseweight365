package com.oli365.nc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.oli365.nc.controller.RecordDAO;
import com.oli365.nc.controller.Utility;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.text.DecimalFormat;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by alvinlin on 2016/4/3.
 */
public class AdapterRecordCursor extends CursorAdapter {


    private static final String TAG=AdapterRecordCursor.class.getName();
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    View view ;
    Context context ;
    Cursor cursor;
    int flag ;



    public AdapterRecordCursor(Context context, Cursor c) {
        super(context, c);
        this.cursor =c;
    }

    /*
    public AdapterRecordCursor(Context context, Cursor c, int flag) {
        //super(context, c, 0);
        this.context = context ;
        this.cursor= c ;
    }

*/

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        // View header = LayoutInflater.from(context).inflate(R.layout.record_header,null);
        // mList.addHeaderView(header);


        return LayoutInflater.from(context).inflate(R.layout.record_item,parent,false);
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
    public void bindView(View view, Context context, Cursor cursor) {

        this.view = view;
        this.context =context;
        this.cursor =cursor;

        //載入資料
        bindingEvent();
        bindingData();

    }

    //region 載入資料

    private void bindingEvent(){

        final Context c =context;
        final String id =cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(c, "_id=" + id , Toast.LENGTH_SHORT).show();
                AlertDialog diaBox = AskOption(c,id);
                diaBox.show();

                // Toast.makeText(c, "_id=" + id + ",recordUid=" + recordUid, Toast.LENGTH_LONG).show();
                return true ;
            }
        });
    }

    private void bindingData(){

        TextView txtCreateTime = (TextView) view.findViewById(R.id.txtCreateTime);
        TextView txtWeight = (TextView) view.findViewById(R.id.txtWeight);
        TextView txtFatRate = (TextView) view.findViewById(R.id.txtFatRate);
        TextView txtMetabolism =(TextView)view.findViewById(R.id.txtMetabolism);
        TextView txtBodyAge =(TextView)view.findViewById(R.id.txtBodyAge);
        TextView txtBoneWeight =(TextView)view.findViewById(R.id.txtBoneWeight);
        TextView txtInsideFat =(TextView)view.findViewById(R.id.txtInsideFat);
        TextView txtMuscleWeight=(TextView)view.findViewById(R.id.txtMuscleWeight);
        TextView txtBodyWater = (TextView)view.findViewById(R.id.txtMuscleRate);
        ImageView txtPhoto =(ImageView)view.findViewById(R.id.txtPhoto);
        TextView tvRecordMainMemo =(TextView)view.findViewById(R.id.tvRecordMainMemo);

        String createTime =cursor.getString(cursor.getColumnIndexOrThrow("CREATE_TIME"));
        createTime = Utility.getShortDate(createTime);  //去掉秒數

        DecimalFormat df=new DecimalFormat("0.0");

        double weight =cursor.getDouble(cursor.getColumnIndexOrThrow("WEIGHT"));
        double fatrate =cursor.getDouble(cursor.getColumnIndexOrThrow("FAT_RATE"));
        double metabolism =cursor.getInt(cursor.getColumnIndexOrThrow("METABOLISM"));
        double bodyAge = cursor.getInt(cursor.getColumnIndexOrThrow("BODY_AGE"));
        double boneWeight = cursor.getDouble(cursor.getColumnIndexOrThrow("BONE_WEIGHT"));
        double insideFat = cursor.getDouble(cursor.getColumnIndexOrThrow("INSIDE_FAT"));
        double muscleWeight =  cursor.getDouble(cursor.getColumnIndexOrThrow("MUSCLE_WEIGHT"));
        double bodywater =  cursor.getDouble(cursor.getColumnIndexOrThrow("BODY_WATER"));    //體水分
        String photo = cursor.getString(cursor.getColumnIndexOrThrow("PHOTO"));
        String memo =cursor.getString(cursor.getColumnIndexOrThrow("MEMO"));

        final String id =cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        final String recordUid =cursor.getString(cursor.getColumnIndexOrThrow("RECORD_UID"));

        double weightdiff =0;
        double fatratediff =0;
        double metabolismdiff =0;
        double bodyagediff=0;
        double boneweightdiff=0;
        double insidefatdiff =0;
        double muscleweightdiff =0;
        double muscleratediff =0 ;  //變體水份


        //與下一筆的差異
        if(cursor.moveToNext()){
            weightdiff=weight-cursor.getDouble(cursor.getColumnIndexOrThrow("WEIGHT"));
            fatratediff=fatrate-cursor.getDouble(cursor.getColumnIndexOrThrow("FAT_RATE"));
            metabolismdiff = metabolism - cursor.getInt(cursor.getColumnIndexOrThrow("METABOLISM"));
            bodyagediff = bodyAge -cursor.getInt(cursor.getColumnIndexOrThrow("BODY_AGE"));
            boneweightdiff = boneWeight - cursor.getDouble(cursor.getColumnIndexOrThrow("BONE_WEIGHT"));
            muscleweightdiff = muscleWeight - cursor.getDouble(cursor.getColumnIndexOrThrow("MUSCLE_WEIGHT"));
            muscleratediff = bodywater - cursor.getDouble(cursor.getColumnIndexOrThrow("BODY_WATER"));
            insidefatdiff = insideFat -cursor.getDouble(cursor.getColumnIndexOrThrow("INSIDE_FAT"));
        }


        String sWeightdiff=df.format(weightdiff);
        String sFatratediff=df.format(fatratediff);
        String sMetabolismdiff =df.format(metabolismdiff);
        String sBodyagediff =df.format(bodyagediff);
        String sBoneweightdiff = df.format(boneweightdiff);
        String sMuscleweightdiff =df.format(muscleweightdiff);
        String sMuscleratediff =df.format(muscleratediff);
        String sInsidefatdiff =df.format(insidefatdiff);

        txtCreateTime.setText(String.valueOf(createTime));
        txtWeight.setText(String.valueOf(df.format(weight) + "/" + sWeightdiff ));
        txtFatRate.setText(String.valueOf(df.format(fatrate))+"%" + "/" + sFatratediff + "%");
        txtMetabolism.setText(String.valueOf(df.format(metabolism)) + "/" + sMetabolismdiff );
        txtBodyAge.setText(String.valueOf(df.format(bodyAge)) + "/" + sBodyagediff );
        txtBoneWeight.setText(String.valueOf(df.format(boneWeight)) + "/" + sBoneweightdiff);
        txtMuscleWeight.setText(String.valueOf(df.format(muscleWeight)) + "/" + sMuscleweightdiff);
        txtBodyWater.setText(String.valueOf(df.format(bodywater)) + "/" + sMuscleratediff);
        txtInsideFat.setText(String.valueOf(df.format(insideFat)) + "/" + sInsidefatdiff);

        tvRecordMainMemo.setText(memo);

        if(!photo.equals("")){

            try{
                Uri uri =Uri.parse("file://" + Utility.getImagePath(view.getContext()) +  photo);
                //String path =getRealPathFromURI(context,uri);

                //             context.grantUriPermission(context.getPackageName(), uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                //Intent data = new Intent(RecordCursorAdapter.)
                //final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION);
                // context.getContentResolver().takePersistableUriPermission(uri, takeFlags);

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inJustDecodeBounds = true;
                InputStream in = context.getContentResolver().openInputStream(
                        uri);
                BitmapFactory.decodeStream(in, null, bitmapOptions);

                // float widthRatio = (float) 1024 / (float)bitmapOptions.outWidth;
                // float heightRatio =(float) 768 / (float)bitmapOptions.outHeight;

                float widthRatio = (float) 640 / (float)bitmapOptions.outWidth;
                float heightRatio =(float) 480 / (float)bitmapOptions.outHeight;
                float sheight ;
                float sweight ;
                if(widthRatio>heightRatio){
                    sheight= bitmap.getHeight()*heightRatio;
                    sweight =bitmap.getWidth()*heightRatio;
                }
                else{
                    sheight= bitmap.getHeight()*widthRatio;
                    sweight =bitmap.getWidth()*widthRatio;
                }

                int newheight = (int)sheight;
                int newweight =(int)sweight;

                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(bitmap, newweight, newheight);

                txtPhoto.setImageBitmap(ThumbImage);



            }
            catch (Exception ex){
                txtPhoto.setImageResource(R.drawable.nopic);

            }
        }
        else {
            txtPhoto.setImageResource(R.drawable.nopic);

            // txtPhoto.setVisibility(View.GONE);
        }


        txtPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext()," click photo"   ,
                        Toast.LENGTH_LONG).show();

            }
        });



        if(weightdiff==0){
            txtWeight.setTextColor(Color.DKGRAY);
        }else if(weightdiff<0){
            txtWeight.setTextColor(Color.BLUE);
        }
        else{
            txtWeight.setTextColor(android.graphics.Color.RED);
        }

        if(fatratediff==0){
            txtFatRate.setTextColor(Color.DKGRAY);
        }
        else if(fatratediff<0){
            txtFatRate.setTextColor(Color.BLUE);
        }
        else{
            txtFatRate.setTextColor(android.graphics.Color.RED);
        }

        if(metabolismdiff==0){
            txtMetabolism.setTextColor(Color.DKGRAY);
        }
        else if(metabolismdiff<0){
            txtMetabolism.setTextColor(Color.RED);
        }
        else {
            txtMetabolism.setTextColor(Color.BLUE);
        }


        if(bodyagediff==0){
            txtBodyAge.setTextColor(Color.DKGRAY);
        }
        else if(bodyagediff<0){
            txtBodyAge.setTextColor(Color.BLUE);
        }
        else{
            txtBodyAge.setTextColor(android.graphics.Color.RED);
        }

        if(insidefatdiff==0){
            txtInsideFat.setTextColor(Color.DKGRAY);
        }
        else if(insidefatdiff<0){
            txtInsideFat.setTextColor(Color.BLUE);
        }
        else{
            txtInsideFat.setTextColor(android.graphics.Color.RED);
        }

        if(muscleweightdiff==0){
            txtMuscleWeight.setTextColor(Color.DKGRAY);
        }
        else if(muscleweightdiff<0){
            txtMuscleWeight.setTextColor(Color.RED);
        }
        else{
            txtMuscleWeight.setTextColor(Color.BLUE);
        }

        if(boneweightdiff==0){
            txtBoneWeight.setTextColor(Color.DKGRAY);
        }
        else if(boneweightdiff<0){
            txtBoneWeight.setTextColor(Color.RED);
        }
        else{
            txtBoneWeight.setTextColor(Color.BLUE);
        }

        if(muscleratediff==0){
            txtBodyWater.setTextColor(Color.DKGRAY);
        }
        else if(muscleratediff<0){
            txtBodyWater.setTextColor(Color.RED);
        }
        else{
            txtBodyWater.setTextColor(Color.BLUE);
        }


        //判斷如果有圖檔才顯示分享
        //分享按鈕
        ShareButton btn_fb_share =(ShareButton)view.findViewById(R.id.btn_fb_share);
        // btn_fb_share.setEnabled(false);
        if(!"".equals(photo)){

            try{
                String filePath="file://" + Utility.getImagePath(view.getContext()) +  photo;
                File f =new File(URI.create(filePath).getPath());
                //Log.d(TAG,"1." + createTime +"=" +  filePath + ",exist=" + f.exists());
                if(f.exists()==true){
                    //Log.d(TAG,"2." + createTime +"=" +  filePath + ",exist=" + f.exists());
                    Uri uri =Uri.parse(filePath);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                    Log.d(TAG,"2." + createTime +"=" +  filePath + ",exist=" + f.exists()+",bitmap=null" + (bitmap==null));
                    String content ="體重差異：" + weightdiff + ",體脂差異：" + fatratediff + "%";       //似乎沒作用
                    setShareFacebook(btn_fb_share,bitmap,content); //設定事件
                }
                else{
                    Log.d(TAG,"3." + createTime +"=" +  filePath + ",exist=" + f.exists());
                }


            }
            catch (Exception e){
                Utility.showMessage(view.getContext(),e.getMessage());
            }



        }


    }


    //region 自訂函式

    //將照片分享到facebook
    private void setShareFacebook(ShareButton btn_fb_share,Bitmap image,String imagecontent){

        btn_fb_share.setEnabled(true);
/*
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle("test")
                .setContentDescription("測試內容")
                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.oli365.nc"))
                .build();

        btn_fb_share.setShareContent(content);

*/

        //分享圖檔

       // Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption(imagecontent)
                .build();


        SharePhotoContent content =new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        btn_fb_share.setShareContent(content);




    }


    //endregion


    //region 載入器

/*
    public Loader<Cursor> onCreateLoader(int id, Bundle arg) {
        return new SimpleCursorLoader(this) {
            public Cursor loadInBackground() {
                // This field reserved to what you want to load in your cursor adater and you return it (for example database query result)
                // return Cursor ;
            }
        };
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    public void onLoaderReset(Loader<Cursor> loader){
        adapter.swapCursor(null);
    }
    */
    //endregion

    private AlertDialog AskOption(final Context context, String id)
    {

        final long _id = Long.valueOf(id);
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(context)
                //set message, title, and icon
                //.setTitle("Delete")
                .setMessage("確定要刪除？")
                // .setIcon(R.drawable.delete)
                .setPositiveButton("刪除", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //       android.os.Debug.waitForDebugger();
                        //上傳刪除記錄，接著才刪除本地端內容
                        RecordDAO rd =new RecordDAO(context);


                        if(rd.delete(_id)){
                            Toast.makeText(context, "_id=" + _id , Toast.LENGTH_SHORT).show();
                            Utility.showMessage(context,"刪除成功");

                        }
                        else{
                            Utility.showMessage(context,"刪除失敗");
                        }


                        //bindData();
                        /*
                        NetworkDAO nd =new NetworkDAO(ActivityRecordMain.this);
                        RecordDAO rd =new RecordDAO(ActivityRecordMain.this);
                        Log.d(TAG,"get id =" + _id);
                        RecordModel rm = rd.get(_id);

                        Log.d(TAG,"rm != null:" + String.valueOf(rm!=null));

                        if(rm!=null){
                            //開始上傳刪除記錄
                            rm.UploadStatus= "D";
                            rm.Status="0";

                            try{
                                nd.uploadJson("user/record/update", rm, new NetworkDAO.downloadJosn() {
                                    @Override
                                    public void onCompleted(NetworkDAO.RETURN_CODE status, Exception ex, String result) {

                                       // android.os.Debug.waitForDebugger();
                                        Log.d(TAG,"Delete Record return result=" + result);

                                        if(status== NetworkDAO.RETURN_CODE.SUCCESS){
                                            //真正刪除本地資料
                                            RecordDAO rd =new RecordDAO(ActivityRecordMain.this);
                                            rd.delete(_id);
                                            Utility.showMessage(ActivityRecordMain.this,"刪除成功");
                                            bindData();
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

*/
                        dialog.dismiss();

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

}
