package com.skybeacon.demo;

public class iBeaconView {
	public String mac = "";
	public String detailInfo = "";
	public boolean isMultiIDs = false;

	public void reset(iBeaconView beacon) {
		this.mac = beacon.mac;
		this.detailInfo = beacon.detailInfo;
		this.isMultiIDs = beacon.isMultiIDs;
	}
}
