package com.bnrc.ui.listviewswipedelete;

import java.io.Serializable;
import java.util.ArrayList;

public class BlogItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1000L;
	private String station;
	private String alertState;

	public BlogItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BlogItem(String station, String alertState) {
		super();
		this.station = station;
		this.alertState = alertState;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getAlertState() {
		return alertState;
	}

	public void setAlertState(String alertState) {
		this.alertState = alertState;
	}

}
