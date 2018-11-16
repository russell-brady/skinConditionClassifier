package com.example.cathal.skinconditionclassifier;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "Upload Image";

    public static final String UPLOAD_URL = "http://54.191.193.7:5000/predict";

    public static final String CHART_URL = "http://54.191.193.7:5000/appChart";

    public static final String UPLOAD_KEY = "upload_image";

    private int PICK_IMAGE_REQUEST = 100;

    private Button btnSelect, btnUpload, btnUrl;
    private TextView txtStatus;
    private TextView responseStatus;

    private ImageView imgView;

    private Bitmap bitmap;

    private Uri filePath;

    private String selectedFilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        imgView = (ImageView) findViewById(R.id.imgView);
        btnSelect = (Button) findViewById(R.id.btnSelect);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnUrl = (Button) findViewById(R.id.btnUrl);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        responseStatus = (TextView) findViewById(R.id.responseStatus);

        btnSelect.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnUrl.setOnClickListener(this);

    }

    Handler handler = handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "Handler " + msg.what);
            if (msg.what == 1) {
                txtStatus.setText("Upload Success");
                responseStatus.setText(UploadImageToFlaskServer.responseStr);

            } else {
                txtStatus.setText("Upload Error");

            }
        }

    };

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            selectedFilePath = getPath(filePath);
            Log.i(TAG, " File path : " + selectedFilePath);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false); //// new
                imgView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void uploadImage() {

        UploadImageToFlaskServer uploadTask = new UploadImageToFlaskServer();
        uploadTask.doFileUpload(UPLOAD_URL, bitmap, handler);


    }

    private void visitUrl() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(CHART_URL));
        startActivity(i);

    }

    @Override
    public void onClick(View v) {
        if (v == btnSelect) {
            responseStatus.setText("");
            txtStatus.setText("");
            showFileChooser();


        } else if (v == btnUpload) {
            if (imgView.getDrawable() == null) {
                Toast.makeText(UploadActivity.this, "Unavailable while the image is not selected!",
                        Toast.LENGTH_LONG).show();
            } else {
                txtStatus.setText("Uploading Started...");
                uploadImage();
            }

        } else {
            if (!txtStatus.getText().toString().equals("Upload Success")) {
                Toast.makeText(UploadActivity.this, "Unavailable while the image is not uploaded!",
                            Toast.LENGTH_LONG).show();
                }
            else {
                visitUrl();
            }
        }
    }
}
