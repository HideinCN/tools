package com.liberation.romtools.view.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dingmouren.colorpicker.ColorPickerDialog;
import com.dingmouren.colorpicker.ColorUtil;
import com.dingmouren.colorpicker.OnColorPickerListener;
import com.liberation.romtools.R;
import com.liberation.romtools.adapter.ToolAdapter;
import com.liberation.romtools.base.BaseFragment;
import com.liberation.romtools.model.Tool;
import com.liberation.romtools.view.activity.FaceActivity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Liberation on 2017/11/9.
 */

public class ToolFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {
	@BindView(R.id.rv)
	RecyclerView mRv;
	Unbinder unbinder;

	ToolAdapter mAdapter;
	private List<Tool> datas;

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		unbinder = ButterKnife.bind(this, view);
		datas = new ArrayList<>();
		datas.add(new Tool("表情制作"));
		datas.add(new Tool("手机跑分"));
		datas.add(new Tool("取色器"));
		datas.add(new Tool("王卡助手"));
		datas.add(new Tool("工具箱"));
		datas.add(new Tool("网页转应用"));
		datas.add(new Tool("我会翻译"));
		datas.add(new Tool("以图搜图"));
		datas.add(new Tool("应用管理"));
		datas.add(new Tool("带壳截图"));
		datas.add(new Tool("直链提取"));
		datas.add(new Tool("物流查询"));
		datas.add(new Tool("二维码生成"));
		datas.add(new Tool("氢壁纸"));
		datas.add(new Tool("文字处理"));
		datas.add(new Tool("原生氢壁纸"));
		mAdapter = new ToolAdapter(R.layout.item_tool, datas);
		mAdapter.setOnItemClickListener(this);
		mRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
		mRv.setAdapter(mAdapter);

	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_tool;
	}


	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	@Override
	public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		switch (datas.get(position).getName()) {
			case "表情制作": {
				startActivity(new Intent(getContext(), FaceActivity.class));
			}
			break;
			case "取色器": {
				initColorPicker();
			}
			break;
			case "工具箱": {
			}
			break;
			case "网页转应用": {
			}
			break;
			case "我会翻译": {
			}
			break;
			case "以图搜图": {
			}
			break;
			case "应用管理": {
			}
			break;
			case "带壳截图": {
			}
			break;
			case "直链提取": {
			}
			break;
			case "物流查询": {
			}
			break;
			case "二维码生成": {
			}
			break;
			case "氢壁纸": {
			}
			break;
			case "文字处理": {
			}
			break;
			case "原生氢壁纸": {
			}
			break;
			case "手机跑分": {
			}
			break;


		}
	}

	private void initColorPicker() {
/*
 * 创建支持透明度的取色器
 * @param context 宿主Activity
 * @param defauleColor 默认的颜色
 * @param isSupportAlpha 颜色是否支持透明度
 * @param listener 取色器的监听器
 */
		ColorPickerDialog mColorPickerDialog = new ColorPickerDialog(
				getContext(),
				getResources().getColor(R.color.colorPrimary),
				true,
				mOnColorPickerListener
		).show();
		mColorPickerDialog.setButtonTextColor(R.color.colorAccent);
	}


	//取色器的监听器
	private OnColorPickerListener mOnColorPickerListener = new OnColorPickerListener() {
		@Override
		public void onColorCancel(ColorPickerDialog dialog) {//取消选择的颜色

		}

		@Override
		public void onColorChange(ColorPickerDialog dialog, int color) {//实时监听颜色变化

		}

		@Override
		public void onColorConfirm(ColorPickerDialog dialog, int color) {//确定的颜色
			// 从API11开始android推荐使用android.content.ClipboardManager
			// 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
			ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
			// 将文本内容放到系统剪贴板里。
			cm.setText(ColorUtil.convertToARGB(color));
			Toast.makeText(getContext(), ColorUtil.convertToARGB(color) + "已复制到剪切板", Toast.LENGTH_LONG).show();
		}
	};
}
