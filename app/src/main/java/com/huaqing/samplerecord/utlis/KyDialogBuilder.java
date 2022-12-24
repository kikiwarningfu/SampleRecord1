package com.huaqing.samplerecord.utlis;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.huaqing.samplerecord.widget.KyDialogView;

public class KyDialogBuilder {
	private Context context;
	private BackgroundLinearLayout backgroundLayout;
	private KyDialogView dialogView;
	private int backgroundAlpha = 150;

	public KyDialogBuilder(Context activity){
		this.context = activity;
		dialogView = new KyDialogView(context);
	}
	public KyDialogBuilder() {
		dialogView = new KyDialogView(context);
	}
	
	public KyDialogBuilder(Activity activity, int nTextColor, int pTextColor, int pBgColor, int pPressBgColor) {
		this.context = activity;
		dialogView = new KyDialogView(context,nTextColor,pTextColor,pBgColor,pPressBgColor);
	}
	
	public void show(){
		backgroundLayout = new BackgroundLinearLayout(context);
		backgroundLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		backgroundLayout.setGravity(Gravity.CENTER);
		backgroundLayout.setBackgroundColor(0xFF000000);

		backgroundLayout.getBackground().setAlpha(backgroundAlpha);
		backgroundLayout.addView(dialogView);		

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
		wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
		wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
		wlp.format = PixelFormat.RGBA_8888;
		wm.addView(backgroundLayout, wlp);
	}
	public void dismiss(){
		if(backgroundLayout == null)
			return ;
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.removeView(backgroundLayout);
		backgroundLayout = null;
	}
	public void setTitle(String title){
		dialogView.setTitle(title);
	}
	public void setMessage(String message) {
		dialogView.setMessage(message);
	}
	public void setNegativeButton(String negativeText, OnClickListener negativeListener){
		dialogView.setNegativeButton(negativeText, negativeListener);
	}
	public void setPositiveButton(String positiveText, OnClickListener positiveListener){
		dialogView.setPositiveButton(positiveText, positiveListener);
	}
	public void setPositiveListener(OnClickListener positiveListener) {
		dialogView.setPositiveListener(positiveListener);
	}
	public void setNegativeListener(OnClickListener negativeListener) {
		dialogView.setNegativeListener(negativeListener);
	}
	public void setTitleTextSize(int titleTextSize) {
		dialogView.setTitleTextSize(titleTextSize);
	}
	public void setMessageTextSize(int messageTextSize) {
		dialogView.setMessageTextSize(messageTextSize);
	}
	public void setButtonTextSize(int buttonTextSize) {
		dialogView.setButtonTextSize(buttonTextSize);
	}
	public void setTitleTextColor(int titleTextColor) {
		dialogView.setTitleTextColor(titleTextColor);
	}
	public void setMessageTextColor(int messageTextColor) {
		dialogView.setMessageTextColor(messageTextColor);
	}
	public void setNegativeNormalTextColor(int negativeNormalTextColor) {
		dialogView.setNegativeNormalTextColor(negativeNormalTextColor);
	}
	public void setNegativePressedTextColor(int negativePressedTextColor) {
		dialogView.setNegativePressedTextColor(negativePressedTextColor);
	}
	public void setPositiveNormalTextColor(int positiveNormalTextColor) {
		dialogView.setPositiveNormalTextColor(positiveNormalTextColor);
	}
	public void setPositivePressedTextColor(int positivePressedTextColor) {
		dialogView.setPositivePressedTextColor(positivePressedTextColor);
	}
	public void setNegativePressedBgColor(int negativePressedBgColor) {
		dialogView.setNegativePressedBgColor(negativePressedBgColor);
	}
	public void setPositivePressedBgColor(int positivePressedBgColor) {
		dialogView.setPositivePressedBgColor(positivePressedBgColor);
	}
	public void setPositiveBgColor(int color) {
		dialogView.setTempColor(color);
	}
	public void setCornerRadius(int cornerRadius) {
		dialogView.setCornerRadius(cornerRadius);
	}
	public void setSeparateLineWidth(int separateLineWidth) {
		dialogView.setSeparateLineWidth(separateLineWidth);
	}
	public void setMessagePadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom){
		dialogView.setMessagePadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
	}
	public int getMessagePaddingLeft() {
		return dialogView.getMessagePaddingLeft();
	}
	public void setMessagePaddingLeft(int messagePaddingLeft) {
		dialogView.setMessagePaddingLeft(messagePaddingLeft);
	}
	public int getMessagePaddingTop() {
		return dialogView.getMessagePaddingTop();
	}
	public void setMessagePaddingTop(int messagePaddingTop) {
		dialogView.setMessagePaddingTop(messagePaddingTop);
	}
	public int getMessagePaddingRight() {
		return dialogView.getMessagePaddingRight();
	}
	public void setMessagePaddingRight(int messagePaddingRight) {
		dialogView.setMessagePaddingRight(messagePaddingRight);
	}
	public int getMessagePaddingBottom() {
		return dialogView.getMessagePaddingBottom();
	}
	public void setMessagePaddingBottom(int messagePaddingBottom) {
		dialogView.setMessagePaddingBottom(messagePaddingBottom);
	}
	public void setBackgroundAlpha(int backgroundAlpha) {
		this.backgroundAlpha = backgroundAlpha;
	}

	private class BackgroundLinearLayout extends LinearLayout {
		public BackgroundLinearLayout(Context context) {
			super(context);
		}
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
		}
		@Override
		public boolean dispatchKeyEvent(KeyEvent event) {
			if(event.getKeyCode() == KeyEvent.KEYCODE_BACK )
			{
				dismiss();
			}

			return super.dispatchKeyEvent(event);
		}
	}
	public void setMessage(String message, OnClickListener messageListener) {
		dialogView.setMessage(message,messageListener);
	}
	public void setMessageListener(OnClickListener messageListener) {
		dialogView.setMessageListener(messageListener);
	}

}
