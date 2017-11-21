/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bnrc.model;

// TODO: Auto-generated Javadoc

/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bMenuItem.java 
 * 鎻忚堪锛氳彍鍗曞疄浣�
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-11-13 涓婂崍9:00:52
 */
public class AbMenuItem {
	
	/** 鑿滃崟鐨刬d. */
	private int id;

	/** 鑿滃崟鐨勫浘鏍噄d. */
	private int iconId;

	/** 鑿滃崟鐨勬枃鏈�. */
	private String text;
	
	/** 鑿滃崟鐨勬弿杩�. */
	private String mark;
	

	/**
	 * Instantiates a new ab menu item.
	 *
	 * @param id the id
	 * @param text the text
	 */
	public AbMenuItem(int id, String text) {
		super();
		this.id = id;
		this.text = text;
	}
	
	/**
	 * Instantiates a new ab menu item.
	 *
	 * @param text the text
	 */
	public AbMenuItem(String text) {
		super();
		this.text = text;
	}
	
	/**
	 * Instantiates a new ab menu item.
	 *
	 */
	public AbMenuItem() {
		super();
	}
	

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the icon id.
	 *
	 * @return the icon id
	 */
	public int getIconId() {
		return iconId;
	}

	/**
	 * Sets the icon id.
	 *
	 * @param iconId the new icon id
	 */
	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the mark.
	 *
	 * @return the mark
	 */
	public String getMark() {
		return mark;
	}

	/**
	 * Sets the mark.
	 *
	 * @param mark the new mark
	 */
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	

}
