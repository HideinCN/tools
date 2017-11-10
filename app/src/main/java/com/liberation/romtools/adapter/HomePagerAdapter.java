package com.liberation.romtools.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;


import com.liberation.romtools.base.BaseFragment;

import java.util.List;

/**
 * 项目名称： ROMS
 * 创建人 : Liberation
 * 创建时间: 2017/3/10 15:55
 */

public class HomePagerAdapter extends FragmentPagerAdapter {
	private List<String> title;
	private List<BaseFragment> views;

	public HomePagerAdapter(FragmentManager fm, List<String> title, List<BaseFragment> views) {
		super(fm);
		this.title = title;
		this.views = views;
	}


	@Override
	public Fragment getItem(int position) {
		return views.get(position);
	}

	@Override
	public int getCount() {
		return title==null?0:title.size();
	}

	//配置标题的方法
	@Override
	public CharSequence getPageTitle(int position) {
		return title.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		//super.destroyItem(container, position, object);
	}
}