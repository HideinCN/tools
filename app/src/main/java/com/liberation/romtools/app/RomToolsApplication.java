package com.liberation.romtools.app;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;



/**
 * 项目名称： ROMS
 * 创建人 : Liberation
 * 创建时间: 2017/3/10 12:52
 */

public class RomToolsApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化应用信息
		AVOSCloud.initialize(this, "WFOLULSoEP3a37mLI703kyYz-gzGzoHsz", "ACH8HvqBwOhRqxV2NvDnjleI");
		// 启用崩溃错误统计
		AVAnalytics.enableCrashReport(this.getApplicationContext(), true);
		AVOSCloud.setLastModifyEnabled(true);
		AVOSCloud.setDebugLogEnabled(true);

	}
}
