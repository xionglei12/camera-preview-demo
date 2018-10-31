package com.fcbox.camerademo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity implements SurfaceHolder.Callback{
	int CAMERA_REQUEST_CODE =1000;
	Camera mCamera;
	int cameraId;
	Camera.Parameters param;
	String TAG = "XL---TEST";

	private ArrayAdapter<String> arr_adapter;
	Spinner spinner;
	private List<String> data_list;

	/**
	 * 利用SurfaceView进行相机内容展示
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				setFullScreen();
		setContentView(R.layout.activity_main);
		SurfaceView surfaceView = findViewById(R.id.sv_camera);

		/**
		 * 设置屏幕宽高比是4：3,也就是设置surfaceView
		 */
	/*	WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int width = windowManager.getDefaultDisplay().getWidth();
		int height = windowManager.getDefaultDisplay().getHeight();
		ViewGroup.LayoutParams layoutParams =  surfaceView.getLayoutParams();
		Log.i(TAG,"befor->width:"+width+"height:"+height);
		Log.i(TAG,"befor->width:"+layoutParams.width+"height:"+layoutParams.height);
		layoutParams.width = width;
		layoutParams.height = height;
		surfaceView.setLayoutParams(layoutParams);
		ViewGroup.LayoutParams afterLayoutParams =  surfaceView.getLayoutParams();
		Log.i(TAG,"after->width:"+afterLayoutParams.width+"height:"+afterLayoutParams.height);*/

		SurfaceHolder holder = surfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.CAMERA)
					!= PackageManager.PERMISSION_GRANTED) {
				// 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
				// 向用户解释为什么需要这个权限
				if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
					new AlertDialog.Builder(this)
							.setMessage("申请相机权限")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//申请相机权限
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
										requestPermissions(
												new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
									}
								}
							})
							.show();
				} else {
					//申请相机权限
					requestPermissions(
							new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
				}
			} else {
				Toast.makeText(this,"动态权限已经申请",Toast.LENGTH_SHORT).show();

					SurfaceHolder holder1 = surfaceView.getHolder();
					holder1.addCallback(this);
					holder1.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
					initCamera();

			}
		}

	}

	private void setCameraDisplayOrientation() {
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
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
	/*	Log.d("xl---test:","info.orientation:"+info.orientation+"rotation:"+rotation+"result:"+result);
		Toast.makeText(this,"info.orientation:"+info.orientation+"rotation:"+rotation+"result:"+result,Toast.LENGTH_SHORT).show();*/
		Log.d("xl---test:","info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT:"+(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT));
		mCamera.setDisplayOrientation(result);
	}

	private void initCamera() {
		int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
		if(null == mCamera){
			mCamera = Camera.open(cameraId);
		}

		setCameraDisplayOrientation();
//		mCamera.setDisplayOrientation(90);
		param = mCamera.getParameters();
		param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		param.setPictureFormat(ImageFormat.JPEG);
		param.setJpegQuality(100);

		param.setPreviewSize(param.getSupportedPreviewSizes().get(0).width,param.getSupportedPreviewSizes().get(0).height);
		Log.e("xl---test","param.getSupportedPreviewSizes():"+param.getSupportedPreviewSizes().get(0).width+"X"+param.getSupportedPreviewSizes().get(0).height);
		param.setPictureSize(param.getSupportedPictureSizes().get(0).width,param.getSupportedPictureSizes().get(0).height);
		/*try{
			mCamera.setParameters(param);
		}catch (Exception e){
			param.setPictureSize(1920, 1080);
			mCamera.setParameters(param);
		}*/
	}



	private void setFullScreen() {
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		getWindow().setFlags(flag,flag);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if(null == mCamera){
				initCamera();
			}
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


	/**
	 * 安卓只能保证一个应用占用相机，如果
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mCamera.release();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
