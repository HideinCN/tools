package com.liberation.romtools.base;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 项目名称： ROMS
 * 创建人 : Liberation
 * 创建时间: 2017/3/10 15:44
 */

public abstract class BaseFragment extends Fragment {

	protected BaseActivity mActivity;

	protected abstract void initView(View view, Bundle savedInstanceState);

	//获取布局文件ID
	protected abstract int getLayoutId();

	//获取宿主Activity
	protected BaseActivity getHoldingActivity() {
		return mActivity;
	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	//添加fragment
	protected void addFragment(BaseFragment fragment) {
		if (null != fragment) {
			getHoldingActivity().addFragment(fragment);
		}
	}

	//移除fragment
	protected void removeFragment() {
		getHoldingActivity().removeFragment();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(getLayoutId(), container, false);
		initView(view, savedInstanceState);
		return view;
	}
}