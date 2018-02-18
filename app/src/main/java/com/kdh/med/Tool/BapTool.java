package com.kdh.med.Tool;

import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.kdh.med.R;

import toast.library.meal.MealLibrary;

/**
 * Created by KDH on 2018-02-18.
 */

public class BapTool
{


    public static final String BAP_PREFERENCE_NAME = "WMEbapData";
    public static final int TYPE_BFAST = 1;
    public static final int TYPE_LUNCH = 2;
    public static final int TYPE_DINNER = 3;
    public static final int TYPE_BFAST_KCAL = 4;
    public static final int TYPE_LUNCH_KCAL = 5;
    public static final int TYPE_DINNER_KCAL = 6;

    public static final String ACTION_UPDATE = "ACTION_WMEBAP_UPDATE";

    public static String getBapStringFormat(int year, int month, int day, int type)
    {
        /**
         * Format : year-month-day-TYPE
         */
        // Calendar의 month는 1이 부족하므로 1을 더해줌
        month += 1;
        return year + "-" + month + "-" + day + "-" + type;
    }

    /**
     * Pref Name Format : 2015-02-17-TYPE_index
     * ex) 2015-02-17-1_3
     */

    public static void saveBapData(Context mContext, String[] Calender, String[] Bfast, String[] Lunch, String[] Dinner, String[] Bfast_kcal, String[] Lunch_kcal, String[] Dinner_kcal)
    {
        Preference mPref = new Preference(mContext, BAP_PREFERENCE_NAME);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd(E)", Locale.KOREA);

        for (int index = 0; index < Calender.length; index++)
        {
            try
            {
                Calendar mDate = Calendar.getInstance();
                mDate.setTime(mFormat.parse(Calender[index]));

                int year = mDate.get(Calendar.YEAR);
                int month = mDate.get(Calendar.MONTH);
                int day = mDate.get(Calendar.DAY_OF_MONTH);

                String mPrefBfastName = getBapStringFormat(year, month, day, TYPE_BFAST);
                String mPrefLunchName = getBapStringFormat(year, month, day, TYPE_LUNCH);
                String mPrefDinnerName = getBapStringFormat(year, month, day, TYPE_DINNER);
                String mPrefBfastKcalName = getBapStringFormat(year, month, day, TYPE_BFAST_KCAL);
                String mPrefLunchKcalName = getBapStringFormat(year, month, day, TYPE_LUNCH_KCAL);
                String mPrefDinnerKcalName = getBapStringFormat(year, month, day, TYPE_DINNER_KCAL);

                String mBfast = Bfast[index];
                String mLunch = Lunch[index];
                String mDinner = Dinner[index];
                String mBfast_kcal = Bfast_kcal[index];
                String mLunch_kcal = Lunch_kcal[index];
                String mDinner_kcal = Dinner_kcal[index];

                if (!MealLibrary.isMealCheck(mBfast)) mBfast = "";
                if (!MealLibrary.isMealCheck(mLunch)) mLunch = "";
                if (!MealLibrary.isMealCheck(mDinner)) mDinner = "";
                if (!MealLibrary.isMealCheck(mBfast_kcal)) mBfast_kcal = "";
                if (!MealLibrary.isMealCheck(mLunch_kcal)) mLunch_kcal = "";
                if (!MealLibrary.isMealCheck(mDinner_kcal)) mDinner_kcal = "";

                mPref.putString(mPrefBfastName, mBfast);
                mPref.putString(mPrefLunchName, mLunch);
                mPref.putString(mPrefDinnerName, mDinner);
                mPref.putString(mPrefBfastKcalName, mBfast_kcal);
                mPref.putString(mPrefLunchKcalName, mLunch_kcal);
                mPref.putString(mPrefDinnerKcalName, mDinner_kcal);

            } catch (ParseException e)
            {
                e.getErrorOffset();
            }
        }
    }

    /**
     * Format : 2015-2-11-2
     */
    public static restoreBapDateClass restoreBapData(Context mContext, int year, int month, int day)
    {
        Preference mPref = new Preference(mContext, BAP_PREFERENCE_NAME);
        SimpleDateFormat mCalenderFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA);
        SimpleDateFormat mDayOfWeekFormat = new SimpleDateFormat("E요일", Locale.KOREA);
        Calendar mDate = Calendar.getInstance();
        mDate.set(year, month, day);

        restoreBapDateClass mData = new restoreBapDateClass();

        String mPrefBfastName = getBapStringFormat(year, month, day, TYPE_BFAST);
        String mPrefLunchName = getBapStringFormat(year, month, day, TYPE_LUNCH);
        String mPrefDinnerName = getBapStringFormat(year, month, day, TYPE_DINNER);
        String mPrefBfastKcalName = getBapStringFormat(year, month, day, TYPE_BFAST_KCAL);
        String mPrefLunchKcalName = getBapStringFormat(year, month, day, TYPE_LUNCH_KCAL);
        String mPrefDinnerKcalName = getBapStringFormat(year, month, day, TYPE_DINNER_KCAL);

        mData.Calender = mCalenderFormat.format(mDate.getTime());
        mData.DayOfTheWeek = mDayOfWeekFormat.format(mDate.getTime());
        mData.Bfast = mPref.getString(mPrefBfastName, null);
        mData.Lunch = mPref.getString(mPrefLunchName, null);
        mData.Dinner = mPref.getString(mPrefDinnerName, null);
        mData.Bfast_kcal = mPref.getString(mPrefBfastKcalName, null);
        mData.Lunch_kcal = mPref.getString(mPrefLunchKcalName, null);
        mData.Dinner_kcal = mPref.getString(mPrefDinnerKcalName, null);

        if (mData.Bfast == null && mData.Lunch == null && mData.Dinner == null && mData.Bfast_kcal == null && mData.Lunch_kcal == null && mData.Dinner_kcal == null)
        {
            mData.isBlankDay = true;
        }

        return mData;
    }

    public static void removeBapData(Context mContext)
    {
        Preference mPref = new Preference(mContext, BAP_PREFERENCE_NAME);
        mPref.clear();
    }

    public static class restoreBapDateClass
    {
        public String Calender;
        public String DayOfTheWeek;
        public String Bfast;
        public String Lunch;
        public String Dinner;
        public String Bfast_kcal;
        public String Lunch_kcal;
        public String Dinner_kcal;
        public boolean isBlankDay = false;
    }

    public static boolean mStringCheck(String mString)
    {
        if (mString == null || "".equals(mString) || " ".equals(mString))
            return true;
        return false;
    }

    public static todayBapData getTodayBap(Context mContext)
    {
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        restoreBapDateClass mData = BapTool.restoreBapData(mContext, year, month, day);
        todayBapData mReturnData = new todayBapData();

        if (!mData.isBlankDay)
        {
            int hour = mCalendar.get(Calendar.HOUR_OF_DAY);

            /**
             * hour : 0~23
             *
             * 0~13 : Lunch
             * 14~23 : Dinner
             */

            if (hour <= 9)
            {
                mReturnData.title = mContext.getString(R.string.today_bfast);
                mReturnData.info = (!MealLibrary.isMealCheck(mData.Bfast) ? mContext.getString(R.string.no_data_bfast) : mData.Bfast);
            }
            else if (hour <= 13 && hour > 9)
            {
                mReturnData.title = mContext.getString(R.string.today_lunch);
                mReturnData.info = (!MealLibrary.isMealCheck(mData.Lunch) ? mContext.getString(R.string.no_data_lunch) : mData.Lunch);
            }
            else
            {
                mReturnData.title = mContext.getString(R.string.today_dinner);
                mReturnData.info = (!MealLibrary.isMealCheck(mData.Dinner) ? mContext.getString(R.string.no_data_dinner) : mData.Dinner);
            }
        }
        else
        {
            mReturnData.title = mContext.getString(R.string.no_data_title);
            mReturnData.info = mContext.getString(R.string.no_data_message);
        }

        return mReturnData;
    }

    public static class todayBapData
    {
        public String title;
        public String info;
    }

    public static String replaceString(String mString)
    {
        Log.d("string", mString);
        String[] mTrash = {"."};

        for (String e : mTrash)
        {
            mString = mString.replace(e, " ");
        }

        //mString = mString.replace("\n", "  ");

        return mString;
    }
}