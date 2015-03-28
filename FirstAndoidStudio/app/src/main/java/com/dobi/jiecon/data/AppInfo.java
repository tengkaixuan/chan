package com.dobi.jiecon.data;

import android.graphics.drawable.Drawable;

import com.dobi.jiecon.utils.IName;
public class AppInfo implements IName{
	private String name;
	private String type;
	private Drawable icon;
	public AppInfo(String name, Drawable icon){
		this.name = name;
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String appName) {
		this.name = appName;
	}
	public String getAppType() {
		return type;
	}
	public void setAppType(String appType) {
		this.type = appType;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
}
