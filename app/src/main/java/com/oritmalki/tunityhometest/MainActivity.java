package com.oritmalki.tunityhometest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public final static String MID_PIXEL_VALUES = "MID_PIXEL_VALUES";
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private Button takePic;
    private int[] mRgb;
    private boolean buttonPressedOnce = false;

    private ImageSurfaceView mImagesurfaceView;
    private Camera camera;
    private FrameLayout cameraPreviewLayout;
    private ImageView capturedImageHolder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        buttonPressedOnce = false;


        takePic = findViewById(R.id.collect_and_display_but);
        cameraPreviewLayout = findViewById(R.id.camera_preview);
        capturedImageHolder = findViewById(R.id.captureImageHolder);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return;
        }
        camera = checkDeviceCamera();
        camera.setDisplayOrientation(90);
        mImagesurfaceView = new ImageSurfaceView(MainActivity.this, camera);
        cameraPreviewLayout.addView(mImagesurfaceView);
                            camera.startPreview();


        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!buttonPressedOnce) {



                    camera.takePicture(null, null, pictureCallback);


                        buttonPressedOnce = true;

                } else {

                    buttonPressedOnce = false;
                    Intent showValuesIntent = new Intent(MainActivity.this, DetailsActivity.class);
                    showValuesIntent.putExtra(MID_PIXEL_VALUES, mRgb);
                    startActivity(showValuesIntent);

                }

            }
        });
    }

    private Camera  checkDeviceCamera(){
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCamera;
    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            if(bitmap==null){
                Toast.makeText(MainActivity.this, "Captured image is empty", Toast.LENGTH_LONG).show();
                return;
            }

            capturedImageHolder.setImageBitmap(bitmap);
            mRgb = getMidPixelValues();

        }
    };


    public int[] getMidPixelValues() {


        Bitmap bitmap = loadBitmapFromView(capturedImageHolder);

        int pixel = bitmap.getPixel(bitmap.getWidth()/2, bitmap.getHeight()/2);
        Toast.makeText(this, "Created one pixel bitamp: x." + bitmap.getWidth() /2 + " y." + bitmap.getHeight() / 2, Toast.LENGTH_SHORT).show();

        int r = Color.red(pixel);
        int g = Color.green(pixel);
        int b = Color.blue(pixel);
        int[] rgb = {r, g, b};
        Log.d(TAG, "RGB values collected: " + Arrays.toString(rgb));

        return rgb;

    }

    private Bitmap loadBitmapFromView(View view) {
        Bitmap b = convertImageViewToBitmap(capturedImageHolder);
        return b;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(MainActivity.this, "Sorry! you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }


    //function to convert imageView to Bitmap

    private Bitmap convertImageViewToBitmap(ImageView v){

          Bitmap bm = ((BitmapDrawable) v.getDrawable()).getBitmap();

        return bm;
    }
}




