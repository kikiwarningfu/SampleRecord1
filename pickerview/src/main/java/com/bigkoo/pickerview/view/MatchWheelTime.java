package com.bigkoo.pickerview.view;

import android.view.View;

import com.bigkoo.pickerview.R;
import com.bigkoo.pickerview.TimePickerView.Type;
import com.bigkoo.pickerview.adapter.NumericWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class MatchWheelTime {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private View view;

    private WheelView wv_hours;
    private WheelView wv_mins;
    private int gravity;

    private Type type;
    private static final int DEFAULT_START_YEAR = 1900;
    private static final int DEFAULT_END_YEAR = 2100;
    private static final int DEFAULT_START_MONTH = 1;
    private static final int DEFAULT_END_MONTH = 12;
    private static final int DEFAULT_START_DAY = 1;
    private static final int DEFAULT_END_DAY = 31;

    private int startYear = DEFAULT_START_YEAR;
    private int endYear = DEFAULT_END_YEAR;
    private int startMonth = DEFAULT_START_MONTH;
    private int endMonth = DEFAULT_END_MONTH;
    private int startDay = DEFAULT_START_DAY;
    private int endDay = DEFAULT_END_DAY; //表示31天的
    private int currentYear;


    // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
    private int textSize = 18;
    //文字的颜色和分割线的颜色
    int textColorOut;
    int textColorCenter;
    int dividerColor;
    // 条目间距倍数
    float lineSpacingMultiplier = 1.6F;

    private WheelView.DividerType dividerType;

    public MatchWheelTime(View view) {
        super();
        this.view = view;
        type = Type.ALL;
        setView(view);
    }

    public MatchWheelTime(View view, Type type, int gravity, int textSize) {
        super();
        this.view = view;
        this.type = type;
        this.gravity = gravity;
        this.textSize = textSize;
        setView(view);
    }

    public void setPicker(int year, int month, int day) {
        this.setPicker(year, month, day, 0, 0, 0);
    }

    public void setPicker(int year, final int month, int day, int h, int m, int s) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        /*  final Context context = view.getContext();*/
        currentYear = year;


        if (startYear == endYear && startMonth == endMonth) {
            if (list_big.contains(String.valueOf(month + 1))) {
                if (endDay > 31) {
                    endDay = 31;
                }
//                wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                if (endDay > 30) {
                    endDay = 30;
                }
//                wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29;
                    }
//                    wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
                } else {
                    if (endDay > 28) {
                        endDay = 28;
                    }
//                    wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
                }
            }
//            wv_day.setCurrentItem(day - startDay);
        } else if (year == startYear && month + 1 == startMonth) {
            // 起始日期的天数控制
            if (list_big.contains(String.valueOf(month + 1))) {

//                wv_day.setAdapter(new NumericWheelAdapter(startDay, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {

//                wv_day.setAdapter(new NumericWheelAdapter(startDay, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {

//                    wv_day.setAdapter(new NumericWheelAdapter(startDay, 29));
                } else {

//                    wv_day.setAdapter(new NumericWheelAdapter(startDay, 28));
                }
            }
//            wv_day.setCurrentItem(day - startDay);
        } else if (year == endYear && month + 1 == endMonth) {
            // 终止日期的天数控制
            if (list_big.contains(String.valueOf(month + 1))) {
                if (endDay > 31) {
                    endDay = 31;
                }
//                wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                if (endDay > 30) {
                    endDay = 30;
                }
//                wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29;
                    }
//                    wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
                } else {
                    if (endDay > 28) {
                        endDay = 28;
                    }
//                    wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
                }
            }
//            wv_day.setCurrentItem(day - 1);
        } else {
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month + 1))) {

//                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {

//                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {

//                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                } else {

//                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
//            wv_day.setCurrentItem(day - 1);
        }

        /* wv_day.setLabel(context.getString(R.string.pickerview_day));*/

//        wv_day.setGravity(gravity);
        //时
        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
        /*  wv_hours.setLabel(context.getString(R.string.pickerview_hours));// 添加文字*/
        wv_hours.setCurrentItem(h);
        wv_hours.setGravity(gravity);
        //分
        wv_mins = (WheelView) view.findViewById(R.id.min);
        wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
        /* wv_mins.setLabel(context.getString(R.string.pickerview_minutes));// 添加文字*/
        wv_mins.setCurrentItem(m);
        wv_mins.setGravity(gravity);
        //秒
//        wv_seconds = (WheelView) view.findViewById(R.id.second);
//        wv_seconds.setAdapter(new NumericWheelAdapter(0, 59));
        /* wv_seconds.setLabel(context.getString(R.string.pickerview_seconds));// 添加文字*/
//        wv_seconds.setCurrentItem(s);
//        wv_seconds.setGravity(gravity);




        switch (type) {
            case ALL:
                /* textSize = textSize * 3;*/
                break;
            case YEAR_MONTH_DAY:
                /* textSize = textSize * 4;*/
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
//                wv_seconds.setVisibility(View.GONE);
                break;
            case HOURS_MINS:
                /*textSize = textSize * 4;*/
//                wv_year.setVisibility(View.GONE);
//                wv_month.setVisibility(View.GONE);
//                wv_day.setVisibility(View.GONE);
//                wv_seconds.setVisibility(View.GONE);
                break;
            case MONTH_DAY_HOUR_MIN:
                /* textSize = textSize * 3;*/
//                wv_year.setVisibility(View.GONE);
//                wv_seconds.setVisibility(View.GONE);
                break;
            case YEAR_MONTH:
                /* textSize = textSize * 4;*/
//                wv_day.setVisibility(View.GONE);
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
//                wv_seconds.setVisibility(View.GONE);
            case YEAR_MONTH_DAY_HOUR_MIN:
                /* textSize = textSize * 4;*/

//                wv_seconds.setVisibility(View.GONE);
        }
        setContentTextSize();
    }




    private void setContentTextSize() {
//        wv_day.setTextSize(textSize);
//        wv_month.setTextSize(textSize);
//        wv_year.setTextSize(textSize);
        wv_hours.setTextSize(textSize);
        wv_mins.setTextSize(textSize);
//        wv_seconds.setTextSize(textSize);
    }

    private void setTextColorOut() {
//        wv_day.setTextColorOut(textColorOut);
//        wv_month.setTextColorOut(textColorOut);
//        wv_year.setTextColorOut(textColorOut);
        wv_hours.setTextColorOut(textColorOut);
        wv_mins.setTextColorOut(textColorOut);
//        wv_seconds.setTextColorOut(textColorOut);
    }

    private void setTextColorCenter() {
//        wv_day.setTextColorCenter(textColorCenter);
//        wv_month.setTextColorCenter(textColorCenter);
//        wv_year.setTextColorCenter(textColorCenter);
        wv_hours.setTextColorCenter(textColorCenter);
        wv_mins.setTextColorCenter(textColorCenter);
//        wv_seconds.setTextColorCenter(textColorCenter);
    }

    private void setDividerColor() {
//        wv_day.setDividerColor(dividerColor);
//        wv_month.setDividerColor(dividerColor);
//        wv_year.setDividerColor(dividerColor);
        wv_hours.setDividerColor(dividerColor);
        wv_mins.setDividerColor(dividerColor);
//        wv_seconds.setDividerColor(dividerColor);
    }

    private void setDividerType() {

//        wv_day.setDividerType(dividerType);
//        wv_month.setDividerType(dividerType);
//        wv_year.setDividerType(dividerType);
        wv_hours.setDividerType(dividerType);
        wv_mins.setDividerType(dividerType);
//        wv_seconds.setDividerType(dividerType);

    }

    private void setLineSpacingMultiplier() {

        wv_hours.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_mins.setLineSpacingMultiplier(lineSpacingMultiplier);
    }

    public void setLabels(String label_year, String label_month, String label_day, String label_hours, String label_mins, String label_seconds) {
        if (label_year != null) {
//            wv_year.setLabel(label_year);
        } else {
//            wv_year.setLabel(view.getContext().getString(R.string.pickerview_year));
        }
        if (label_month != null) {
//            wv_month.setLabel(label_month);
        } else {
//            wv_month.setLabel(view.getContext().getString(R.string.pickerview_month));
        }
        if (label_day != null) {
//            wv_day.setLabel(label_day);
        } else {
//            wv_day.setLabel(view.getContext().getString(R.string.pickerview_day));
        }
        if (label_hours != null) {
            wv_hours.setLabel(label_hours);
        } else {
            wv_hours.setLabel(view.getContext().getString(R.string.pickerview_hours));
        }
        if (label_mins != null) {
            wv_mins.setLabel(label_mins);
        } else {
            wv_mins.setLabel(view.getContext().getString(R.string.pickerview_minutes));
        }
        if (label_seconds != null) {
//            wv_seconds.setLabel(label_seconds);
        } else {
//            wv_seconds.setLabel(view.getContext().getString(R.string.pickerview_seconds));
        }

    }


    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
//        wv_year.setCyclic(cyclic);
//        wv_month.setCyclic(cyclic);
//        wv_day.setCyclic(cyclic);
        wv_hours.setCyclic(cyclic);
        wv_mins.setCyclic(cyclic);
//        wv_seconds.setCyclic(cyclic);
    }

    public String getTime() {
        StringBuffer sb = new StringBuffer();
//        if (currentYear == startYear) {
           /* int i = wv_month.getCurrentItem() + startMonth;
            System.out.println("i:" + i);*/

            sb.append(
                    wv_hours.getCurrentItem()).append(":")
                    .append(wv_mins.getCurrentItem());


//        }

        return sb.toString();


//        if (currentYear == startYear) {
//           /* int i = wv_month.getCurrentItem() + startMonth;
//            System.out.println("i:" + i);*/
//            if ((wv_month.getCurrentItem() + startMonth) == startMonth) {
//                sb.append((wv_year.getCurrentItem() + startYear)).append("-")
//                        .append((wv_month.getCurrentItem() + startMonth)).append("-")
//                        .append((wv_day.getCurrentItem() + startDay)).append(" ")
//                        .append(wv_hours.getCurrentItem()).append(":")
//                        .append(wv_mins.getCurrentItem()).append(":")
//                        .append(wv_seconds.getCurrentItem());
//            } else {
//                sb.append((wv_year.getCurrentItem() + startYear)).append("-")
//                        .append((wv_month.getCurrentItem() + startMonth)).append("-")
//                        .append((wv_day.getCurrentItem() + 1)).append(" ")
//                        .append(wv_hours.getCurrentItem()).append(":")
//                        .append(wv_mins.getCurrentItem()).append(":")
//                        .append(wv_seconds.getCurrentItem());
//            }
//
//
//        } else {
//            sb.append((wv_year.getCurrentItem() + startYear)).append("-")
//                    .append((wv_month.getCurrentItem() + 1)).append("-")
//                    .append((wv_day.getCurrentItem() + 1)).append(" ")
//                    .append(wv_hours.getCurrentItem()).append(":")
//                    .append(wv_mins.getCurrentItem()).append(":")
//                    .append(wv_seconds.getCurrentItem());
//        }
//
//        return sb.toString();
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }


    public void setRangDate(Calendar startDate, Calendar endDate) {

        if (startDate == null && endDate != null) {
            int year = endDate.get(Calendar.YEAR);
            int month = endDate.get(Calendar.MONTH) + 1;
            int day = endDate.get(Calendar.DAY_OF_MONTH);
            if (year > startYear) {
                this.endYear = year;
                this.endMonth = month;
                this.endDay = day;
            } else if (year == startYear) {
                if (month > startMonth) {
                    this.endYear = year;
                    this.endMonth = month;
                    this.endDay = day;
                } else if (month == startMonth) {
                    if (month > startDay) {
                        this.endYear = year;
                        this.endMonth = month;
                        this.endDay = day;
                    }
                }
            }

        } else if (startDate != null && endDate == null) {
            int year = startDate.get(Calendar.YEAR);
            int month = startDate.get(Calendar.MONTH) + 1;
            int day = startDate.get(Calendar.DAY_OF_MONTH);
            if (year < endYear) {
                this.startMonth = month;
                this.startDay = day;
                this.startYear = year;
            } else if (year == endYear) {
                if (month < endMonth) {
                    this.startMonth = month;
                    this.startDay = day;
                    this.startYear = year;
                } else if (month == endMonth) {
                    if (day < endDay) {
                        this.startMonth = month;
                        this.startDay = day;
                        this.startYear = year;
                    }
                }
            }

        } else if (startDate != null && endDate != null) {
            this.startYear = startDate.get(Calendar.YEAR);
            this.endYear = endDate.get(Calendar.YEAR);
            this.startMonth = startDate.get(Calendar.MONTH) + 1;
            this.endMonth = endDate.get(Calendar.MONTH) + 1;
            this.startDay = startDate.get(Calendar.DAY_OF_MONTH);
            this.endDay = endDate.get(Calendar.DAY_OF_MONTH);


        }


    }


    /**
     * 设置间距倍数,但是只能在1.0-2.0f之间
     *
     * @param lineSpacingMultiplier
     */
    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        this.lineSpacingMultiplier = lineSpacingMultiplier;
        setLineSpacingMultiplier();
    }

    /**
     * 设置分割线的颜色
     *
     * @param dividerColor
     */
    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        setDividerColor();
    }

    /**
     * 设置分割线的类型
     *
     * @param dividerType
     */
    public void setDividerType(WheelView.DividerType dividerType) {
        this.dividerType = dividerType;
        setDividerType();
    }

    /**
     * 设置分割线之间的文字的颜色
     *
     * @param textColorCenter
     */
    public void setTextColorCenter(int textColorCenter) {
        this.textColorCenter = textColorCenter;
        setTextColorCenter();
    }

    /**
     * 设置分割线以外文字的颜色
     *
     * @param textColorOut
     */
    public void setTextColorOut(int textColorOut) {
        this.textColorOut = textColorOut;
        setTextColorOut();
    }

    /**
     * Label 是否只显示中间选中项的
     *
     * @param isCenterLabel
     */

    public void isCenterLabel(Boolean isCenterLabel) {

//        wv_day.isCenterLabel(isCenterLabel);
//        wv_month.isCenterLabel(isCenterLabel);
//        wv_year.isCenterLabel(isCenterLabel);
        wv_hours.isCenterLabel(isCenterLabel);
        wv_mins.isCenterLabel(isCenterLabel);
//        wv_seconds.isCenterLabel(isCenterLabel);
    }
}
