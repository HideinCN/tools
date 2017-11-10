package com.liberation.romtools.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.liberation.romtools.R;
import com.liberation.romtools.base.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 项目名称： ROMS
 * 创建人 : Liberation
 * 创建时间: 2017/3/10 15:52
 */

public class AboutMeFragment extends BaseFragment implements View.OnClickListener {
	@BindView(R.id.tv_notice)
	TextView mTvNotice;
	@BindView(R.id.tv_contact_me)
	TextView mTvContactMe;
	@BindView(R.id.tv_qq)
	TextView mTvQQ;


	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		ButterKnife.bind(this, view);
		initData();
		mTvQQ.setOnClickListener(this);

	}

	private void initData() {
		mTvNotice.setText(
				"1. 刷入第三方ROM可能导致不再享有厂商的保修服务。\n" +
						"2. 此ROM若出现BUG，例如导致你的手机闹钟不响而被老板炒了鱿鱼，大表哥只能为你默哀三分钟。\n" +
						"3. 本ROM存在的缺陷问题欢迎联系我，本人会尽力去修正。\n" +
						"4. 毕竟个人能力有限不要把此ROM与官方ROM比较，觉得我做的ROM符合你的口味就用，不喜欢就换，没必要恶语相加。\n" +
						"5. 未经我的允许，不得将此ROM二次修改打包发布。"
						);
		mTvContactMe.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_about;


	}


	@Override
	public void onClick(View view) {
		if(isQQClientAvailable(getContext())){
			String url="mqqwpa://im/chat?chat_type=wpa&uin=910689331&version=1&src_type=web&web_src=badcatu.com";
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		}else {
			Toast.makeText(getContext(),"QQ未安装或者无法正常运行",Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * 判断qq是否可用
	 *
	 * @param context
	 * @return
	 */
	public static boolean isQQClientAvailable(Context context) {
		final PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mobileqq")) {
					return true;
				}
			}
		}
		return false;
	}
}
