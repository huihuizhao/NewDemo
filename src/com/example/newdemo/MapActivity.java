package com.example.newdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.newdemo.util.MyLog;
import com.example.newdemo.util.MyToast;

public class MapActivity extends Activity implements OnClickListener {
	//系统控件
	private Button request_loc;
	
	//定位相关
	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true;//是否首次定位
	
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	
//	private BDLocation location;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(this.getApplicationContext());  
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map);
		
		initBaiduMap();
		initView();
	}

	private void initBaiduMap() {
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		//普通地图  
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);  
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);//打开gps
		option.setCoorType("bd09ll"); //设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	private void initView() {
		//标题栏
		RelativeLayout titleBar = (RelativeLayout)findViewById(R.id.layout_titlebar);
		TextView center_tx = (TextView)titleBar.findViewById(R.id.tv_titlebar_title);
		center_tx.setText("地图");
		Button rightBtn = (Button)titleBar.findViewById(R.id.btn_titlebar_right);
		rightBtn.setText("汇报");
		((Button)titleBar.findViewById(R.id.btn_titlebar_back)).setVisibility(View.GONE);
		
		request_loc = (Button)findViewById(R.id.request);
		request_loc.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_titlebar_right:
			Intent intent = new Intent(MapActivity.this, UploadActivity.class);
			startActivity(intent);
			break;
		case R.id.request:
			//GPS没开启，跳转到开启界面
//			manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//			if (!(manager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))) {
//				startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
//			}
			requestLoc();
			break;
		default:
			break;
		}
	}
	//手动请求定位的方法
	private void requestLoc() {
		if (mLocClient != null && mLocClient.isStarted()) {
			MyToast.showToast(this, "正在定位...");
			mLocClient.requestLocation();
//			LatLng ll = new LatLng(location.getLatitude(),
//					location.getLongitude());
//			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//			mBaiduMap.animateMapStatus(u);
		} else {
			MyLog.d("locClient is null or not started");
		}
	}
	
	//定位接口
	private class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
//			MapActivity.this.location = location;
			//mapView 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					//此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			//如果是首次定位，把位置移到中心（自动移动），不是首次的话下次不会移到中心了，在这里可以更改设置
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog isExit = new AlertDialog.Builder(MapActivity.this).setTitle("温馨提示：").
					setMessage("确定要退出吗？").setPositiveButton("确定", listener).setNegativeButton("取消", listener).create();
			isExit.show();
			isExit.setCancelable(false);
		}
		return false;
	}
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				stopBaiduMap();
				MapActivity.this.finish();
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onPause() {
		super.onPause();
		if (mMapView != null) {
			mMapView.onPause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mMapView != null) {
			mMapView.onResume();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopBaiduMap();
	}
	
	private void stopBaiduMap() {
		if (mBaiduMap != null) {
			//关闭定位图层
//			mBaiduMap.setMyLocationEnabled(false);
			mBaiduMap = null;
		}
		if (mLocClient != null) {
			//退出时销毁定位
			mLocClient.stop();
		}
		if (mMapView != null) {
			mMapView.onDestroy();
			mMapView = null;
		}
	}
}