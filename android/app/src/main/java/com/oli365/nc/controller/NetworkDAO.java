package com.oli365.nc.controller;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.io.File;
import java.util.concurrent.Future;

/**
 * Created by alvinlin on 2016/11/19.
 */

public class NetworkDAO<T> implements Runnable {

    static final String TAG=NetworkDAO.class.getName();

    Context context ;

    public NetworkDAO(Context context) {
        this.context = context;
    }

    //給前端call back使用(result等於空字串代表回傳的資料有問題)
    
    public interface  downloadJosn{
        void onCompleted(RETURN_CODE status,Exception ex,String result);
    }

    @Override
    public void run() {

    }
    /*
    
    public interface downloadResonseString {
         void onCompleted(Exception ex,String result);
    }

    public interface downloadJsonObject{
        void onCompleted(Exception ex,JsonObject result);
    }

*/

    public enum RETURN_CODE {
        SUCCESS,FAIL,NOT_FOUND
    }


    //檢查網路是否連線中
    public boolean checkNetwork(){

        ConnectivityManager CM = (ConnectivityManager)   context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = CM.getActiveNetworkInfo();
        if(info.isConnected()){
            return true;
        }

        return false;
    }

/*
    //上傳jsonObject 回傳jsonObject
    public void upload(String jspath,JsonObject jsonObject,final downloadJsonObject callback) throws  Exception{


        String url = Utility.getHostUrl(context,jspath);
        Ion.with(context)
                .load(url)
                .setLogging("MyLogs", Log.DEBUG)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        callback.onCompleted(e,result);
                    }
                });
    }



    //上傳jsonObject回傳string
    public void  upload(String jspath,JsonObject json,final downloadResonseString callback) throws  Exception {

        if(checkNetwork()==false){
            return ;
        }

        String url = Utility.getHostUrl(context,jspath);

        Ion.with(context)
                .load(url)
                .setLogging("MyLogs", Log.DEBUG)
                .setJsonObjectBody(json)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> response) {
                        callback.onCompleted(e,response.getResult());

                    }
                });


    }

*/


    //上傳json格式，並回傳結果
    //必須用非同步方式執行
    public void uploadJson(String jspath, T obj, final downloadJosn callback) throws Exception{

        if(checkNetwork()==false){
            Log.d(TAG,"網路未開啓");
            callback.onCompleted(RETURN_CODE.FAIL, new NetworkErrorException("網路未開啓"),"");
            return ;
        }

        if(obj!=null){
           /*
            Gson gson = new Gson();
            String json = gson.toJson(obj);
            Log.d(TAG, "object to json string = " +json);
*/
            //要上傳的路徑
            String url = Utility.getHostUrl(context,jspath);

            //Log.d(TAG,"post url =" + url + ",json content = " + jsonString);

            Ion.with(context)
                    .load(url)
                    .setLogging("MyLogs", Log.DEBUG)
                    .setJsonPojoBody(obj)
                    .asString()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {

                            if(result!=null && result.getHeaders().code()==200){
                                callback.onCompleted(RETURN_CODE.SUCCESS,null,result.getResult());
                            }
                            else if(result!=null && result.getHeaders().code()==404){
                                callback.onCompleted(RETURN_CODE.NOT_FOUND,null,result.getResult());
                            }
                            else{
                                callback.onCompleted(RETURN_CODE.FAIL,null,result.getResult());
                            }
                            //callback.onCompleted(e,result);
                        }
                    });
        }

    }





    //上傳圖檔
    public void uploadImage(String imageName){

        try{

            String mFileName = imageName;
            String mLocalPath = Utility.getImagePath(context);
            String mLocalFullName = mLocalPath + mFileName; //本地完整名稱
            //mServerPath=UserDAO.getUserEmail(this) + "/images/" + mFileName;

            String url = Utility.getHostUrl(context,"/image/upload");

            File f = new File(mLocalFullName);

            Future uploading = Ion.with(context)
                    .load(url)
                    .setMultipartParameter("uid",UserDAO.getUserUid(context) )
                    .setMultipartParameter("email",UserDAO.getUserEmail(context) )
                    .setMultipartFile("image", f)
                    .asString()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {

                            /*
                            try {
                                JSONObject jobj = new JSONObject(result.getResult());
                                // Toast.makeText(getApplicationContext(), jobj.getString("response"), Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"upload image result =" + result.getResult() + ",code=" + result.getHeaders().code());

                            } catch (JSONException e1) {
                                // e1.printStackTrace();
                                Log.d(TAG,"upload image error =" + e1.toString());
                            }
                            */

                        }
                    });

            boolean isDone =uploading.isDone();
            Log.d(TAG,"is upload=" + String.valueOf(isDone));



            /*
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(getString(R.string.firebase_storage_url));

            StorageReference spaceRef = storageRef.child(mServerPath);
            Uri file = Uri.fromFile(new File(mLocalFullName));

            UploadTask uploadTask = spaceRef.putFile(file);


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

            */
        }
        catch (Exception ex ){
            LogDAO.LogError(context,TAG,ex);
        }

        //return true;
    }

}
