//package com.skybeacon.demo;
//
//import com.skybeacon.sdk.IndoorLocateListener;
//import com.skybeacon.sdk.locate.SKYLocateManager;
//import com.skybeacon.sdk.locate.SKYPosition;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.widget.TextView;
//
//@SuppressLint("HandlerLeak")
//public class LocateActivity extends Activity {
//	private static final int UPDATE_LOCATE_RESULT_VIEW = 1;
//	private static final int UPDATE_TEST_STRING_VIEW = 2;
//
//	TextView locateResult;
//	TextView testString;
//
//	Handler handler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case UPDATE_LOCATE_RESULT_VIEW:
//				locateResult.setText((String) msg.obj);
//				break;
//			case UPDATE_TEST_STRING_VIEW:
//				testString.setText((String) msg.obj);
//				break;
//
//			default:
//				break;
//			}
//		}
//
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_locate);
//		SKYLocateManager.getInstance().init(this);
//		SKYLocateManager.getInstance().setOnIndoorLocateListener(
//				indoorLocateListener);
//
//		initView();
//	}
//
//	private void initView() {
//		locateResult = (TextView) findViewById(R.id.locate_result);
//		testString = (TextView) findViewById(R.id.test_string);
//	}
//
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		SKYLocateManager.getInstance().stop();
//	}
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		SKYLocateManager.getInstance().start();
//	}
//
//	IndoorLocateListener indoorLocateListener = new IndoorLocateListener() {
//
//		@Override
//		public void onReceivePosition(SKYPosition position) {
//			// TODO Auto-generated method stub
//			String result = "Version: "
//					+ SKYLocateManager.getInstance().getVersion() + "\r\n";
//			result += "User id: " + position.getUserID() + "\r\n";
//			result += "Error code: " + String.valueOf(position.getErrorCode())
//					+ "\r\n";
//			result += "Build id: " + String.valueOf(position.getBuildID())
//					+ "\r\n";
//			result += "Floor id: " + String.valueOf(position.getFloorID())
//					+ "\r\n";
//			result += "X: " + String.valueOf(position.getxMillimeters())
//					+ "\r\n";
//			result += "Y: " + String.valueOf(position.getyMillimeters())
//					+ "\r\n";
//			result += "Accuracy: " + String.valueOf(position.getAccuracy())
//					+ "\r\n";
//			result += "Timestamp: "
//					+ String.valueOf(position.getTimeStampMillisecond())
//					+ "\r\n";
//			result += "Info: " + String.valueOf(position.getInfo()) + "\r\n";
//			Message msg = new Message();
//			msg.what = UPDATE_LOCATE_RESULT_VIEW;
//			msg.obj = result;
//			handler.sendMessage(msg);
//		}
//
//		@Override
//		public void onTestString(String test) {
//			// TODO Auto-generated method stub
//			Message msg = new Message();
//			msg.what = UPDATE_TEST_STRING_VIEW;
//			msg.obj = test;
//			handler.sendMessage(msg);
//		}
//	};
//}
