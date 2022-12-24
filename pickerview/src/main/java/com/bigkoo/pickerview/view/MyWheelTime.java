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


public class MyWheelTime {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private View viewMin;
    private View viewMax;

    private WheelView wv_yearMax;
    private WheelView wv_monthMax;
    private WheelView wv_dayMax;
    private WheelView wv_hoursMax;
    private WheelView wv_minsMax;
    private WheelView wv_secondsMax;

    private WheelView wv_yearMin;
    private WheelView wv_monthMin;
    private WheelView wv_dayMin;
    private WheelView wv_hoursMin;
    private WheelView wv_minsMin;
    private WheelView wv_secondMin;

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
    private int littleCurrentYear;

    // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
    private int textSize = 18;
    //文字的颜色和分割线的颜色
    int textColorOut;
    int textColorCenter;
    int dividerColor;
    // 条目间距倍数
    float lineSpacingMultiplier = 1.6F;

    private WheelView.DividerType dividerType;

    public MyWheelTime(View viewMin, View viewMax) {
        super();
        this.viewMin = viewMin;
        type = Type.ALL;
        this.viewMax = viewMax;
        setViewMin(viewMin);
        setViewMax(viewMax);
    }

    public MyWheelTime(View viewMin, View viewMax, Type type, int gravity, int textSize) {
        super();
        this.viewMin = viewMin;
        this.type = type;
        this.gravity = gravity;
        this.textSize = textSize;
        this.viewMax = viewMax;
        setViewMin(viewMin);
        setViewMax(viewMax);
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
        littleCurrentYear = year;

        // 年
        wv_yearMax = (WheelView) viewMax.findViewById(R.id.year_max);
        wv_yearMax.setAdapter(new NumericWheelAdapter(startYear, endYear));// 设置"年"的显示数据
        /*wv_year.setLabel(context.getString(R.string.pickerview_year));// 添加文字*/
        wv_yearMax.setCurrentItem(year - startYear);// 初始化时显示的数据
        wv_yearMax.setGravity(gravity);
        /*minYear*/
        wv_yearMin=(WheelView)viewMin.findViewById(R.id.year_min);
        wv_yearMin.setAdapter(new NumericWheelAdapter(startYear,endYear));
        wv_yearMin.setCurrentItem(year-startYear);
        wv_yearMin.setGravity(gravity);
        // 月
        wv_monthMax = (WheelView) viewMax.findViewById(R.id.month_max);
        if (startYear == endYear) {//开始年等于终止年
            wv_monthMax.setAdapter(new NumericWheelAdapter(startMonth, endMonth));
            wv_monthMax.setCurrentItem(month + 1 - startMonth);
        } else if (year == startYear) {
            //起始日期的月份控制
            wv_monthMax.setAdapter(new NumericWheelAdapter(startMonth, 12));
            wv_monthMax.setCurrentItem(month + 1 - startMonth);
        } else if (year == endYear) {
            //终止日期的月份控制
            wv_monthMax.setAdapter(new NumericWheelAdapter(1, endMonth));
            wv_monthMax.setCurrentItem(month);
        } else {
            wv_monthMax.setAdapter(new NumericWheelAdapter(1, 12));
            wv_monthMax.setCurrentItem(month);
        }
        /*   wv_month.setLabel(context.getString(R.string.pickerview_month));*/

        wv_monthMax.setGravity(gravity);
        /*monthMin*/
        wv_monthMin = (WheelView) viewMin.findViewById(R.id.month_min);
        if (startYear == endYear) {//开始年等于终止年
            wv_monthMin.setAdapter(new NumericWheelAdapter(startMonth, endMonth));
            wv_monthMin.setCurrentItem(month + 1 - startMonth);
        } else if (year == startYear) {
            //起始日期的月份控制
            wv_monthMin.setAdapter(new NumericWheelAdapter(startMonth, 12));
            wv_monthMin.setCurrentItem(month + 1 - startMonth);
        } else if (year == endYear) {
            //终止日期的月份控制
            wv_monthMin.setAdapter(new NumericWheelAdapter(1, endMonth));
            wv_monthMin.setCurrentItem(month);
        } else {
            wv_monthMin.setAdapter(new NumericWheelAdapter(1, 12));
            wv_monthMin.setCurrentItem(month);
        }
        /*   wv_month.setLabel(context.getString(R.string.pickerview_month));*/
        wv_monthMin.setGravity(gravity);

        // 日
        wv_dayMax = (WheelView) viewMax.findViewById(R.id.day_max);
        if (startYear == endYear && startMonth == endMonth) {
            if (list_big.contains(String.valueOf(month + 1))) {
                if (endDay > 31) {
                    endDay = 31;
                }
                wv_dayMax.setAdapter(new NumericWheelAdapter(startDay, endDay));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                if (endDay > 30) {
                    endDay = 30;
                }
                wv_dayMax.setAdapter(new NumericWheelAdapter(startDay, endDay));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29;
                    }
                    wv_dayMax.setAdapter(new NumericWheelAdapter(startDay, endDay));
                } else {
                    if (endDay > 28) {
                        endDay = 28;
                    }
                    wv_dayMax.setAdapter(new NumericWheelAdapter(startDay, endDay));
                }
            }
            wv_dayMax.setCurrentItem(day - startDay);
        } else if (year == startYear && month + 1 == startMonth) {
            // 起始日期的天数控制
            if (list_big.contains(String.valueOf(month + 1))) {

                wv_dayMax.setAdapter(new NumericWheelAdapter(startDay, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {

                wv_dayMax.setAdapter(new NumericWheelAdapter(startDay, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {

                    wv_dayMax.setAdapter(new NumericWheelAdapter(startDay, 29));
                } else {

                    wv_dayMax.setAdapter(new NumericWheelAdapter(startDay, 28));
                }
            }
            wv_dayMax.setCurrentItem(day - startDay);
        } else if (year == endYear && month + 1 == endMonth) {
            // 终止日期的天数控制
            if (list_big.contains(String.valueOf(month + 1))) {
                if (endDay > 31) {
                    endDay = 31;
                }
                wv_dayMax.setAdapter(new NumericWheelAdapter(1, endDay));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                if (endDay > 30) {
                    endDay = 30;
                }
                wv_dayMax.setAdapter(new NumericWheelAdapter(1, endDay));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29;
                    }
                    wv_dayMax.setAdapter(new NumericWheelAdapter(1, endDay));
                } else {
                    if (endDay > 28) {
                        endDay = 28;
                    }
                    wv_dayMax.setAdapter(new NumericWheelAdapter(1, endDay));
                }
            }
            wv_dayMax.setCurrentItem(day - 1);
        } else {
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month + 1))) {

                wv_dayMax.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {

                wv_dayMax.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {

                    wv_dayMax.setAdapter(new NumericWheelAdapter(1, 29));
                } else {

                    wv_dayMax.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
            wv_dayMax.setCurrentItem(day - 1);
        }

        /* wv_day.setLabel(context.getString(R.string.pickerview_day));*/

        wv_dayMax.setGravity(gravity);
        /*dayMin*/

        wv_dayMin = (WheelView) viewMin.findViewById(R.id.day_min);
        if (startYear == endYear && startMonth == endMonth) {
            if (list_big.contains(String.valueOf(month + 1))) {
                if (endDay > 31) {
                    endDay = 31;
                }
                wv_dayMin.setAdapter(new NumericWheelAdapter(startDay, endDay));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                if (endDay > 30) {
                    endDay = 30;
                }
                wv_dayMin.setAdapter(new NumericWheelAdapter(startDay, endDay));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29;
                    }
                    wv_dayMin.setAdapter(new NumericWheelAdapter(startDay, endDay));
                } else {
                    if (endDay > 28) {
                        endDay = 28;
                    }
                    wv_dayMin.setAdapter(new NumericWheelAdapter(startDay, endDay));
                }
            }
            wv_dayMin.setCurrentItem(day - startDay);
        } else if (year == startYear && month + 1 == startMonth) {
            // 起始日期的天数控制
            if (list_big.contains(String.valueOf(month + 1))) {

                wv_dayMin.setAdapter(new NumericWheelAdapter(startDay, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {

                wv_dayMin.setAdapter(new NumericWheelAdapter(startDay, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {

                    wv_dayMin.setAdapter(new NumericWheelAdapter(startDay, 29));
                } else {

                    wv_dayMin.setAdapter(new NumericWheelAdapter(startDay, 28));
                }
            }
            wv_dayMin.setCurrentItem(day - startDay);
        } else if (year == endYear && month + 1 == endMonth) {
            // 终止日期的天数控制
            if (list_big.contains(String.valueOf(month + 1))) {
                if (endDay > 31) {
                    endDay = 31;
                }
                wv_dayMin.setAdapter(new NumericWheelAdapter(1, endDay));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                if (endDay > 30) {
                    endDay = 30;
                }
                wv_dayMin.setAdapter(new NumericWheelAdapter(1, endDay));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29;
                    }
                    wv_dayMin.setAdapter(new NumericWheelAdapter(1, endDay));
                } else {
                    if (endDay > 28) {
                        endDay = 28;
                    }
                    wv_dayMin.setAdapter(new NumericWheelAdapter(1, endDay));
                }
            }
            wv_dayMin.setCurrentItem(day - 1);
        } else {
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month + 1))) {

                wv_dayMin.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {

                wv_dayMin.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {

                    wv_dayMin.setAdapter(new NumericWheelAdapter(1, 29));
                } else {

                    wv_dayMin.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
            wv_dayMin.setCurrentItem(day - 1);
        }

        /* wv_day.setLabel(context.getString(R.string.pickerview_day));*/
        wv_dayMin.setGravity(gravity);


        //时
        wv_hoursMax = (WheelView) viewMax.findViewById(R.id.hour_max);
        wv_hoursMax.setAdapter(new NumericWheelAdapter(0, 23));
        /*  wv_hours.setLabel(context.getString(R.string.pickerview_hours));// 添加文字*/
        wv_hoursMax.setCurrentItem(h);
        wv_hoursMax.setGravity(gravity);

        /*hourMin*/
        wv_hoursMin = (WheelView) viewMin.findViewById(R.id.hour_min);
        wv_hoursMin.setAdapter(new NumericWheelAdapter(0, 23));
        /*  wv_hours.setLabel(context.getString(R.string.pickerview_hours));// 添加文字*/
        wv_hoursMin.setCurrentItem(h);
        wv_hoursMin.setGravity(gravity);

        //分
        wv_minsMax = (WheelView) viewMax.findViewById(R.id.min_max);
        wv_minsMax.setAdapter(new NumericWheelAdapter(0, 59));
        /* wv_mins.setLabel(context.getString(R.string.pickerview_minutes));// 添加文字*/
        wv_minsMax.setCurrentItem(m);
        wv_minsMax.setGravity(gravity);
        /*minuteMin*/
        wv_minsMin = (WheelView) viewMin.findViewById(R.id.min_min);
        wv_minsMin.setAdapter(new NumericWheelAdapter(0, 59));
        /* wv_mins.setLabel(context.getString(R.string.pickerview_minutes));// 添加文字*/
        wv_minsMin.setCurrentItem(m);
        wv_minsMin.setGravity(gravity);

        //秒
        wv_secondsMax = (WheelView) viewMax.findViewById(R.id.second_max);
        wv_secondsMax.setAdapter(new NumericWheelAdapter(0, 59));
        /* wv_seconds.setLabel(context.getString(R.string.pickerview_seconds));// 添加文字*/
        wv_secondsMax.setCurrentItem(s);
        wv_secondsMax.setGravity(gravity);

        wv_secondMin = (WheelView) viewMin.findViewById(R.id.second_min);
        wv_secondMin.setAdapter(new NumericWheelAdapter(0, 59));
        /* wv_seconds.setLabel(context.getString(R.string.pickerview_seconds));// 添加文字*/
        wv_secondMin.setCurrentItem(s);
        wv_secondMin.setGravity(gravity);

        // 添加"年"监听
        OnItemSelectedListener wheelListener_year_max = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int year_num = index + startYear;
                currentYear = year_num;
                int currentMonthItem = wv_monthMax.getCurrentItem();//记录上一次的item位置
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (startYear == endYear) {
                    //重新设置月份
                    wv_monthMax.setAdapter(new NumericWheelAdapter(startMonth, endMonth));

                    if (currentMonthItem > wv_monthMax.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_monthMax.getAdapter().getItemsCount() - 1;
                        wv_monthMax.setCurrentItem(currentMonthItem);
                    }

                    int monthNum = currentMonthItem + startMonth;

                    if (startMonth == endMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, startDay, endDay, list_big, list_little);
                    } else if (monthNum == startMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, startDay, 31, list_big, list_little);
                    } else {
                        //重新设置日
                        setReDay(year_num, monthNum, 1, 31, list_big, list_little);
                    }
                } else if (year_num == startYear) {//等于开始的年
                    //重新设置月份
                    wv_monthMax.setAdapter(new NumericWheelAdapter(startMonth, 12));

                    if (currentMonthItem > wv_monthMax.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_monthMax.getAdapter().getItemsCount() - 1;
                        wv_monthMax.setCurrentItem(currentMonthItem);
                    }

                    int month = currentMonthItem + startMonth;
                    if (month == startMonth) {

                        //重新设置日
                        setReDay(year_num, month, startDay, 31, list_big, list_little);
                    } else {
                        //重新设置日

                        setReDay(year_num, month, 1, 31, list_big, list_little);
                    }

                } else if (year_num == endYear) {
                    //重新设置月份
                    wv_monthMax.setAdapter(new NumericWheelAdapter(1, endMonth));
                    if (currentMonthItem > wv_monthMax.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_monthMax.getAdapter().getItemsCount() - 1;
                        wv_monthMax.setCurrentItem(currentMonthItem);
                    }
                    int monthNum = currentMonthItem + 1;

                    if (monthNum == endMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, 1, endDay, list_big, list_little);
                    } else {
                        //重新设置日
                        setReDay(year_num, monthNum, 1, 31, list_big, list_little);
                    }

                } else {
                    //重新设置月份
                    wv_monthMax.setAdapter(new NumericWheelAdapter(1, 12));
                    //重新设置日
                    setReDay(year_num, wv_monthMax.getCurrentItem() + 1, 1, 31, list_big, list_little);

                }

            }
        };

        // 添加"月"监听
        OnItemSelectedListener wheelListener_month_max = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int month_num = index + 1;

                if (startYear == endYear) {
                    month_num = month_num + startMonth - 1;
                    if (startMonth == endMonth) {
                        //重新设置日
                        setReDay(currentYear, month_num, startDay, endDay, list_big, list_little);
                    } else if (startMonth == month_num) {

                        //重新设置日
                        setReDay(currentYear, month_num, startDay, 31, list_big, list_little);
                    } else if (endMonth == month_num) {
                        setReDay(currentYear, month_num, 1, endDay, list_big, list_little);
                    } else {
                        setReDay(currentYear, month_num, 1, 31, list_big, list_little);
                    }
                } else if (currentYear == startYear) {
                    month_num = month_num + startMonth - 1;
                    if (month_num == startMonth) {
                        //重新设置日
                        setReDay(currentYear, month_num, startDay, 31, list_big, list_little);
                    } else {
                        //重新设置日
                        setReDay(currentYear, month_num, 1, 31, list_big, list_little);
                    }

                } else if (currentYear == endYear) {
                    if (month_num == endMonth) {
                        //重新设置日
                        setReDay(currentYear, wv_monthMax.getCurrentItem() + 1, 1, endDay, list_big, list_little);
                    } else {
                        setReDay(currentYear, wv_monthMax.getCurrentItem() + 1, 1, 31, list_big, list_little);
                    }
                } else {
                    //重新设置日
                    setReDay(currentYear, month_num, 1, 31, list_big, list_little);
                }
            }
        };
        wv_yearMax.setOnItemSelectedListener(wheelListener_year_max);
        wv_yearMin.setOnItemSelectedListener(wheelListener_year_max);
        wv_monthMax.setOnItemSelectedListener(wheelListener_month_max);
        wv_monthMin.setOnItemSelectedListener(wheelListener_month_max);

        // 添加"年"监听
        OnItemSelectedListener wheelListener_yearMin = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int year_num = index + startYear;
                currentYear = year_num;
                int currentMonthItem = wv_monthMin.getCurrentItem();//记录上一次的item位置
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (startYear == endYear) {
                    //重新设置月份
                    wv_monthMin.setAdapter(new NumericWheelAdapter(startMonth, endMonth));

                    if (currentMonthItem > wv_monthMin.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_monthMin.getAdapter().getItemsCount() - 1;
                        wv_monthMin.setCurrentItem(currentMonthItem);
                    }

                    int monthNum = currentMonthItem + startMonth;

                    if (startMonth == endMonth) {
                        //重新设置日
                        setReDayMin(year_num, monthNum, startDay, endDay, list_big, list_little);
                    } else if (monthNum == startMonth) {
                        //重新设置日
                        setReDayMin(year_num, monthNum, startDay, 31, list_big, list_little);
                    } else {
                        //重新设置日
                        setReDayMin(year_num, monthNum, 1, 31, list_big, list_little);
                    }
                } else if (year_num == startYear) {//等于开始的年
                    //重新设置月份
                    wv_monthMin.setAdapter(new NumericWheelAdapter(startMonth, 12));

                    if (currentMonthItem > wv_monthMin.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_monthMin.getAdapter().getItemsCount() - 1;
                        wv_monthMin.setCurrentItem(currentMonthItem);
                    }

                    int month = currentMonthItem + startMonth;
                    if (month == startMonth) {

                        //重新设置日
                        setReDayMin(year_num, month, startDay, 31, list_big, list_little);
                    } else {
                        //重新设置日

                        setReDayMin(year_num, month, 1, 31, list_big, list_little);
                    }

                } else if (year_num == endYear) {
                    //重新设置月份
                    wv_monthMin.setAdapter(new NumericWheelAdapter(1, endMonth));
                    if (currentMonthItem > wv_monthMin.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_monthMin.getAdapter().getItemsCount() - 1;
                        wv_monthMin.setCurrentItem(currentMonthItem);
                    }
                    int monthNum = currentMonthItem + 1;

                    if (monthNum == endMonth) {
                        //重新设置日
                        setReDayMin(year_num, monthNum, 1, endDay, list_big, list_little);
                    } else {
                        //重新设置日
                        setReDayMin(year_num, monthNum, 1, 31, list_big, list_little);
                    }

                } else {
                    //重新设置月份
                    wv_monthMin.setAdapter(new NumericWheelAdapter(1, 12));
                    //重新设置日
                    setReDayMin(year_num, wv_monthMin.getCurrentItem() + 1, 1, 31, list_big, list_little);

                }

            }
        };

        // 添加"月"监听
        OnItemSelectedListener wheelListener_monthMin = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int month_num = index + 1;

                if (startYear == endYear) {
                    month_num = month_num + startMonth - 1;
                    if (startMonth == endMonth) {
                        //重新设置日
                        setReDayMin(currentYear, month_num, startDay, endDay, list_big, list_little);
                    } else if (startMonth == month_num) {

                        //重新设置日
                        setReDayMin(currentYear, month_num, startDay, 31, list_big, list_little);
                    } else if (endMonth == month_num) {
                        setReDayMin(currentYear, month_num, 1, endDay, list_big, list_little);
                    } else {
                        setReDayMin(currentYear, month_num, 1, 31, list_big, list_little);
                    }
                } else if (currentYear == startYear) {
                    month_num = month_num + startMonth - 1;
                    if (month_num == startMonth) {
                        //重新设置日
                        setReDayMin(currentYear, month_num, startDay, 31, list_big, list_little);
                    } else {
                        //重新设置日
                        setReDayMin(currentYear, month_num, 1, 31, list_big, list_little);
                    }

                } else if (currentYear == endYear) {
                    if (month_num == endMonth) {
                        //重新设置日
                        setReDayMin(currentYear, wv_monthMin.getCurrentItem() + 1, 1, endDay, list_big, list_little);
                    } else {
                        setReDayMin(currentYear, wv_monthMin.getCurrentItem() + 1, 1, 31, list_big, list_little);
                    }

                } else {
                    //重新设置日
                    setReDayMin(currentYear, month_num, 1, 31, list_big, list_little);

                }


            }
        };
        wv_yearMax.setOnItemSelectedListener(wheelListener_year_max);
        wv_yearMin.setOnItemSelectedListener(wheelListener_yearMin);
        wv_monthMax.setOnItemSelectedListener(wheelListener_month_max);
        wv_monthMin.setOnItemSelectedListener(wheelListener_monthMin);
        switch (type) {
            case ALL:
                /* textSize = textSize * 3;*/
                break;
            case YEAR_MONTH_DAY:
                /* textSize = textSize * 4;*/
                wv_hoursMax.setVisibility(View.GONE);
                wv_minsMax.setVisibility(View.GONE);
                wv_secondsMax.setVisibility(View.GONE);
                wv_hoursMin.setVisibility(View.GONE);
                wv_minsMin.setVisibility(View.GONE);
                wv_secondMin.setVisibility(View.GONE);

                break;
            case HOURS_MINS:
                /*textSize = textSize * 4;*/
                wv_yearMax.setVisibility(View.GONE);
                wv_monthMax.setVisibility(View.GONE);
                wv_dayMax.setVisibility(View.GONE);
                wv_secondsMax.setVisibility(View.GONE);

                wv_yearMin.setVisibility(View.GONE);
                wv_monthMin.setVisibility(View.GONE);
                wv_dayMin.setVisibility(View.GONE);
                wv_secondMin.setVisibility(View.GONE);
                break;
            case MONTH_DAY_HOUR_MIN:
                /* textSize = textSize * 3;*/
                wv_yearMax.setVisibility(View.GONE);
                wv_secondsMax.setVisibility(View.GONE);
                wv_yearMin.setVisibility(View.GONE);
                wv_secondMin.setVisibility(View.GONE);
                break;
            case YEAR_MONTH:
                /* textSize = textSize * 4;*/
                wv_dayMax.setVisibility(View.GONE);
                wv_hoursMax.setVisibility(View.GONE);
                wv_minsMax.setVisibility(View.GONE);
                wv_secondsMax.setVisibility(View.GONE);

                wv_dayMin.setVisibility(View.GONE);
                wv_hoursMin.setVisibility(View.GONE);
                wv_minsMin.setVisibility(View.GONE);
                wv_secondMin.setVisibility(View.GONE);
            case YEAR_MONTH_DAY_HOUR_MIN:
                /* textSize = textSize * 4;*/

                wv_secondsMax.setVisibility(View.GONE);
                wv_secondMin.setVisibility(View.GONE);

        }
        setContentTextSize();
    }
    private void setReDayMin(int year_num, int monthNum, int startD, int endD, List<String> list_big, List<String> list_little) {
        int currentItem = wv_dayMin.getCurrentItem();

        int maxItem;
        if (list_big
                .contains(String.valueOf(monthNum))) {
            if (endD > 31) {
                endD = 31;
            }
            wv_dayMin.setAdapter(new NumericWheelAdapter(startD, endD));
            maxItem = endD;
        } else if (list_little.contains(String.valueOf(monthNum))) {
            if (endD > 30) {
                endD = 30;
            }
            wv_dayMin.setAdapter(new NumericWheelAdapter(startD, endD));
            maxItem = endD;
        } else {
            if ((year_num % 4 == 0 && year_num % 100 != 0)
                    || year_num % 400 == 0) {
                if (endD > 29) {
                    endD = 29;
                }
                wv_dayMin.setAdapter(new NumericWheelAdapter(startD, endD));
                maxItem = endD;
            } else {
                if (endD > 28) {
                    endD = 28;
                }
                wv_dayMin.setAdapter(new NumericWheelAdapter(startD, endD));
                maxItem = endD;
            }
        }

        if (currentItem > wv_dayMin.getAdapter().getItemsCount() - 1) {
            currentItem = wv_dayMin.getAdapter().getItemsCount() - 1;
            wv_dayMin.setCurrentItem(currentItem);
        }

    }

    private void setReDay(int year_num, int monthNum, int startD, int endD, List<String> list_big, List<String> list_little) {
        int currentItem = wv_dayMax.getCurrentItem();

        int maxItem;
        if (list_big
                .contains(String.valueOf(monthNum))) {
            if (endD > 31) {
                endD = 31;
            }
            wv_dayMax.setAdapter(new NumericWheelAdapter(startD, endD));
            maxItem = endD;
        } else if (list_little.contains(String.valueOf(monthNum))) {
            if (endD > 30) {
                endD = 30;
            }
            wv_dayMax.setAdapter(new NumericWheelAdapter(startD, endD));
            maxItem = endD;
        } else {
            if ((year_num % 4 == 0 && year_num % 100 != 0)
                    || year_num % 400 == 0) {
                if (endD > 29) {
                    endD = 29;
                }
                wv_dayMax.setAdapter(new NumericWheelAdapter(startD, endD));
                maxItem = endD;
            } else {
                if (endD > 28) {
                    endD = 28;
                }
                wv_dayMax.setAdapter(new NumericWheelAdapter(startD, endD));
                maxItem = endD;
            }
        }

        if (currentItem > wv_dayMax.getAdapter().getItemsCount() - 1) {
            currentItem = wv_dayMax.getAdapter().getItemsCount() - 1;
            wv_dayMax.setCurrentItem(currentItem);
        }

    }


    private void setContentTextSize() {
        wv_dayMax.setTextSize(textSize);
        wv_monthMax.setTextSize(textSize);
        wv_yearMax.setTextSize(textSize);
        wv_hoursMax.setTextSize(textSize);
        wv_minsMax.setTextSize(textSize);
        wv_secondsMax.setTextSize(textSize);

        wv_dayMin.setTextSize(textSize);
        wv_monthMin.setTextSize(textSize);
        wv_yearMin.setTextSize(textSize);
        wv_hoursMin.setTextSize(textSize);
        wv_minsMin.setTextSize(textSize);
        wv_secondMin.setTextSize(textSize);
    }

    private void setTextColorOut() {
        wv_dayMax.setTextColorOut(textColorOut);
        wv_monthMax.setTextColorOut(textColorOut);
        wv_yearMax.setTextColorOut(textColorOut);
        wv_hoursMax.setTextColorOut(textColorOut);
        wv_minsMax.setTextColorOut(textColorOut);
        wv_secondsMax.setTextColorOut(textColorOut);

        wv_dayMin.setTextColorOut(textColorOut);
        wv_monthMin.setTextColorOut(textColorOut);
        wv_yearMin.setTextColorOut(textColorOut);
        wv_hoursMin.setTextColorOut(textColorOut);
        wv_minsMin.setTextColorOut(textColorOut);
        wv_secondMin.setTextColorOut(textColorOut);
    }

    private void setTextColorCenter() {
        wv_dayMax.setTextColorCenter(textColorCenter);
        wv_monthMax.setTextColorCenter(textColorCenter);
        wv_yearMax.setTextColorCenter(textColorCenter);
        wv_hoursMax.setTextColorCenter(textColorCenter);
        wv_minsMax.setTextColorCenter(textColorCenter);
        wv_secondsMax.setTextColorCenter(textColorCenter);

        wv_dayMin.setTextColorCenter(textColorCenter);
        wv_monthMin.setTextColorCenter(textColorCenter);
        wv_yearMin.setTextColorCenter(textColorCenter);
        wv_hoursMin.setTextColorCenter(textColorCenter);
        wv_minsMin.setTextColorCenter(textColorCenter);
        wv_secondMin.setTextColorCenter(textColorCenter);
    }

    private void setDividerColor() {
        wv_dayMax.setDividerColor(dividerColor);
        wv_monthMax.setDividerColor(dividerColor);
        wv_yearMax.setDividerColor(dividerColor);
        wv_hoursMax.setDividerColor(dividerColor);
        wv_minsMax.setDividerColor(dividerColor);
        wv_secondsMax.setDividerColor(dividerColor);

        wv_dayMin.setDividerColor(dividerColor);
        wv_monthMin.setDividerColor(dividerColor);
        wv_yearMin.setDividerColor(dividerColor);
        wv_hoursMin.setDividerColor(dividerColor);
        wv_minsMin.setDividerColor(dividerColor);
        wv_secondMin.setDividerColor(dividerColor);
    }

    private void setDividerType() {

        wv_dayMax.setDividerType(dividerType);
        wv_monthMax.setDividerType(dividerType);
        wv_yearMax.setDividerType(dividerType);
        wv_hoursMax.setDividerType(dividerType);
        wv_minsMax.setDividerType(dividerType);
        wv_secondsMax.setDividerType(dividerType);

        wv_dayMin.setDividerType(dividerType);
        wv_monthMin.setDividerType(dividerType);
        wv_yearMin.setDividerType(dividerType);
        wv_hoursMin.setDividerType(dividerType);
        wv_minsMin.setDividerType(dividerType);
        wv_secondMin.setDividerType(dividerType);

    }

    private void setLineSpacingMultiplier() {
        wv_dayMax.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_monthMax.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_yearMax.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_hoursMax.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_minsMax.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_secondsMax.setLineSpacingMultiplier(lineSpacingMultiplier);

        wv_dayMin.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_monthMin.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_yearMin.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_hoursMin.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_minsMin.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_secondMin.setLineSpacingMultiplier(lineSpacingMultiplier);
    }

    public void setViewMinLabels(String label_year, String label_month, String label_day, String label_hours, String label_mins, String label_seconds) {
        if (label_year != null) {
            wv_yearMax.setLabel(label_year);
        } else {
            wv_yearMax.setLabel(viewMax.getContext().getString(R.string.pickerview_year));
        }
        if (label_month != null) {
            wv_monthMax.setLabel(label_month);
        } else {
            wv_monthMax.setLabel(viewMax.getContext().getString(R.string.pickerview_month));
        }
        if (label_day != null) {
            wv_dayMax.setLabel(label_day);
        } else {
            wv_dayMax.setLabel(viewMax.getContext().getString(R.string.pickerview_day));
        }
        if (label_hours != null) {
            wv_hoursMax.setLabel(label_hours);
        } else {
            wv_hoursMax.setLabel(viewMax.getContext().getString(R.string.pickerview_hours));
        }
        if (label_mins != null) {
            wv_minsMax.setLabel(label_mins);
        } else {
            wv_minsMax.setLabel(viewMax.getContext().getString(R.string.pickerview_minutes));
        }
        if (label_seconds != null) {
            wv_secondsMax.setLabel(label_seconds);
        } else {
            wv_secondsMax.setLabel(viewMax.getContext().getString(R.string.pickerview_seconds));
        }

        if (label_year != null) {
            wv_yearMin.setLabel(label_year);
        } else {
            wv_yearMin.setLabel(viewMin.getContext().getString(R.string.pickerview_year));
        }
        if (label_month != null) {
            wv_monthMin.setLabel(label_month);
        } else {
            wv_monthMin.setLabel(viewMin.getContext().getString(R.string.pickerview_month));
        }
        if (label_day != null) {
            wv_dayMin.setLabel(label_day);
        } else {
            wv_dayMin.setLabel(viewMin.getContext().getString(R.string.pickerview_day));
        }
        if (label_hours != null) {
            wv_hoursMin.setLabel(label_hours);
        } else {
            wv_hoursMin.setLabel(viewMin.getContext().getString(R.string.pickerview_hours));
        }
        if (label_mins != null) {
            wv_minsMin.setLabel(label_mins);
        } else {
            wv_minsMin.setLabel(viewMin.getContext().getString(R.string.pickerview_minutes));
        }
        if (label_seconds != null) {
            wv_secondMin.setLabel(label_seconds);
        } else {
            wv_secondMin.setLabel(viewMin.getContext().getString(R.string.pickerview_seconds));
        }
    }


    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wv_yearMax.setCyclic(cyclic);
        wv_monthMax.setCyclic(cyclic);
        wv_dayMax.setCyclic(cyclic);
        wv_hoursMax.setCyclic(cyclic);
        wv_minsMax.setCyclic(cyclic);
        wv_secondsMax.setCyclic(cyclic);

        wv_yearMin.setCyclic(cyclic);
        wv_monthMin.setCyclic(cyclic);
        wv_dayMin.setCyclic(cyclic);
        wv_hoursMin.setCyclic(cyclic);
        wv_minsMin.setCyclic(cyclic);
        wv_secondMin.setCyclic(cyclic);

    }
    public String getTimeMin() {
        StringBuffer sb = new StringBuffer();
        if (currentYear == startYear) {
           /* int i = wv_month.getCurrentItem() + startMonth;
            System.out.println("i:" + i);*/
            if ((wv_monthMin.getCurrentItem() + startMonth) == startMonth) {
                sb.append((wv_yearMin.getCurrentItem() + startYear)).append("-")
                        .append((wv_monthMin.getCurrentItem() + startMonth)).append("-")
                        .append((wv_dayMin.getCurrentItem() + startDay)).append(" ")
                        .append(wv_hoursMin.getCurrentItem()).append(":")
                        .append(wv_minsMin.getCurrentItem()).append(":")
                        .append(wv_secondMin.getCurrentItem());
            } else {
                sb.append((wv_yearMin.getCurrentItem() + startYear)).append("-")
                        .append((wv_monthMin.getCurrentItem() + startMonth)).append("-")
                        .append((wv_dayMin.getCurrentItem() + 1)).append(" ")
                        .append(wv_hoursMin.getCurrentItem()).append(":")
                        .append(wv_minsMin.getCurrentItem()).append(":")
                        .append(wv_secondMin.getCurrentItem());
            }


        } else {
            sb.append((wv_yearMin.getCurrentItem() + startYear)).append("-")
                    .append((wv_monthMin.getCurrentItem() + 1)).append("-")
                    .append((wv_dayMin.getCurrentItem() + 1)).append(" ")
                    .append(wv_hoursMin.getCurrentItem()).append(":")
                    .append(wv_minsMin.getCurrentItem()).append(":")
                    .append(wv_secondMin.getCurrentItem());
        }

        return sb.toString();
    }

    public String getTime() {
        StringBuffer sb = new StringBuffer();
        if (currentYear == startYear) {
           /* int i = wv_month.getCurrentItem() + startMonth;
            System.out.println("i:" + i);*/
            if ((wv_monthMax.getCurrentItem() + startMonth) == startMonth) {
                sb.append((wv_yearMax.getCurrentItem() + startYear)).append("-")
                        .append((wv_monthMax.getCurrentItem() + startMonth)).append("-")
                        .append((wv_dayMax.getCurrentItem() + startDay)).append(" ")
                        .append(wv_hoursMax.getCurrentItem()).append(":")
                        .append(wv_minsMax.getCurrentItem()).append(":")
                        .append(wv_secondsMax.getCurrentItem());
            } else {
                sb.append((wv_yearMax.getCurrentItem() + startYear)).append("-")
                        .append((wv_monthMax.getCurrentItem() + startMonth)).append("-")
                        .append((wv_dayMax.getCurrentItem() + 1)).append(" ")
                        .append(wv_hoursMax.getCurrentItem()).append(":")
                        .append(wv_minsMax.getCurrentItem()).append(":")
                        .append(wv_secondsMax.getCurrentItem());
            }


        } else {
            sb.append((wv_yearMax.getCurrentItem() + startYear)).append("-")
                    .append((wv_monthMax.getCurrentItem() + 1)).append("-")
                    .append((wv_dayMax.getCurrentItem() + 1)).append(" ")
                    .append(wv_hoursMax.getCurrentItem()).append(":")
                    .append(wv_minsMax.getCurrentItem()).append(":")
                    .append(wv_secondsMax.getCurrentItem());
        }

        return sb.toString();
    }

    public View getViewMax() {
        return viewMax;
    }

    public void setViewMax(View viewMax) {
        this.viewMax = viewMax;
    }

    public View getViewMin() {
        return viewMin;
    }

    public void setViewMin(View viewMin) {
        this.viewMin = viewMin;
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

        wv_dayMax.isCenterLabel(isCenterLabel);
        wv_monthMax.isCenterLabel(isCenterLabel);
        wv_yearMax.isCenterLabel(isCenterLabel);
        wv_hoursMax.isCenterLabel(isCenterLabel);
        wv_minsMax.isCenterLabel(isCenterLabel);
        wv_secondsMax.isCenterLabel(isCenterLabel);

        wv_dayMin.isCenterLabel(isCenterLabel);
        wv_monthMin.isCenterLabel(isCenterLabel);
        wv_yearMin.isCenterLabel(isCenterLabel);
        wv_hoursMin.isCenterLabel(isCenterLabel);
        wv_minsMin.isCenterLabel(isCenterLabel);
        wv_secondMin.isCenterLabel(isCenterLabel);
    }
}
