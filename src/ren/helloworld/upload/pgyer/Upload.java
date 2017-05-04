package ren.helloworld.upload.pgyer;

/**
 * Created by dafan on 2017/5/4 0004.
 */
public class Upload {
	
	/**
	 * code : 0
	 * message :
	 * data : {"appKey":"052171abdd35af4a26f9071f6b893908","userKey":"84e1019fb01536f66e4c62c910879159","appType":"2","appIsLastest":"1","appFileSize":"1947841","appName":"Address-sample","appVersion":"1.0","appVersionNo":"1","appBuildVersion":"5","appIdentifier":"ren.helloworld.address_sample","appIcon":"e1bb991d7bbc6c2c3ab164db89490480","appDescription":"","appUpdateDescription":"","appScreenshots":"","appShortcutUrl":"8fMv","appCreated":"2017-05-04 20:52:00","appUpdated":"2017-05-04 20:52:00","appQRCodeURL":"https://static.pgyer.com/app/qrcodeHistory/eef17314c3e03426d737a21802a985c86fd26b35dd3aa8556be634925eed8118"}
	 */
	
	private int code;
	private String message;
	private DataBean data;
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public DataBean getData() {
		return data;
	}
	
	public void setData(DataBean data) {
		this.data = data;
	}
	
	public static class DataBean {
		/**
		 * appKey : 052171abdd35af4a26f9071f6b893908
		 * userKey : 84e1019fb01536f66e4c62c910879159
		 * appType : 2
		 * appIsLastest : 1
		 * appFileSize : 1947841
		 * appName : Address-sample
		 * appVersion : 1.0
		 * appVersionNo : 1
		 * appBuildVersion : 5
		 * appIdentifier : ren.helloworld.address_sample
		 * appIcon : e1bb991d7bbc6c2c3ab164db89490480
		 * appDescription :
		 * appUpdateDescription :
		 * appScreenshots :
		 * appShortcutUrl : 8fMv
		 * appCreated : 2017-05-04 20:52:00
		 * appUpdated : 2017-05-04 20:52:00
		 * appQRCodeURL : https://static.pgyer.com/app/qrcodeHistory/eef17314c3e03426d737a21802a985c86fd26b35dd3aa8556be634925eed8118
		 */
		
		private String appKey;
		private String userKey;
		private String appType;
		private String appIsLastest;
		private String appFileSize;
		private String appName;
		private String appVersion;
		private String appVersionNo;
		private String appBuildVersion;
		private String appIdentifier;
		private String appIcon;
		private String appDescription;
		private String appUpdateDescription;
		private String appScreenshots;
		private String appShortcutUrl;
		private String appCreated;
		private String appUpdated;
		private String appQRCodeURL;
		
		public String getAppKey() {
			return appKey;
		}
		
		public void setAppKey(String appKey) {
			this.appKey = appKey;
		}
		
		public String getUserKey() {
			return userKey;
		}
		
		public void setUserKey(String userKey) {
			this.userKey = userKey;
		}
		
		public String getAppType() {
			return appType;
		}
		
		public void setAppType(String appType) {
			this.appType = appType;
		}
		
		public String getAppIsLastest() {
			return appIsLastest;
		}
		
		public void setAppIsLastest(String appIsLastest) {
			this.appIsLastest = appIsLastest;
		}
		
		public String getAppFileSize() {
			return appFileSize;
		}
		
		public void setAppFileSize(String appFileSize) {
			this.appFileSize = appFileSize;
		}
		
		public String getAppName() {
			return appName;
		}
		
		public void setAppName(String appName) {
			this.appName = appName;
		}
		
		public String getAppVersion() {
			return appVersion;
		}
		
		public void setAppVersion(String appVersion) {
			this.appVersion = appVersion;
		}
		
		public String getAppVersionNo() {
			return appVersionNo;
		}
		
		public void setAppVersionNo(String appVersionNo) {
			this.appVersionNo = appVersionNo;
		}
		
		public String getAppBuildVersion() {
			return appBuildVersion;
		}
		
		public void setAppBuildVersion(String appBuildVersion) {
			this.appBuildVersion = appBuildVersion;
		}
		
		public String getAppIdentifier() {
			return appIdentifier;
		}
		
		public void setAppIdentifier(String appIdentifier) {
			this.appIdentifier = appIdentifier;
		}
		
		public String getAppIcon() {
			return appIcon;
		}
		
		public void setAppIcon(String appIcon) {
			this.appIcon = appIcon;
		}
		
		public String getAppDescription() {
			return appDescription;
		}
		
		public void setAppDescription(String appDescription) {
			this.appDescription = appDescription;
		}
		
		public String getAppUpdateDescription() {
			return appUpdateDescription;
		}
		
		public void setAppUpdateDescription(String appUpdateDescription) {
			this.appUpdateDescription = appUpdateDescription;
		}
		
		public String getAppScreenshots() {
			return appScreenshots;
		}
		
		public void setAppScreenshots(String appScreenshots) {
			this.appScreenshots = appScreenshots;
		}
		
		public String getAppShortcutUrl() {
			return appShortcutUrl;
		}
		
		public void setAppShortcutUrl(String appShortcutUrl) {
			this.appShortcutUrl = appShortcutUrl;
		}
		
		public String getAppCreated() {
			return appCreated;
		}
		
		public void setAppCreated(String appCreated) {
			this.appCreated = appCreated;
		}
		
		public String getAppUpdated() {
			return appUpdated;
		}
		
		public void setAppUpdated(String appUpdated) {
			this.appUpdated = appUpdated;
		}
		
		public String getAppQRCodeURL() {
			return appQRCodeURL;
		}
		
		public void setAppQRCodeURL(String appQRCodeURL) {
			this.appQRCodeURL = appQRCodeURL;
		}
	}
}
