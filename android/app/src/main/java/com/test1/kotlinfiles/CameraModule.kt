package com.test1.kotlinfiles;
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.sj.camera_lib_android.utils.CameraSDK
import org.json.JSONObject
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.sj.camera_lib_android.Database.ReactPendingData
import java.util.ArrayList

class CameraModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName() = "CameraModule"

    private lateinit var mLocalBroadcastReceiver: LocalBroadcastReceiver


    init {
        mLocalBroadcastReceiver = LocalBroadcastReceiver()
        val localBroadcastManager = LocalBroadcastManager.getInstance(reactApplicationContext)
        localBroadcastManager.registerReceiver(
            mLocalBroadcastReceiver,
            IntentFilter("did-receive-queue-data")
        )
    }

    inner class LocalBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if(intent.action?.equals("did-receive-queue-data") == true) {
                val imageList = intent.getParcelableArrayListExtra<ReactPendingData>("imageList")
                imageList?.let { images -> sendEvent("did-receive-queue-data", images) }
            }
        }
    }


    private fun sendEvent(eventName: String, params: ArrayList<ReactPendingData>) {
        val reactContext = reactApplicationContext as ReactContext
        val writableArray = Arguments.createArray()

        for (reactPendingData in params) {
            val writableMap = Arguments.createMap()
            writableMap.putString("session_id", reactPendingData.session_id)

            val writableArrayImages = Arguments.createArray()
            for (reactPendingImage in reactPendingData.images) {
                val writableMapImage = Arguments.createMap()
                writableMapImage.putString("uri", reactPendingImage.uri)
                writableMapImage.putBoolean("upload_status", reactPendingImage.upload_status)
                writableMapImage.putString("error", reactPendingImage.error)
                writableArrayImages.pushMap(writableMapImage)
            }

            writableMap.putArray("images", writableArrayImages)
            writableArray.pushMap(writableMap)
        }

        Log.d("imageSW emit", writableArray.toString())

        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, writableArray)
    }

    @ReactMethod
    fun initSDK(bucket: String) {
        CameraSDK.init(reactApplicationContext, bucket)
    }

    @ReactMethod
    fun openCam(orientation: String,
                widthPercentage: String,
                uploadParams: String,
                allowCrop: Boolean,
                allowBlurCheck: Boolean,
                referenceUrl: String,
                resolution: String,
                uploadFrom: String,
                isRetake: Boolean = false,
                zoomLevel: Double = 1.0) {
        CameraSDK.startCamera(
            context = reactApplicationContext,
            orientation = orientation,
            widthPercentage = widthPercentage.toInt(),
            uploadParams = JSONObject(uploadParams),
            allowCrop = allowCrop,
            allowBlurCheck = allowBlurCheck,
            referenceUrl = referenceUrl,
            resolution = resolution.toInt(),
            uploadFrom = uploadFrom,
            isRetake = isRetake,
            zoomLevel = zoomLevel
        )
    }

    private var listenerCount = 0

    @ReactMethod
    fun addListener(eventName: String) {
        if (listenerCount == 0) {
            // Set up any upstream listeners or background tasks as necessary
        }

        listenerCount += 1
    }

    @ReactMethod
    fun removeListeners(count: Int) {
        listenerCount -= count
        if (listenerCount == 0) {
            // Remove upstream listeners, stop unnecessary background tasks
        }
    }
}