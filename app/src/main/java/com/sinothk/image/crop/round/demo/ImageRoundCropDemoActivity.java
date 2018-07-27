package com.sinothk.image.crop.round.demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sinothk.image.crop.round.FileUtil;
import com.sinothk.image.crop.round.ImageRoundCropActivity;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


public class ImageRoundCropDemoActivity extends AppCompatActivity {

    ImageView imageView;
    Button btnClip;

    String oldImagPath = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_round_crop_demo);

        imageView = (ImageView) findViewById(R.id.img2);
        btnClip = (Button) findViewById(R.id.btn_clip);

        btnClip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageRoundCropActivity.start(ImageRoundCropDemoActivity.this, oldImagPath, 199);
            }
        });


        Bitmap oldBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.yinhun);
        Uri mSaveUri = Uri.fromFile(new File(getCacheDir(), "crop_" + System.currentTimeMillis() + ".png"));
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(mSaveUri);
                if (outputStream != null) {
                    oldBitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
                }
            } catch (IOException ex) {
                Log.e("android", "Cannot open file: " + mSaveUri, ex);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            oldImagPath = mSaveUri.getPath();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 199 && resultCode == ImageRoundCropActivity.CODE_RESULT) {
            if (data != null) {
                String path = data.getStringExtra(ImageRoundCropActivity.KEY_IMAGE_PATH);

                Bitmap bitmap = BitmapFactory.decodeFile(path);

                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
