package com.bnrc.ui.rtBus;

public class StationItem {
	public int getLineID() {
		return LineID;
	}
	public void setLineID(int lineID) {
		LineID = lineID;
	}
	public int getStationID() {
		return StationID;
	}
	public void setStationID(int stationID) {
		StationID = stationID;
	}
	public int getSequence() {
		return Sequence;
	}
	public void setSequence(int sequence) {
		Sequence = sequence;
	}
	public String getStationName() {
		return StationName;
	}
	public void setStationName(String stationName) {
		StationName = stationName;
	}
	public String getStartStation() {
		return StartStation;
	}
	public void setStartStation(String startStation) {
		StartStation = startStation;
	}
	public String getEndStation() {
		return EndStation;
	}
	public void setEndStation(String endStation) {
		EndStation = endStation;
	}
	public int getAzimuth() {
		return Azimuth;
	}
	public void setAzimuth(int azimuth) {
		Azimuth = azimuth;
	}
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	public double getLatitude() {
		return Latitude;
	}
	public void setLatitude(double latitude) {
		Latitude = latitude;
	}
	public double getLongitude() {
		return Longitude;
	}
	public void setLongitude(double longitude) {
		Longitude = longitude;
	}
	private int LineID;
	private int StationID;
	private int Sequence;
	private String StationName;
	private String StartStation;
	private String EndStation;
	private int Azimuth;
	private int Type;
	private double Latitude;
	private double Longitude;
}
