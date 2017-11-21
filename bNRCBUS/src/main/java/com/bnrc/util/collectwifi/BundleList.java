package com.bnrc.util.collectwifi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BundleList implements Serializable {
	private List<Map<String, String>> list;

	public BundleList(List<Map<String, String>> list) {
		this.list = list;
	}

	public List<Map<String, String>> getList() {
		return list;
	}
}
