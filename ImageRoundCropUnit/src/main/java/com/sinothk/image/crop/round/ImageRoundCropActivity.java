package com.sinothk.image.crop.round;

import android.app.Activity;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


public class ImageRoundCropActivity extends AppCompatActivity implements View.OnClickListener {


    public static String KEY_IMAGE_PATH = "KEY_IMAGE_PATH";
    public static int CODE_RESULT = 100;

    private ImageRoundCropView wrhImageView;
//    private Button btnClip;

    public static void start(Activity mActivity, String imgPath, int requestCode) {
        Intent intent = new Intent(mActivity, ImageRoundCropActivity.class);
        intent.putExtra("imgPath", imgPath);
        mActivity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_round_crop);

//        btnClip = (Button) findViewById(R.id.btn_clip);
//        btnClip.setOnClickListener(this);

        wrhImageView = (ImageRoundCropView) findViewById(R.id.cropView);

//        wrhImageView.setImageResource(imgId);
        String oldBitmapPath = getIntent().getStringExtra("imgPath");
        wrhImageView.setImageBitmap(BitmapFactory.decodeFile(oldBitmapPath));

        // 按钮事件
        findViewById(R.id.titleBarLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.completeTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok();
            }
        });
    }

    @Override
    public void onClick(View v) {
//        ok();
    }

    private void ok() {
        wrhImageView.setVisibility(View.VISIBLE);
        Bitmap zoomedCropBitmap = wrhImageView.clipBitmap();

        // 保存图片
        if (zoomedCropBitmap == null) {
            Log.e("android", "zoomedCropBitmap == null");
            return;
        }
        Uri mSaveUri = Uri.fromFile(new File(getCacheDir(), "cropped_" + System.currentTimeMillis() + ".png"));
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(mSaveUri);
                if (outputStream != null) {
                    zoomedCropBitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
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
            Intent intent = new Intent();
            intent.setData(mSaveUri);
            intent.putExtra(KEY_IMAGE_PATH, mSaveUri.getPath());
            setResult(CODE_RESULT, intent);
            finish();
        }
    }
}
