package com.huaqing.samplerecord.widget.datepicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.huaqing.samplerecord.R;


/**
 * HourAndMinutePicker
 * Created by ycuwq on 2018/1/22.
 */
public class HourAndMinutePicker extends LinearLayout implements
   HourPicker.OnHourSelectedListener, MinutePicker.OnMinuteSelectedListener {

    private HourPicker mHourPicker;
    private MinutePicker mMinutePicker;
    private OnTimeSelectedListener mOnTimeSelectedListener;

    public HourAndMinutePicker(Context context) {
        this(context, null);
    }

    public HourAndMinutePicker(Context context,  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HourAndMinutePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.layout_time, this);
        initChild();
        initAttrs(context, attrs);
        mHourPicker.setBackgroundDrawable(getBackground());
        mMinutePicker.setBackgroundDrawable(getBackground());
    }

    @Override
    public void onHourSelected(int hour) {
        onTimeSelected();
    }

    @Override
    public void onMinuteSelected(int hour) {
        onTimeSelected();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HourAndMinutePicker);
        int textSize = a.getDimensionPixelSize(R.styleable.HourAndMinutePicker_itemTextSize,
                getResources().getDimensionPixelSize(R.dimen.WheelItemTextSize));
        int textColor = a.getColor(R.styleable.HourAndMinutePicker_itemTextColor,
                Color.BLACK);
        boolean isTextGradual = a.getBoolean(R.styleable.HourAndMinutePicker_textGradual, true);
        boolean isCyclic = a.getBoolean(R.styleable.HourAndMinutePicker_wheelCyclic, true);
        int halfVisibleItemCount = a.getInteger(R.styleable.HourAndMinutePicker_halfVisibleItemCount, 2);
        int selectedItemTextColor = a.getColor(R.styleable.HourAndMinutePicker_selectedTextColor,
                getResources().getColor(R.color.com_ycuwq_datepicker_selectedTextColor));
        int selectedItemTextSize = a.getDimensionPixelSize(R.styleable.HourAndMinutePicker_selectedTextSize,
                getResources().getDimensionPixelSize(R.dimen.WheelSelectedItemTextSize));
        int itemWidthSpace = a.getDimensionPixelSize(R.styleable.HourAndMinutePicker_itemWidthSpace,
                getResources().getDimensionPixelOffset(R.dimen.WheelItemWidthSpace));
        int itemHeightSpace = a.getDimensionPixelSize(R.styleable.HourAndMinutePicker_itemHeightSpace,
                getResources().getDimensionPixelOffset(R.dimen.WheelItemHeightSpace));
        boolean isZoomInSelectedItem = a.getBoolean(R.styleable.HourAndMinutePicker_zoomInSelectedItem, true);
        boolean isShowCurtain = a.getBoolean(R.styleable.HourAndMinutePicker_wheelCurtain, true);
        int curtainColor = a.getColor(R.styleable.HourAndMinutePicker_wheelCurtainColor, Color.WHITE);
        boolean isShowCurtainBorder = a.getBoolean(R.styleable.HourAndMinutePicker_wheelCurtainBorder, true);
        int curtainBorderColor = a.getColor(R.styleable.HourAndMinutePicker_wheelCurtainBorderColor,
                getResources().getColor(R.color.com_ycuwq_datepicker_divider));
        a.recycle();

        setTextSize(textSize);
        setTextColor(textColor);
        setTextGradual(isTextGradual);
        setCyclic(isCyclic);
        setHalfVisibleItemCount(halfVisibleItemCount);
        setSelectedItemTextColor(selectedItemTextColor);
        setSelectedItemTextSize(selectedItemTextSize);
        setItemWidthSpace(itemWidthSpace);
        setItemHeightSpace(itemHeightSpace);
        setZoomInSelectedItem(isZoomInSelectedItem);
        setShowCurtain(isShowCurtain);
        setCurtainColor(curtainColor);
        setShowCurtainBorder(isShowCurtainBorder);
        setCurtainBorderColor(curtainBorderColor);
    }
    private void initChild() {
        mHourPicker = findViewById(R.id.hourPicker_layout_time);
        mHourPicker.setOnHourSelectedListener(this);
        mMinutePicker = findViewById(R.id.minutePicker_layout_time);
        mMinutePicker.setOnMinuteSelectedListener(this);

    }

    private void onTimeSelected() {
        if (mOnTimeSelectedListener != null) {
            mOnTimeSelectedListener.onTimeSelected(getHour(), getMinute());
        }
    }


    /**
     * Sets time.
     *
     * @param hour         the year
     * @param minute        the month
     */
    public void setTime(int hour, int minute) {
        setTime(hour, minute, true);
    }

    /**
     * Sets time.
     *
     * @param hour         the year
     * @param minute        the month
     * @param smoothScroll the smooth scroll
     */
    public void setTime(int hour, int minute, boolean smoothScroll) {
        mHourPicker.setSelectedHour(hour, smoothScroll);
        mMinutePicker.setSelectedMinute(minute, smoothScroll);
    }

    /**
     * Gets hour.
     *
     * @return the hour
     */
    public int getHour() {
        return mHourPicker.getCurrentPosition();
    }


    /**
     * Gets minuute.
     *
     * @return the minute
     */
    public int getMinute() {
        return mMinutePicker.getCurrentPosition();
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (mHourPicker != null) {
            mHourPicker.setBackgroundColor(color);
        }
        if (mMinutePicker != null) {
            mMinutePicker.setBackgroundColor(color);
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        if (mHourPicker != null) {
            mHourPicker.setBackgroundResource(resid);
        }
        if (mMinutePicker != null) {
            mMinutePicker.setBackgroundResource(resid);
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        if (mHourPicker != null) {
            mHourPicker.setBackgroundDrawable(background);
        }
        if (mMinutePicker != null) {
            mMinutePicker.setBackgroundDrawable(background);
        }
    }

    public HourPicker getHourPicker() {
        return mHourPicker;
    }

    public MinutePicker getMinutePicker() {
        return mMinutePicker;
    }

    /**
     * ???????????????????????????
     *
     * @param textColor ????????????
     */
    public void setTextColor(int textColor) {
        mHourPicker.setTextColor(textColor);
        mMinutePicker.setTextColor(textColor);
    }

    /**
     * ???????????????????????????
     *
     * @param textSize ????????????
     */
    public void setTextSize(int textSize) {
        mHourPicker.setTextSize(textSize);
        mMinutePicker.setTextSize(textSize);
    }

    /**
     * ????????????????????????????????????
     *
     * @param selectedItemTextColor ????????????
     */
    public void setSelectedItemTextColor(int selectedItemTextColor) {
        mHourPicker.setSelectedItemTextColor(selectedItemTextColor);
        mMinutePicker.setSelectedItemTextColor(selectedItemTextColor);
    }

    /**
     * ????????????????????????????????????
     *
     * @param selectedItemTextSize ????????????
     */
    public void setSelectedItemTextSize(int selectedItemTextSize) {
        mHourPicker.setSelectedItemTextSize(selectedItemTextSize);
        mMinutePicker.setSelectedItemTextSize(selectedItemTextSize);
    }


    /**
     * ??????????????????????????????????????????
     * ?????????????????????????????????,????????????????????????itemCount = mHalfVisibleItemCount * 2 + 1
     *
     * @param halfVisibleItemCount ??????????????????
     */
    public void setHalfVisibleItemCount(int halfVisibleItemCount) {
        mHourPicker.setHalfVisibleItemCount(halfVisibleItemCount);
        mMinutePicker.setHalfVisibleItemCount(halfVisibleItemCount);
    }

    /**
     * Sets item width space.
     *
     * @param itemWidthSpace the item width space
     */
    public void setItemWidthSpace(int itemWidthSpace) {
        mHourPicker.setItemWidthSpace(itemWidthSpace);
        mMinutePicker.setItemWidthSpace(itemWidthSpace);
    }

    /**
     * ????????????Item???????????????
     *
     * @param itemHeightSpace ?????????
     */
    public void setItemHeightSpace(int itemHeightSpace) {
        mHourPicker.setItemHeightSpace(itemHeightSpace);
        mMinutePicker.setItemHeightSpace(itemHeightSpace);
    }


    /**
     * Set zoom in center item.
     *
     * @param zoomInSelectedItem the zoom in center item
     */
    public void setZoomInSelectedItem(boolean zoomInSelectedItem) {
        mHourPicker.setZoomInSelectedItem(zoomInSelectedItem);
        mMinutePicker.setZoomInSelectedItem(zoomInSelectedItem);
    }

    /**
     * ???????????????????????????
     * set wheel cyclic
     * @param cyclic ????????????????????????
     */
    public void setCyclic(boolean cyclic) {
        mHourPicker.setCyclic(cyclic);
        mMinutePicker.setCyclic(cyclic);
    }

    /**
     * ?????????????????????????????????????????????
     * Set the text color gradient
     * @param textGradual ????????????
     */
    public void setTextGradual(boolean textGradual) {
        mHourPicker.setTextGradual(textGradual);
        mMinutePicker.setTextGradual(textGradual);
    }


    /**
     * ????????????Item?????????????????????
     * set the center item curtain cover
     * @param showCurtain ???????????????
     */
    public void setShowCurtain(boolean showCurtain) {
        mHourPicker.setShowCurtain(showCurtain);
        mMinutePicker.setShowCurtain(showCurtain);
    }

    /**
     * ??????????????????
     * set curtain color
     * @param curtainColor ????????????
     */
    public void setCurtainColor(int curtainColor) {
        mHourPicker.setCurtainColor(curtainColor);
        mMinutePicker.setCurtainColor(curtainColor);
    }

    /**
     * ??????????????????????????????
     * set curtain border
     * @param showCurtainBorder ?????????????????????
     */
    public void setShowCurtainBorder(boolean showCurtainBorder) {
        mHourPicker.setShowCurtainBorder(showCurtainBorder);
        mMinutePicker.setShowCurtainBorder(showCurtainBorder);
    }

    /**
     * ?????????????????????
     * curtain border color
     * @param curtainBorderColor ??????????????????
     */
    public void setCurtainBorderColor(int curtainBorderColor) {
        mHourPicker.setCurtainBorderColor(curtainBorderColor);
        mMinutePicker.setCurtainBorderColor(curtainBorderColor);
    }

    /**
     * ?????????????????????????????????
     * set indicator text
     * @param hourText  ?????????????????????
     * @param minuteText ?????????????????????

     */
    public void setIndicatorText(String hourText, String minuteText) {
        mHourPicker.setIndicatorText(hourText);
        mMinutePicker.setIndicatorText(minuteText);
    }

    /**
     * ??????????????????????????????
     * set indicator text color
     * @param textColor ????????????
     */
    public void setIndicatorTextColor(int textColor) {
        mHourPicker.setIndicatorTextColor(textColor);
        mMinutePicker.setIndicatorTextColor(textColor);
    }

    /**
     * ??????????????????????????????
     *  indicator text size
     * @param textSize ????????????
     */
    public void setIndicatorTextSize(int textSize) {
        mHourPicker.setTextSize(textSize);
        mMinutePicker.setTextSize(textSize);
    }

    /**
     * Sets on date selected listener.
     *
     * @param onTimeSelectedListener the on time selected listener
     */
    public void setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelectedListener) {
        mOnTimeSelectedListener = onTimeSelectedListener;
    }

    /**
     * The interface On date selected listener.
     */
    public interface OnTimeSelectedListener {
        /**
         * On time selected.
         *
         * @param hour  the hour
         * @param minute the minute
         */
        void onTimeSelected(int hour, int minute);
    }
}
