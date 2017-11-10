package com.liberation.romtools.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * 项目名称： ROMS
 * 创建人 : Liberation
 * 创建时间: 2017/3/10 15:44
 */

public abstract class BaseActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ButterKnife.bind(this);
		initData();
	}

	//布局文件ID
	protected abstract int getContentViewId();

	//布局中Fragment的ID
	protected abstract int getFragmentContentId();
	protected abstract void initData();

	//添加fragment
	protected void addFragment(BaseFragment fragment) {
		if (fragment != null) {
			getSupportFragmentManager().beginTransaction()
					.replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
					.addToBackStack(fragment.getClass().getSimpleName())
					.commitAllowingStateLoss();
		}
	}

	//移除fragment
	protected void removeFragment() {
		if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
			getSupportFragmentManager().popBackStack();
		} else {
			finish();
		}
	}

}
