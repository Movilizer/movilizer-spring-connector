package com.movilizer.connector.bpost;

import java.util.ArrayList;
import java.util.List;

public class MoveletListAssignment {

	private String deviceAddress;

	private List<String> moveletKeys = new ArrayList<String>();

	public String getDeviceAddress() {
		return deviceAddress;
	}

	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	public List<String> getMoveletKeys() {
		return moveletKeys;
	}

	public void setMoveletKeys(List<String> moveletKeys) {
		this.moveletKeys = moveletKeys;
	}

}
