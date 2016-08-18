package com.oli365.nc;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * Created by alvinlin on 2016/4/3.
 */
public class RecordCursorAdapter extends CursorAdapter {

    /*
    private TextView txtCreateTime;
    private TextView txtWeight;
    private TextView txtFatRate;
    private TextView txtMetabolism;
    private TextView txtBodyAge;
    private TextView txtBoneWeight;
    private TextView txtInsideFat;
    private TextView txtMuscleWeight;
    private TextView txtMuscleRate;
    */

    public RecordCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }


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

        TextView txtCreateTime = (TextView) view.findViewById(R.id.txtCreateTime);
        TextView txtWeight = (TextView) view.findViewById(R.id.txtWeight);
        TextView txtFatRate = (TextView) view.findViewById(R.id.txtFatRate);
        TextView txtMetabolism =(TextView)view.findViewById(R.id.txtMetabolism);
        TextView txtBodyAge =(TextView)view.findViewById(R.id.txtBodyAge);
        TextView txtBoneWeight =(TextView)view.findViewById(R.id.txtBoneWeight);
        TextView txtInsideFat =(TextView)view.findViewById(R.id.txtInsideFat);
        TextView txtMuscleWeight=(TextView)view.findViewById(R.id.txtMuscleWeight);
        TextView txtMuscleRate = (TextView)view.findViewById(R.id.txtMuscleRate);
        ImageView txtPhoto =(ImageView)view.findViewById(R.id.txtPhoto);

        String createTime =cursor.getString(cursor.getColumnIndexOrThrow("CREATE_TIME"));
        createTime = Utility.getShortDate(createTime);  //去掉秒數
        double weight =cursor.getDouble(cursor.getColumnIndexOrThrow("WEIGHT"));
        double fatrate =cursor.getDouble(cursor.getColumnIndexOrThrow("FAT_RATE"));
        double metabolism =cursor.getInt(cursor.getColumnIndexOrThrow("METABOLISM"));
        double bodyAge = cursor.getInt(cursor.getColumnIndexOrThrow("BODY_AGE"));
        double boneWeight = cursor.getDouble(cursor.getColumnIndexOrThrow("BONE_WEIGHT"));
        double insideFat = cursor.getDouble(cursor.getColumnIndexOrThrow("INSIDE_FAT"));
        double muscleWeight =  cursor.getDouble(cursor.getColumnIndexOrThrow("MUSCLE_WEIGHT"));
        double muscleRate =  cursor.getInt(cursor.getColumnIndexOrThrow("MUSCLE_RATE"));    //體水分
        String photo = cursor.getString(cursor.getColumnIndexOrThrow("PHOTO"));

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
            muscleratediff = muscleRate - cursor.getInt(cursor.getColumnIndexOrThrow("MUSCLE_RATE"));
            insidefatdiff = insideFat -cursor.getDouble(cursor.getColumnIndexOrThrow("INSIDE_FAT"));
        }

        DecimalFormat df=new DecimalFormat("#0.##");
        String sWeightdiff=df.format(weightdiff);
        String sFatratediff=df.format(fatratediff);
        String sMetabolismdiff =df.format(metabolismdiff);
        String sBodyagediff =df.format(bodyagediff);
        String sBoneweightdiff = df.format(boneweightdiff);
        String sMuscleweightdiff =df.format(muscleweightdiff);
        String sMuscleratediff =df.format(muscleratediff);
        String sInsidefatdiff =df.format(insidefatdiff);

        txtCreateTime.setText(String.valueOf(createTime));
        txtWeight.setText(String.valueOf(weight + "/" + sWeightdiff ));
        txtFatRate.setText(String.valueOf(fatrate)+"%" + "/" + sFatratediff + "%");
        txtMetabolism.setText(String.valueOf(metabolism) + "/" + sMetabolismdiff );
        txtBodyAge.setText(String.valueOf(bodyAge) + "/" + sBodyagediff );
        txtBoneWeight.setText(String.valueOf(boneWeight) + "/" + sBoneweightdiff);
        txtMuscleWeight.setText(String.valueOf(muscleWeight) + "/" + sMuscleweightdiff);
        txtMuscleRate.setText(String.valueOf(muscleRate) + "/" + sMuscleratediff);
        txtInsideFat.setText(String.valueOf(insideFat) + "/" + sInsidefatdiff);

        if(!photo.equals("")){

            try{
                Uri uri =Uri.parse("file://" + photo);
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

                float widthRatio = (float) 800 / (float)bitmapOptions.outWidth;
                float heightRatio =(float) 600 / (float)bitmapOptions.outHeight;
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

        }

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
            txtMuscleRate.setTextColor(Color.DKGRAY);
        }
        else if(muscleratediff<0){
            txtMuscleRate.setTextColor(Color.RED);
        }
        else{
            txtMuscleRate.setTextColor(Color.BLUE);
        }
    }


}
