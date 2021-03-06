//package com.bnrc.ui.rjz;
//
//import com.bnrc.busapp.R;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.drawable.ColorDrawable;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.PopupWindow;
//
//public class PublishSelectPicPopupWindow extends PopupWindow {
//
//	private Button mCancelBtn;
//	private ImageView mHomeBtn, mWorkBtn, mDelBtn;
//	private View mMenuView;
//
//	@SuppressWarnings("deprecation")
//	public PublishSelectPicPopupWindow(Activity context,
//			OnClickListener clickListener) {
//		super(context);
//		LayoutInflater inflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		mMenuView = inflater.inflate(R.layout.publish_dialog, null);
//
//		mCancelBtn = (Button) mMenuView.findViewById(R.id.btn_cancel);
//		mHomeBtn = (ImageView) mMenuView.findViewById(R.id.iv_home);
//		mWorkBtn = (ImageView) mMenuView.findViewById(R.id.iv_work);
//		mDelBtn = (ImageView) mMenuView.findViewById(R.id.iv_del);
//
//		mHomeBtn.setOnClickListener(clickListener);
//		mWorkBtn.setOnClickListener(clickListener);
//		mDelBtn.setOnClickListener(clickListener);
//		mCancelBtn.setOnClickListener(clickListener);
//		// 设置SelectPicPopupWindow的View
//		this.setContentView(mMenuView);
//		// 设置SelectPicPopupWindow弹出窗体的宽
//		this.setWidth(LayoutParams.FILL_PARENT);
//		// 设置SelectPicPopupWindow弹出窗体的高
//		this.setHeight(LayoutParams.WRAP_CONTENT);
//		// 设置SelectPicPopupWindow弹出窗体可点击
//		this.setFocusable(true);
//		// 设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimBottom);
//		// 实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0xb0000000);
//		// 设置SelectPicPopupWindow弹出窗体的背景
//		this.setBackgroundDrawable(dw);
//		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//		mMenuView.setOnTouchListener(new OnTouchListener() {
//			public boolean onTouch(View v, MotionEvent event) {
//
//				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
//				int y = (int) event.getY();
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					if (y < height) {
//						dismiss();
//					}
//				}
//				return true;
//			}
//		});
//
//	}
//
//}
