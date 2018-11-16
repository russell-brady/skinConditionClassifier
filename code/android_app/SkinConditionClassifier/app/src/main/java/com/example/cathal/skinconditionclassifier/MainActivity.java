package com.example.cathal.skinconditionclassifier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button moleButton = (Button) findViewById(R.id.uploadButton);
        Button skinButton = (Button) findViewById(R.id.cameraButton);

        moleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMoleClassfificationPage();
            }
        });

        skinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSkinDiseaseClassificationPage();
            }
        });


    }

    private void goToMoleClassfificationPage() {
        Intent intent = new Intent(MainActivity.this, UploadActivity.class);
        startActivity(intent);
    }

    private void goToSkinDiseaseClassificationPage() {
        Intent intent = new Intent(MainActivity.this, SkinActivity.class);
        startActivity(intent);
    }


}
