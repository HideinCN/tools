package com.liberation.romtools.view.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dingmouren.colorpicker.ColorPickerDialog;
import com.dingmouren.colorpicker.ColorUtil;
import com.dingmouren.colorpicker.OnColorPickerListener;
import com.liberation.romtools.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Liberation on 2017/11/10.
 */

public class FaceActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
	@BindView(R.id.iv)
	ImageView mIv;
	@BindView(R.id.tv_text)
	EditText mTvText;
	@BindView(R.id.tv_color)
	TextView mTvColor;
	@BindView(R.id.sb_textSize)
	SeekBar mSbTextSize;
	@BindView(R.id.tv_notice)
	TextView mTvNotice;
	@BindView(R.id.cv_head)
	CardView mCvHead;
	@BindView(R.id.rv_head)
	RelativeLayout mRvHead;
	@BindView(R.id.sc)
	Switch mSc;
	private int RESULT_LOAD_IMAGE = 100;
	private int textSize;
	private File file;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_face);
		ButterKnife.bind(this);
		setTitle("表情");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mSbTextSize.setOnSeekBarChangeListener(this);
		mTvColor.setOnLongClickListener(this);
		mTvText.addTextChangedListener(new MyEditTextChangeListener());
		mSc.setOnCheckedChangeListener(this);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.face_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		if (item.getItemId() == R.id.save) {
			viewSaveToImage(mRvHead);
			return true;
		}

		if(item.getItemId()==R.id.share){
			share();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@OnClick(R.id.cv_head)
	public void onViewClicked() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, RESULT_LOAD_IMAGE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data)


		{
			Uri selectedImage = data.getData();
			String[] filePathColumn = {MediaStore.Images.Media.DATA};

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			Glide.with(this).load(picturePath).error(R.mipmap.alipay).into(mIv);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
		mTvNotice.setTextSize(textSize = i / 2);

	}


	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		modifyTextSize();
	}

	private void modifyTextSize() {
		if (TextUtils.isEmpty(mTvText.getText().toString())) {
			Toast.makeText(this, "请输入需要添加的文字", Toast.LENGTH_LONG).show();
			return;
		}
		mTvNotice.setText(mTvText.getText().toString());
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public boolean onLongClick(View view) {
		initColorPicker();
		return true;
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
				this,
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
			mTvNotice.setTextColor(Color.parseColor(ColorUtil.convertToARGB(color)));
		}

		@Override
		public void onColorConfirm(ColorPickerDialog dialog, int color) {//确定的颜色

		}
	};

	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
		if (b) {
			mTvNotice.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
		} else {
			mTvNotice.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//加粗
		}

	}


	private class MyEditTextChangeListener implements TextWatcher {
		/**
		 * 编辑框的内容发生改变之前的回调方法
		 */
		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			mTvNotice.setTextSize(textSize);
		}

		/**
		 * 编辑框的内容正在发生改变时的回调方法 >>用户正在输入
		 * 我们可以在这里实时地 通过搜索匹配用户的输入
		 */
		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			mTvNotice.setTextSize(i);
			mTvNotice.setText(charSequence);
		}

		/**
		 * 编辑框的内容改变以后,用户没有继续输入时 的回调方法
		 */
		@Override
		public void afterTextChanged(Editable editable) {
		}
	}

	public void viewSaveToImage(View view) {
		view.setDrawingCacheEnabled(true);
		view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
		view.setDrawingCacheBackgroundColor(Color.WHITE);

		// 把一个View转换成图片
		Bitmap cachebmp = loadBitmapFromView(view);


		FileOutputStream fos;
		try {
			// 判断手机设备是否有SD卡
			boolean isHasSDCard = Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED);
			if (isHasSDCard) {
				// SD卡根目录
				File sdRoot = Environment.getExternalStorageDirectory();
				file = new File(sdRoot, System.currentTimeMillis()+".png");
				fos = new FileOutputStream(file);
			} else{
				throw new Exception("创建文件失败!");
			}


			cachebmp.compress(Bitmap.CompressFormat.PNG, 100, fos);

			fos.flush();
			fos.close();
			Toast.makeText(FaceActivity.this, "成功保存到" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			Toast.makeText(FaceActivity.this, "保存失败" , Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

		view.destroyDrawingCache();
	}

	private Bitmap loadBitmapFromView(View v) {
		int w = v.getWidth();
		int h = v.getHeight();

		Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmp);

		c.drawColor(Color.WHITE);
		/** 如果不设置canvas画布为白色，则生成透明 */
		v.layout(0, 0, w, h);
		v.draw(c);

		return bmp;
	}

	private void share(){
		viewSaveToImage(mRvHead);
		Uri imageUri = Uri.fromFile(file);
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
		shareIntent.setType("image/*");
		startActivity(Intent.createChooser(shareIntent, "分享图片"));
	}

}
