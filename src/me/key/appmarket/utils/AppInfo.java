package me.key.appmarket.utils;

import android.graphics.drawable.Drawable;

public class AppInfo {

	private String idx;
	private String appName;
	private String appSize;
	private String iconUrl;
	private String appDescri;
	private String appUrl;
	private String appDownCount;

	private boolean isInstalled;

	private String version;
	private String packageName;
	private Drawable appIcon = null;
	private String Root;
	private String apkName;
	private boolean ispause;
	private boolean isDown;
	public boolean isDown() {
		return isDown;
	}

	public void setDown(boolean isDown) {
		this.isDown = isDown;
	}

	public boolean isIspause() {
		return ispause;
	}

	public void setIspause(boolean ispause) {
		this.ispause = ispause;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public String getRoot() {
		return Root;
	}

	public void setRoot(String root) {
		Root = root;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isInstalled() {
		return isInstalled;
	}

	public void setInstalled(boolean isInstalled) {
		this.isInstalled = isInstalled;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getAppDownCount() {
		return appDownCount;
	}

	public void setAppDownCount(String appDownCount) {
		this.appDownCount = appDownCount;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppSize() {
		return appSize;
	}

	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}

	public AppInfo(String idx, String appName, String appSize, String iconUrl,
			String appUrl, String appDownCount) {
		super();
		this.appName = appName;
		this.appSize = appSize;
		this.iconUrl = iconUrl;
		this.appUrl = appUrl;
		this.idx = idx;
		this.appDownCount = appDownCount;
	}

	public String getAppDescri() {
		return appDescri;
	}

	public void setAppDescri(String appDescri) {
		this.appDescri = appDescri;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	@Override
	public String toString() {
		return "AppInfo [idx= " + idx + ", appName=" + appName + ", appSize="
				+ appSize + ", iconUrl=" + iconUrl + ", appDescri=" + appDescri
				+ ", appUrl=" + appUrl + "]";
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public AppInfo() {
		super();
	}
}
