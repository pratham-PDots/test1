package com.test1;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.sj.camera_lib_android.utils.CameraSDK;


public class CameraModule extends ReactContextBaseJavaModule {
    CameraModule(ReactApplicationContext context) {
        super(context);
    }

    @NonNull
    @Override
    public String getName() {
        return "CameraModule";
    }

    public void openCam() {

    }
}