package com.test1.kotlinfiles;
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.sj.camera_lib_android.utils.CameraSDK
import org.json.JSONObject

class CameraModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName() = "CameraModule"

    @ReactMethod
    fun openCam() {
        CameraSDK.init(reactApplicationContext, "gs://shelfwatch-app-dev")
         val uploadParams = JSONObject("""
                        {
                            "shop_id": 1288,
                            "project_id": "79b7c955-f253-4f19-b1ab-710585614dd4",
                            "td_version_id": 178,
                            "shelf_image_id": null,
                            "asset_image_id": null,
                            "shelf_type": "Main Aisle",
                            "category_id": 309,
                            "user_id": 133,
                            "isConnected": true,
                            "sn_image_type": "skus",
                            "image_type": "single",
                            "seq_no": 1,
                            "level": 1,
                            "last_image_flag": 1,
                            "uploadOnlyOnWifi": 0,
                            "app_session_id": "8e2faa6b-d6fe-413a-a693-76a0cbe0ce71"
                        }
                        """)
        CameraSDK.startCamera(
            context = reactApplicationContext,
            orientation = "portrait",
            widthPercentage = 50,
            uploadParams = uploadParams,
            allowCrop = true,
            allowBlurCheck = true,
            referenceUrl = "",
            resolution = 2048,
            uploadFrom = "Shelfwatch"
        )
    }
}