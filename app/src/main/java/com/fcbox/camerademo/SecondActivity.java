package com.fcbox.camerademo;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.IOException;
import java.util.List;

public class SecondActivity extends Activity implements SurfaceHolder.Callback{
	int CAMERA_REQUEST_CODE =1000;
	Camera mCamera;
	int cameraId;
	Camera.Parameters param;
	String TAG = "XL---TEST";

	private ArrayAdapter<String> arr_adapter;
	Spinner spinner;
	private List<String> data_list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);

	}

	private void setCameraDisplayOrientation() {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
			case Surface.ROTATION_0: degrees = 0; break;
			case Surface.ROTATION_90: degrees = 90; break;
			case Surface.ROTATION_180: degrees = 180; break;
			case Surface.ROTATION_270: degrees = 270; break;
		}
		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;  // compensate the mirror
		} else {  // back-facing
			result = (info.orientation - degrees) % 360;
		}
		mCamera.setDisplayOrientation(result);
	}

	private void initCamera() {
		int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
		if(null == mCamera){
			mCamera = Camera.open(cameraId);
		}
		setCameraDisplayOrientation();
		param = mCamera.getParameters();
		param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		param.setPictureFormat(ImageFormat.JPEG);
		param.setJpegQuality(100);

		param.setPreviewSize(param.getSupportedPreviewSizes().get(0).width,param.getSupportedPreviewSizes().get(0).height);
		Log.e("xl---test","param.getSupportedPreviewSizes():"+param.getSupportedPreviewSizes().get(0).width+"X"+param.getSupportedPreviewSizes().get(0).height);
		param.setPictureSize(param.getSupportedPictureSizes().get(0).width,param.getSupportedPictureSizes().get(0).height);
		mCamera.setParameters(param);
	}



	private void setFullScreen() {
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		getWindow().setFlags(flag,flag);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}
}
