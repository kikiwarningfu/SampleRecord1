package com.huaqing.samplerecord.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class KyDialogView extends View implements OnGestureListener {

	private Paint paint = new Paint();
	private RectF rectF = new RectF();
	private GestureDetector mGestureDetector;

	private String title;
	private String message;

	private boolean havePositive;
	private String positiveText;
	private OnClickListener positiveListener;

	private boolean haveNegative;
	private String negativeText;
	private OnClickListener negativeListener;

	private int titleTextSize = dip2px(this.getContext(), 18);
	private int messageTextSize = dip2px(this.getContext(), 16);
	private int buttonTextSize = dip2px(this.getContext(), 16);

	private int titleTextColor = 0xFF222222;
	private int messageTextColor = 0xFF222222;
	private int negativeNormalTextColor = 0xFFF39800;
	private int negativePressedTextColor = 0xFFF39800;
	private int positiveNormalTextColor = 0xFFF39800;
	private int positivePressedTextColor = 0xFFF39800;

	private int negativePressedBgColor = 0xFFF2F2F2;
	private int positivePressedBgColor = 0xFFF2F2F2;

	private int messagePaddingLeft = dip2px(this.getContext(), 20);
	private int messagePaddingTop = dip2px(this.getContext(), -25);
	private int messagePaddingRight = dip2px(this.getContext(), 15);
	private int messagePaddingBottom = dip2px(this.getContext(), 0);

	private int cornerRadius = dip2px(this.getContext(), 10);
	private int separateLineWidth = createDefaultSeparateLineWidth();
	private int lineSpacing = dip2px(this.getContext(), 5);

	private int tempColor = 0xFFFFFFFF;
	
	private int negativeCurrentTextColor = negativeNormalTextColor;
	private int positiveCurrentTextColor = positiveNormalTextColor;
	private int negativeCurrentBgColor = 0xFFFFFFFF;
	private int positiveCurrentBgColor = getTempColor();
	
	private OnClickListener messageListener;
	private float strWidth = 0;
	private float strMesWidth = 0;
	private float strMesTop = 0;
	private float strMesBottom = 0;

	public KyDialogView(Context context) {
		super(context);
		mGestureDetector = new GestureDetector(this);
		paint.setAntiAlias(true);
	}
	
	public KyDialogView(Context context, int nTextColor, int pTextColor, int pBgColor, int pPressBgColor) {
		super(context);
		mGestureDetector = new GestureDetector(this);
		paint.setAntiAlias(true);
		
		negativeCurrentTextColor = nTextColor;
		positiveCurrentTextColor = pTextColor;
		positiveCurrentBgColor = pBgColor;
		positivePressedBgColor = pPressBgColor;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(), measureHeight());
	}

	private int measureWidth() {
		int width = dip2px(this.getContext(), 310);
		return width;
	}

	private int measureHeight() {
		 return getTitleLayoutHeight() + getMessageLayoutHeight()
		 + getButtonLayoutHeight();
//		return getMessageLayoutHeight() + getButtonLayoutHeight();
	}

	private int getTitleLayoutHeight() {
		return dip2px(this.getContext(), 50);
	}

	private int getButtonLayoutHeight() {
		return dip2px(this.getContext(), 50);
	}

	private int getMessageLayoutHeight() {
		return dip2px(this.getContext(), 70);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		positiveCurrentTextColor= Color.parseColor("#34B48F");
		negativeCurrentTextColor= Color.parseColor("#000000");
		drawBackground(canvas);
		drawTitle(canvas);
		drawMessage(canvas);
		drawButtons(canvas);
	}

	private void drawBackground(Canvas canvas) {
		rectF.left = 0;
		rectF.top = 0;
		rectF.right = this.getWidth();
		rectF.bottom = this.getHeight();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(0xFFFFFFFF);
		canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
	}

	private void drawTitle(Canvas canvas) {
		// canvas.drawColor(this.positivePressedTextColor);
		// canvas.drawl
		paint.setColor(this.titleTextColor);
		paint.setTextSize(this.titleTextSize);
		String title = this.title == null ? "" : this.title.trim();
		int txtWidth = (int) this.paint.measureText(title);
		FontMetricsInt fontMetrics = paint.getFontMetricsInt();
		int txtHeight = fontMetrics.bottom - fontMetrics.ascent;
		canvas.drawText(title, this.getWidth() / 2 - txtWidth / 2,
				(getTitleLayoutHeight() - txtHeight) / 2 - fontMetrics.ascent,
				paint);
	}

	private void drawMessage(Canvas canvas) {
		paint.setTextSize(this.messageTextSize);
		List<String> lines = splitMessage(message, this.getWidth()
				- this.messagePaddingLeft - this.messagePaddingRight);
		int lineCount = lines == null ? 0 : lines.size();// 琛屾暟
		if (lineCount == 0)
			return;
		paint.setColor(this.messageTextColor);
		FontMetricsInt fontMetrics = paint.getFontMetricsInt();
		int txtWidth = (int) this.paint.measureText(message);
		int txtHeight = fontMetrics.bottom - fontMetrics.ascent;
		int top = this.getMessageLayoutHeight() + this.messagePaddingTop;
		strMesTop = top - fontMetrics.ascent;
		for (String line : lines) {
			canvas.drawText(line, this.getWidth() / 2 - txtWidth / 2, 
					top- fontMetrics.ascent, paint);
			top += txtHeight;
			top += lineSpacing;
			strWidth = paint.measureText(line + "a");
			strMesBottom = top;
		}
	}

	private void drawButtons(Canvas canvas) {

		RectF rectF = createButtonRectF();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(0xFFDADADA);
		Path path = new Path();
		float[] radii = { 0f, 0f, 0f, 0f, cornerRadius, cornerRadius,
				cornerRadius, cornerRadius };
		path.addRoundRect(rectF, radii, Path.Direction.CW);
		canvas.drawPath(path, paint);

		drawNegativeButtons(canvas);

		drawPositiveButtons(canvas);
	}

	private void drawNegativeButtons(Canvas canvas) {
		RectF rectF = createNegativeRectF();
		if (rectF == null)
			return;
		paint.setColor(negativeCurrentBgColor);
		Path path = new Path();
		float[] radii = new float[] { 0f, 0f, 0f, 0f, 0f, 0f, cornerRadius,
				cornerRadius };
		path.addRoundRect(rectF, radii, Path.Direction.CW);
		canvas.drawPath(path, paint);
		paint.setColor(negativeCurrentTextColor);
		paint.setTextSize(this.buttonTextSize);
		FontMetricsInt fontMetrics = paint.getFontMetricsInt();
		String negativeText = this.negativeText == null ? ""
				: this.negativeText.trim();
		int txtWidth = (int) this.paint.measureText(negativeText);
		int txtHeight = fontMetrics.bottom - fontMetrics.ascent;
		canvas.drawText(negativeText, rectF.width() / 2 - txtWidth / 2,
				rectF.top + (rectF.height() - txtHeight) / 2
						- fontMetrics.ascent, paint);
	}

	private void drawPositiveButtons(Canvas canvas) {
		paint.setColor(getPositiveCurrentBgColor());
		RectF rectF = createPositiveRectF();
		Path path = new Path();
		float[] radii = null;
//		if (Constant.DIALOGTAG) {
//			radii = new float[] { 0f, 0f, 0f, 0f, cornerRadius, cornerRadius,
//					cornerRadius, cornerRadius };
//		} else {
			radii = new float[] { 0f, 0f, 0f, 0f, cornerRadius, cornerRadius,
					0f, 0f };
//		}
		path.addRoundRect(rectF, radii, Path.Direction.CW);
		canvas.drawPath(path, paint);
		paint.setColor(positiveCurrentTextColor);
		paint.setTextSize(this.buttonTextSize);
		String positiveText = this.positiveText == null ? ""
				: this.positiveText.trim();
		FontMetricsInt fontMetrics = paint.getFontMetricsInt();
		int txtWidth = (int) this.paint.measureText(positiveText);
		int txtHeight = fontMetrics.bottom - fontMetrics.ascent;
		canvas.drawText(positiveText, rectF.left
				+ (rectF.width() / 2 - txtWidth / 2),
				rectF.top + (rectF.height() - txtHeight) / 2
						- fontMetrics.ascent, paint);
	}

	private RectF createButtonRectF() {
		int top = this.getHeight() - this.getButtonLayoutHeight();
		rectF.left = 0;
		rectF.top = top;
		rectF.right = this.getWidth();
		rectF.bottom = this.getHeight();
		return rectF;
	}

	private RectF createNegativeRectF() {
		if (!this.haveNegative)
			return null;
		int right = (this.getWidth() - separateLineWidth) / 2;
		if (!this.havePositive)
			right = this.getWidth();
		int top = this.getHeight() - this.getButtonLayoutHeight()
				+ separateLineWidth;
		rectF.left = 0;
		rectF.top = top;
		rectF.right = right;
		rectF.bottom = this.getHeight();
		return rectF;
	}

	private RectF createPositiveRectF() {
		if (!this.havePositive)
			return null;
		int left = (this.getWidth() - separateLineWidth) / 2
				+ separateLineWidth;
		if (!this.haveNegative)
			left = 0;
		int top = this.getHeight() - this.getButtonLayoutHeight()
				+ separateLineWidth;
		rectF.left = left;
		rectF.top = top;
		rectF.right = this.getWidth();
		rectF.bottom = this.getHeight();
		return rectF;
	}

	private List<String> splitMessage(String text, int lineWidth) {
		if (text == null || text.trim().equals(""))
			return null;
		paint.setTextSize(this.messageTextSize);
		FontMetricsInt fontMetrics = paint.getFontMetricsInt();
		List<String> lines = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			String ch = text.substring(i, i + 1);
			int txtWidth = (int) paint.measureText(sb.toString() + ch);
			if (txtWidth <= lineWidth) {
				sb.append(ch);
				continue;
			}
			lines.add(sb.toString());
			sb = new StringBuffer();
			sb.append(ch);
		}
		if (sb.length() != 0)
			lines.add(sb.toString());
		return lines;
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	private int createDefaultSeparateLineWidth() {
		WindowManager wm = (WindowManager) this.getContext().getSystemService(
				Context.WINDOW_SERVICE);
		int screenWidth = wm.getDefaultDisplay().getWidth();
		DisplayMetrics metric = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metric);
		int densityDpi = metric.densityDpi;

		if (screenWidth >= 1400) {// 1440
			return dip2px(this.getContext(), 1);
		}
		if (screenWidth >= 1000) {// 1080
			if (densityDpi >= 480)
				return 3;
			if (densityDpi >= 320)
				return 2;
			return 2;
		}
		if (screenWidth >= 700) {// 720
			if (densityDpi >= 320)
				return 2;
			if (densityDpi >= 240)
				return 2;
			return 1;
		}
		return 1;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
			mGestureDetector.onTouchEvent(event);
			if (event.getAction() == MotionEvent.ACTION_UP) {
				upEventHandler((int) event.getX(), (int) event.getY());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean onDown(MotionEvent event) {
		downEventHandler((int) event.getX(), (int) event.getY());
		return true;
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2, float arg2,
                           float arg3) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent event) {
	}

	@Override
	public boolean onScroll(MotionEvent event1, MotionEvent event2, float arg2,
                            float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent event) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		clickEventHandler((int) event.getX(), (int) event.getY());
		return true;
	}

	private void downEventHandler(int x, int y) {
		RectF rectF = createNegativeRectF();
		if (rectF != null) {
			if (x >= rectF.left && x <= rectF.right && y >= rectF.top
					&& y <= rectF.bottom) {
				this.negativeCurrentTextColor = this.negativePressedTextColor;
				this.negativeCurrentBgColor = this.negativePressedBgColor;
			}
		}
		rectF = createPositiveRectF();
		if (rectF != null) {
			if (x >= rectF.left && x <= rectF.right && y >= rectF.top
					&& y <= rectF.bottom) {
				this.positiveCurrentTextColor = this.positivePressedTextColor;
				this.setPositiveCurrentBgColor(this.positivePressedBgColor);
			}
		}
		this.invalidate();
	}

	private void upEventHandler(int x, int y) {
		this.negativeCurrentTextColor = this.negativeNormalTextColor;
		this.negativeCurrentBgColor = 0xFFFFFFFF;

		this.positiveCurrentTextColor = this.positiveNormalTextColor;
		this.setPositiveCurrentBgColor(getTempColor());
		this.invalidate();
	}

	private void clickEventHandler(int x, int y) {
		RectF rectF = createNegativeRectF();
		if (rectF != null) {
			if (x >= rectF.left && x <= rectF.right && y >= rectF.top
					&& y <= rectF.bottom) {
				if (this.negativeListener != null) {
					this.negativeListener.onClick(this);
					return;
				}
			}
		}
		rectF = createPositiveRectF();
		if (rectF != null) {
			if (x >= rectF.left && x <= rectF.right && y >= rectF.top
					&& y <= rectF.bottom) {
				if (this.positiveListener != null) {
					this.positiveListener.onClick(this);
					return;
				}
			}
		}

		rectF = createMessageRectF();
		if (rectF != null) {
			if (x >= rectF.left && x <= rectF.right && y >= rectF.top
					&& y <= rectF.bottom) {
				if (this.messageListener != null) {
					this.messageListener.onClick(this);
					return;
				}
			}
		}
	}

	private RectF createMessageRectF() {
		// int top = this.getTitleLayoutHeight();
		rectF.left = strWidth;
		rectF.top = strMesTop;
		rectF.right = strWidth + strMesWidth;
		// rectF.bottom = this.getHeight() - this.getButtonLayoutHeight();
		rectF.bottom = strMesBottom;
		return rectF;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setNegativeButton(String negativeText,
                                  OnClickListener negativeListener) {
		this.haveNegative = true;
		this.negativeText = negativeText;
		this.negativeListener = negativeListener;
	}

	public void setPositiveButton(String positiveText,
                                  OnClickListener positiveListener) {
		this.havePositive = true;
		this.positiveText = positiveText;
		this.positiveListener = positiveListener;
	}

	public void setPositiveListener(OnClickListener positiveListener) {
		this.positiveListener = positiveListener;
	}

	public void setNegativeListener(OnClickListener negativeListener) {
		this.negativeListener = negativeListener;
	}

	public void setTitleTextSize(int titleTextSize) {
		this.titleTextSize = titleTextSize;
	}

	public void setMessageTextSize(int messageTextSize) {
		this.messageTextSize = messageTextSize;
	}

	public void setButtonTextSize(int buttonTextSize) {
		this.buttonTextSize = buttonTextSize;
	}

	public void setTitleTextColor(int titleTextColor) {
		this.titleTextColor = titleTextColor;
	}

	public void setMessageTextColor(int messageTextColor) {
		this.messageTextColor = messageTextColor;
	}

	public void setNegativeNormalTextColor(int negativeNormalTextColor) {
		this.negativeNormalTextColor = negativeNormalTextColor;
	}

	public void setNegativePressedTextColor(int negativePressedTextColor) {
		this.negativePressedTextColor = negativePressedTextColor;
	}

	public void setPositiveNormalTextColor(int positiveNormalTextColor) {
		this.positiveNormalTextColor = positiveNormalTextColor;
		this.positiveCurrentTextColor = this.positiveNormalTextColor;
	}

	public void setPositivePressedTextColor(int positivePressedTextColor) {
		this.positivePressedTextColor = positivePressedTextColor;
	}

	public void setNegativePressedBgColor(int negativePressedBgColor) {
		this.negativePressedBgColor = negativePressedBgColor;
	}

	public void setPositivePressedBgColor(int positivePressedBgColor) {
		this.positivePressedBgColor = positivePressedBgColor;
	}

	public void setCornerRadius(int cornerRadius) {
		this.cornerRadius = cornerRadius;
	}

	public void setSeparateLineWidth(int separateLineWidth) {
		this.separateLineWidth = separateLineWidth;
	}

	public void setMessagePadding(int paddingLeft, int paddingTop,
			int paddingRight, int paddingBottom) {
		this.messagePaddingLeft = paddingLeft;
		this.messagePaddingTop = paddingTop;
		this.messagePaddingRight = paddingRight;
		this.messagePaddingBottom = paddingBottom;
	}

	public int getMessagePaddingLeft() {
		return messagePaddingLeft;
	}

	public void setMessagePaddingLeft(int messagePaddingLeft) {
		this.messagePaddingLeft = messagePaddingLeft;
	}

	public int getMessagePaddingTop() {
		return messagePaddingTop;
	}

	public void setMessagePaddingTop(int messagePaddingTop) {
		this.messagePaddingTop = messagePaddingTop;
	}

	public int getMessagePaddingRight() {
		return messagePaddingRight;
	}

	public void setMessagePaddingRight(int messagePaddingRight) {
		this.messagePaddingRight = messagePaddingRight;
	}

	public int getMessagePaddingBottom() {
		return messagePaddingBottom;
	}

	public void setMessagePaddingBottom(int messagePaddingBottom) {
		this.messagePaddingBottom = messagePaddingBottom;
	}

	public void setMessage(String message, OnClickListener messageListener) {
		this.message = message;
		this.messageListener = messageListener;
	}

	public void setMessageListener(OnClickListener messageListener) {
		this.messageListener = messageListener;
	}

	public String getMessage() {
		return message;
	}

	public int getPositiveCurrentBgColor() {
		return positiveCurrentBgColor;
	}

	public void setPositiveCurrentBgColor(int positiveCurrentBgColor) {
		this.positiveCurrentBgColor = positiveCurrentBgColor;
	}

	public int getTempColor() {
		return tempColor;
	}

	public void setTempColor(int tempColor) {
		this.tempColor = tempColor;
	}

}
