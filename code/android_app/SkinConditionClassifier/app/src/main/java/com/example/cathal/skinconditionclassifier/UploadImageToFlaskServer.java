package com.example.cathal.skinconditionclassifier;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
@SuppressWarnings("deprecation")
public class UploadImageToFlaskServer {

    public static final String TAG = "Upload Image Flask";
    public static String responseStr;
    public void doFileUpload(final String url, final Bitmap bmp, final Handler handler){
        final String [] responses = new String[1];
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Log.i(TAG, "Starting Upload...");
                final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                try {
                    //String bit = convertBitmapToString(bmp);
                    nameValuePairs.add(new BasicNameValuePair("image", convertBitmapToString(bmp)));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(url);
                    //httppost.setHeader("Content-Type", "image/png");
                    //httppost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("image", convertBitmapToString(bmp));
                    StringEntity j = new StringEntity(jsonObj.toString());
                    httppost.setEntity(j);
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    //System.out.println(responseStr + "blah");

                    Log.i(TAG, "doFileUpload Response : " + responseStr);
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    System.out.println("Error in http connection " + e.toString());
                    handler.sendEmptyMessage(0);
                }

            }
        });
        t.start();


    }

    public String convertBitmapToString(Bitmap bmp) throws UnsupportedEncodingException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream); //compress to which format you want. // was 90
        byte[] byte_arr = stream.toByteArray();
        String imageStr =  new String(Base64.encodeBase64(byte_arr));
        //System.out.println(imageStr);
        return imageStr;
    }
}