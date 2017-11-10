package com.liberation.romtools.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liberation.romtools.R;
import com.liberation.romtools.model.Tool;


import java.util.List;

/**
 * Created by Liberation on 2017/10/24.
 */

public class ToolAdapter extends BaseQuickAdapter<Tool,BaseViewHolder> {
	public ToolAdapter(@LayoutRes int layoutResId, @Nullable List<Tool> data) {
		super(layoutResId, data);
	}

	@Override
	protected void convert(BaseViewHolder helper,Tool item) {
		helper.setText(R.id.tv_name, item.getName());

	}
}
