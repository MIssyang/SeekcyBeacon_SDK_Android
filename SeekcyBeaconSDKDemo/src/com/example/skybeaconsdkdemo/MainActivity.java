package com.example.skybeaconsdkdemo;

import java.util.ArrayList;
import java.util.List;

import com.seekcy.decryption.DecryptProcess;
import com.skybeacon.sdk.RangingBeaconsListener;
import com.skybeacon.sdk.ScanServiceStateCallback;
import com.skybeacon.sdk.locate.SKYBeacon;
import com.skybeacon.sdk.locate.SKYBeaconManager;
import com.skybeacon.sdk.locate.SKYBeaconMultiIDs;
import com.skybeacon.sdk.locate.SKYRegion;
import com.skybeacon.sdk.utils.DataConvertUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {
	private final int UPDATE_LIST_VIEW = 1;
	private SharedPreferences mPreferences;
	@SuppressWarnings("unused")
	private EditText mPasscode;
	@SuppressWarnings("unused")
	private TextView mHintView;
	private String passcodeStr;
	private EditText encryptKey;
	private Spinner spinnerKey;
	private List<String> mKeyList = null;
	private boolean firstIn = true;

	private SKYBeaconManager skyBeaconManager;
	// listview
	private ListView listView;
	private LeDeviceListAdapter leDeviceListAdapter;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case UPDATE_LIST_VIEW:
				leDeviceListAdapter.addDevice((iBeaconView) msg.obj);
				leDeviceListAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mPreferences = getSharedPreferences("seekcyBeaconSDKDemo", MODE_PRIVATE);
		passcodeStr = mPreferences.getString("ENCRYPT_KEY", "");
		encryptKey = (EditText) findViewById(R.id.config_encrypt_key);
		encryptKey.setText(passcodeStr);
		skyBeaconManager = new SKYBeaconManager(this);
		if (passcodeStr != null && !passcodeStr.equals("")) {
			skyBeaconManager.setBroadcastKey(passcodeStr);
		}
		encryptKey.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if (arg0.length() == 64 || arg0.length() == 0) {
					// 锟斤拷锟絊haredPreferences 锟斤拷Editor锟斤拷锟斤拷
					SharedPreferences.Editor editor = mPreferences.edit();
					// 锟睫革拷锟斤拷锟�
					editor.putString("ENCRYPT_KEY",
							String.valueOf(encryptKey.getText()));
					editor.commit();
					DecryptProcess.InitPublic(DataConvertUtils
							.hexStringToByte(arg0.toString()));
				}
			}
		});
		mKeyList = new ArrayList<String>();
		mKeyList.add("");
		mKeyList.add("");
		mKeyList.add("A5B5C146ADA7291E7FF5579539C04181B2E3F58C232641D741D03EED5932409D");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.spinner_broadcast_id, R.id.spinner_broadcast_id_view,
				mKeyList);
		spinnerKey = (Spinner) findViewById(R.id.config_encrypt_key_spinner);
		spinnerKey.setAdapter(adapter);
		spinnerKey.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (!firstIn) {
					encryptKey.setText(mKeyList.get(arg2));
				} else {
					firstIn = false;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onStart();
		initListView();
		startRanging();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onStop();
		stopRanging();
	}

	private void initListView() {
		listView = (ListView) findViewById(R.id.listview_scan);
		leDeviceListAdapter = new LeDeviceListAdapter(this);
		listView.setAdapter(leDeviceListAdapter);
		// TODO ListAdapter锟斤拷notify锟斤拷锟铰憋拷锟斤拷锟斤拷锟饺挥帮拷锟斤拷锟斤拷录锟�
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ListView lview = (ListView) arg0;
				iBeaconView beacon = (iBeaconView) lview
						.getItemAtPosition(arg2);
				String deviceAddress = beacon.mac;

				if (beacon.isMultiIDs) {
					Intent intent = new Intent(MainActivity.this,
							ConfigMultiIDsActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("deviceAddress", deviceAddress);
					intent.putExtras(bundle);
					startActivity(intent);
				} else {
					Intent intent = new Intent(MainActivity.this,
							ConfigSingleIDActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("deviceAddress", deviceAddress);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
	}

	private void startRanging() {
		skyBeaconManager.startScanService(new ScanServiceStateCallback() {

			@Override
			public void onServiceDisconnected() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onServiceConnected() {
				// TODO Auto-generated method stub
				skyBeaconManager.startRangingBeacons(null);
			}
		});

		skyBeaconManager
				.setRangingBeaconsListener(new RangingBeaconsListener() {

					@Override
					public void onRangedBeaconsMultiIDs(SKYRegion beaconRegion,
							List<SKYBeaconMultiIDs> beaconMultiIDsList) {
						// TODO Auto-generated method stub
						for (int i = 0; i < beaconMultiIDsList.size(); i++) {
							iBeaconView beacon = new iBeaconView();
							beacon.mac = beaconMultiIDsList.get(i)
									.getDeviceAddress();
							beacon.isMultiIDs = true;
							beacon.detailInfo = "version: "
									+ String.valueOf(beaconMultiIDsList.get(i)
											.getHardwareVersion())
									+ "."
									+ String.valueOf(beaconMultiIDsList.get(i)
											.getFirmwareVersionMajor())
									+ "."
									+ String.valueOf(beaconMultiIDsList.get(i)
											.getFirmwareVersionMinor());
							beacon.detailInfo += "\r\n";
							for (int j = 0; j < beaconMultiIDsList.get(i)
									.getBeaconList().size(); j++) {
								beacon.detailInfo += beaconMultiIDsList.get(i)
										.getBeaconList().get(j)
										.getDeviceAddress()
										+ "\r\n";
							}
							Message msg = new Message();
							msg.obj = beacon;
							msg.what = UPDATE_LIST_VIEW;
							mHandler.sendMessage(msg);
						}
					}

					@Override
					public void onRangedBeacons(SKYRegion beaconRegion,
							List<SKYBeacon> beaconList) {
						// TODO Auto-generated method stub
						for (int i = 0; i < beaconList.size(); i++) {
							iBeaconView beacon = new iBeaconView();
							beacon.mac = beaconList.get(i).getDeviceAddress();
							beacon.isMultiIDs = false;
							beacon.detailInfo = "version: "
									+ String.valueOf(beaconList.get(i)
											.getHardwareVersion())
									+ "."
									+ String.valueOf(beaconList.get(i)
											.getFirmwareVersionMajor())
									+ "."
									+ String.valueOf(beaconList.get(i)
											.getFirmwareVersionMinor());
							Message msg = new Message();
							msg.obj = beacon;
							msg.what = UPDATE_LIST_VIEW;
							mHandler.sendMessage(msg);
						}
					}
				});
	}

	private void stopRanging() {
		if (skyBeaconManager != null) {
			skyBeaconManager.stopScanService();
			skyBeaconManager.stopRangingBeasons(null);
		}
	}
}
