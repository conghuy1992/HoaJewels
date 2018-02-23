package com.hoa.jewels;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.zxing.Result;
import com.hoa.jewels._class.Const;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by maidinh on 5/29/2017.
 */

public class ScannerActivity extends AppCompatActivity implements View.OnClickListener, ZXingScannerView.ResultHandler {
    private String TAG = "ScannerActivity";
    private FrameLayout frLogout;
    private ZXingScannerView mScannerView;
    private FrameLayout layoutCamera;
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_layout);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

//        int rc = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
//        if (rc == PackageManager.PERMISSION_GRANTED) {
//            initView();
//        } else {
//            requestCameraPermission();
//        }

        if (Build.VERSION.SDK_INT > 22) {
            if (checkPermissions()) {
                startApplication();
            } else {
                setPermissions();
            }
        } else {
            startApplication();
        }

    }
    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
    private final int MY_PERMISSIONS_REQUEST_CODE = 1;
    private void setPermissions() {
        String[] requestPermission = new String[]{
                Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(this, requestPermission, MY_PERMISSIONS_REQUEST_CODE);
    }


    // Handles the requesting of the camera permission.
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{android.Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
        }
    }

    void startApplication() {
        layoutCamera = (FrameLayout) findViewById(R.id.layoutCamera);
        frLogout = (FrameLayout) findViewById(R.id.frLogout);
        frLogout.setOnClickListener(this);

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera
        layoutCamera.addView(mScannerView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSIONS_REQUEST_CODE) {
            return;
        }

        boolean isGranted = true;

        // 결과값을 체크합니다. 한개라도 실패하면 앱을 실행하지 않습니다.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }
        }

        // 결과 체크에 따른 처리
        if (isGranted) {
            startApplication();
        } else {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Multitracker sample")
                    .setMessage(R.string.no_camera_permission)
                    .setPositiveButton(R.string.ok, listener)
                    .show();
        }
    }



    @Override
    public void onBackPressed() {
        onBack();
    }

    void onBack() {
        Intent intent = new Intent();
        setResult(Const.ON_BACK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == frLogout) {
            onBack();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mScannerView != null) {
            mScannerView.stopCamera();           // Stop camera on pause
        }

    }

    @Override
    public void handleResult(Result result) {
        // Do something with the result here

//        Log.d(TAG, result.getText()); // Prints scan results
//        Log.d(TAG, result.getBarcodeFormat().toString()); // Prints the scan format (qrcode)

        String barCode = result.getText();
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_BARCODE, barCode);
        setResult(CommonStatusCodes.SUCCESS, intent);
        finish();

    }
}
