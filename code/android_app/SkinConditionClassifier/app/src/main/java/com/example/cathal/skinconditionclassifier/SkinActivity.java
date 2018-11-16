package com.example.cathal.skinconditionclassifier;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;
@SuppressWarnings("deprecation")
public class SkinActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "Upload Image";

    // I am using my local server for uploading image, you should replace it with your server address
    public static final String UPLOAD_URL = "http://54.191.193.7:5000/predict";
    public static final String CHART_URL = "http://54.191.193.7:5000/appChart";

    public static final String UPLOAD_KEY = "upload_image";

    private int PICK_IMAGE_REQUEST = 100;

    private Button btnSelect, btnUpload;
    private TextView txtStatus;
    private TextView responseStatus;

    private ImageView imgView;

    private Bitmap bitmap;

    private Uri filePath;

    private String selectedFilePath;

    private CameraView cameraView;
    private TextView classificationText;
    private Button classifyButton;
    private ImageView pictureView;
    private Button loadImageButton;
    private Button btnUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        cameraView = (CameraView) findViewById(R.id.cameraView);
        classifyButton = (Button) findViewById(R.id.classifySkinButton);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        responseStatus = (TextView) findViewById(R.id.responseStatus);
        btnUrl = (Button) findViewById(R.id.btnUrl);
        pictureView = (ImageView) findViewById(R.id.pictureView);
        //txtStatus.setText(" ");
        //cameraView.start();
//        btnSelect.setOnClickListener(this);
//        btnUpload.setOnClickListener(this);

        cameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] jpeg) {
                super.onPictureTaken(jpeg);

                bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);

                bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false);

                pictureView.setImageBitmap(bitmap);

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadImage();
                    }
                });
                t.start();
            }

        });

        classifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStatus.setText("Uploading Started...");
                responseStatus.setText("");
                cameraView.captureImage();
            }
        });

        btnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(txtStatus.toString().equals(" "));

                if(!txtStatus.getText().toString().equals("Upload Success")) {
                    Toast.makeText(SkinActivity.this, "Unavailable while the image is not uploaded!",
                            Toast.LENGTH_LONG).show();

                } else {
                    visitUrl();
                }

            }
        });

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




    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        //cameraView.start();
        super.onResume();
        cameraView.start();
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
            //showFileChooser();

        } else {
            txtStatus.setText("Uploading Started...");
            uploadImage();
        }
    }
}