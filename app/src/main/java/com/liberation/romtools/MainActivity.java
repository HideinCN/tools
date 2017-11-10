package com.liberation.romtools;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.liberation.romtools.adapter.HomePagerAdapter;
import com.liberation.romtools.base.BaseFragment;
import com.liberation.romtools.utils.FileUtils;
import com.liberation.romtools.utils.NetWorkUtils;
import com.liberation.romtools.utils.ShellUtils.ShellUtils;
import com.liberation.romtools.utils.TabLayoutUtils;
import com.liberation.romtools.view.fragment.AboutMeFragment;
import com.liberation.romtools.view.fragment.ToolFragment;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.orhanobut.logger.Logger;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	@BindView(R.id.tabs)
	TabLayout mTabs;
	@BindView(R.id.vp_view)
	ViewPager mVpView;
	private List<String> mTitle=new ArrayList<>();
	private List<BaseFragment> mFragment=new ArrayList<>();
	private ArrayList<String> commnandList;
	private int flag;
	private String DISPLAY;
	private View sake;
	private String url;
	private String log;
	private String installId="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(!NetWorkUtils.isNetworkConnected(this)){
			Toast.makeText(this,"当前网络不可用",Toast.LENGTH_LONG).show();
			finish();
		}
		ButterKnife.bind(this);
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		initTitleBar();
		initBroadcast();
		initData();
	}

	private void initData() {


		FileUtils.CopyAssets(this,"pay",Environment.getExternalStorageDirectory()+"/pay");

		commnandList=new ArrayList<>();
		AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
			public void done(AVException e) {
				if (e == null) {
					// 保存成功
					installId= AVInstallation.getCurrentInstallation().getInstallationId();
					// 关联  installationId 到用户表等操作……
				} else {
					// 保存失败，输出错误信息
				}
			}
		});
	}

	private void initBroadcast() {
		// 设置默认打开的 Activity
		PushService.setDefaultPushCallback(this, MainActivity.class);
		// 订阅频道，当该频道消息到来的时候，打开对应的 Activity
		/*PushService.subscribe(this, "public", PushDemo.class);
		PushService.subscribe(this, "private", Callback1.class);
		PushService.subscribe(this, "protected", Callback2.class);*/
		// 显示的设备的 installationId，用于推送的设备标示
		// 保存 installation 到服务器
		AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
			@Override
			public void done(AVException e) {
				AVInstallation.getCurrentInstallation().saveInBackground();
			}
		});
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

			int id = item.getItemId();
			switch (id) {
				case R.id.action_reboot:
					if(ShellUtils.checkRootPermission()){
						commnandList.clear();
						commnandList.add("reboot");
						ShellUtils.execCommand(commnandList, true);
					}else {
						Toast.makeText(this,"未能获取ROOT权限，该功能无法正常使用",Toast.LENGTH_LONG).show();
					}

					break;
				case R.id.action_shoutdown:
					if(ShellUtils.checkRootPermission()){
						commnandList.clear();
						commnandList.add("reboot -p");
						ShellUtils.execCommand(commnandList, true);
					}else {
						Toast.makeText(this,"未能获取ROOT权限，该功能无法正常使用",Toast.LENGTH_LONG).show();
					}

					break;
				case R.id.action_recovery:
					if(ShellUtils.checkRootPermission()){
						commnandList.clear();
						commnandList.add("reboot recovery");
						ShellUtils.execCommand(commnandList, true);
					}else {
						Toast.makeText(this,"未能获取ROOT权限，该功能无法正常使用",Toast.LENGTH_LONG).show();
					}

					break;
				case R.id.action_bootloader:
					if(ShellUtils.checkRootPermission()){
						commnandList.clear();
						commnandList.add("reboot bootloader");
						ShellUtils.execCommand(commnandList, true);
					}else {
						Toast.makeText(this,"未能获取ROOT权限，该功能无法正常使用",Toast.LENGTH_LONG).show();
					}

					break;
			}

		return true;
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_flash_recovery) {
			// Handle the camera action
			new MaterialFilePicker()
					.withActivity(this)
					.withRequestCode(1)
					.withFilter(Pattern.compile(".*\\.zip$")) // Filtering files and directories by file name using regexp
					.withFilterDirectories(true) // Set directories filterable (false by default)
					.withHiddenFiles(true) // Show hidden files and folders
					.start();
		} else if (id == R.id.nav_factary_set) {


		}  else if (id == R.id.nav_pay) {
			View v = LayoutInflater.from(this).inflate(R.layout.pay,null);
			final Dialog d = new Dialog(this);
			d.setContentView(v);
			d.show();
			Button confirm = (Button) v.findViewById(R.id.confirm);
			Button cancle = (Button) v.findViewById(R.id.cancle);
			final ImageView iv = (ImageView) v.findViewById(R.id.iv);
			iv.setBackgroundResource(R.mipmap.alipay);
			iv.setOnLongClickListener(new View.OnLongClickListener() {
				public String path;

				@Override
				public boolean onLongClick(View view) {
					if(flag==0){
						path = Environment.getExternalStorageDirectory()+"/pay/alipay.jpg";
					}else {
						path = Environment.getExternalStorageDirectory()+"/pay/weixin.jpg";
					}
					CodeUtils.analyzeBitmap(path, new CodeUtils.AnalyzeCallback() {
						@Override
						public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
							Logger.e(result);
							Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
							it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
							startActivity(it);
						}

						@Override
						public void onAnalyzeFailed() {
							Logger.e("失败");

						}
					});
					return false;
				}
			});

			confirm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					iv.setBackgroundResource(R.mipmap.ic_launcher);
					iv.setBackgroundResource(R.mipmap.alipay);
					flag=0;
				}
			});
			cancle.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					iv.setBackgroundResource(R.mipmap.ic_launcher);
					iv.setBackgroundResource(R.mipmap.weixin);
					flag=1;
				}
			});


		} else if (id == R.id.nav_send) {


		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
	private void initTitleBar() {
		//添加顶部标题
		mTitle.add("通用");
		mTitle.add("工具");
		mTitle.add("热点");
		mTitle.add("作者");
		//添加fragment
		mFragment.add(new ToolFragment());
		mFragment.add(new ToolFragment());
		mFragment.add(new ToolFragment());
		mFragment.add(new AboutMeFragment());

		//设置数据
		HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager(), mTitle, mFragment);
		mVpView.setAdapter(adapter);
		//为TabLayout设置ViewPager
		mTabs.setupWithViewPager(mVpView);
		//使用ViewPager的适配器
		mTabs.setTabsFromPagerAdapter(adapter);
		mVpView.setCurrentItem(0);
		TabLayoutUtils.setIndicator(this, mTabs, 5, 5);

	}


	protected void onPause() {
		super.onPause();
		AVAnalytics.onPause(this);

	}

	protected void onResume() {
		super.onResume();
		AVAnalytics.onResume(this);


	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}







}
