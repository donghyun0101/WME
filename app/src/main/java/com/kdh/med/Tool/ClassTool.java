package com.kdh.med.Tool;

import android.content.Context;
import android.database.Cursor;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import com.kdh.med.R;

/**
 * Created by whdghks913 on 2015-02-17.
 * Modified by KDH on 2018-02-18.
 */

public class ClassTool
{
    public static final String TimeTableDBName = "WMECLASS.db";
    public static final String tableName = "Wclass";

    public final static String mFilePath = "/data/data/com.kdh.med/databases/";
    public final static String mGoogleSpreadSheetUrl = "https://docs.google.com/spreadsheets/d/1Nf1JxMtarsM3eMnV8DdtgUyxpbaWHHssJrJ9tuDjlRo/pubhtml?gid=0&single=true";

    public final static String[] mDisplayName = {"월요일", "화요일", "수요일", "목요일", "금요일"};

    public static boolean fileExists()
    {
        return new File(ClassTool.mFilePath + ClassTool.TimeTableDBName).exists();
    }

    public static timeTableData getTimeTableData(int mMajor, int mGrade, int mClass, int DayOfWeek)
    {
        if (mMajor == -1 || mGrade == -1 || mClass == -1)
        {
            return null;
        }

        timeTableData mData = new timeTableData();
        String[] subject = new String[7];

        Database mDatabase = new Database();
        mDatabase.openDatabase(ClassTool.mFilePath, ClassTool.TimeTableDBName);

        Cursor mCursor;
        mCursor = mDatabase.getData(ClassTool.tableName, "M" + mMajor + mGrade + mClass);

        /**
         * Move to Row
         * ---- moveToFirst
         * ---- moveToNext
         * ---- moveToPosition
         * ---- moveToLast
         *
         * Mon : DayOfWeek : 0
         * Tus : DayOfWeek : 1
         * ...
         * Fri : DayOfWeek : 4
         */
        mCursor.moveToPosition((DayOfWeek * 7) + 1);

        for (int period = 0; period < 7; period++)
        {
            String mSubject;

            /**
             * | | | |
             * 0 1 2 3
             */

            mSubject = mCursor.getString(0);

            if (mSubject != null && !mSubject.isEmpty()
                    && mSubject.contains("\n"))
                mSubject = mSubject.replace("\n", " (") + ")";

            subject[period] = mSubject;

            mCursor.moveToNext();
        }

        mData.subject = subject;

        return mData;
    }

    public static class timeTableData
    {
        public String[] subject;
    }

    public static todayTimeTableData getTodayTimeTable(Context mContext)
    {
        Preference mPref = new Preference(mContext);
        todayTimeTableData mData = new todayTimeTableData();

        Calendar mCalendar = Calendar.getInstance();

        int DayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

        int mMajor = mPref.getInt("myMajor", -1);
        int mGrade = mPref.getInt("myGrade", -1);
        int mClass = mPref.getInt("myClass", -1);

        if (DayOfWeek > 0 && DayOfWeek < 7)
        {
            DayOfWeek -= 2;
        }
        else
        {
            mData.title = mContext.getString(R.string.not_go_to_school_title);
            mData.info = mContext.getString(R.string.not_go_to_school_message);
            return mData;
        }

        mData.title = String.format(mContext.getString(R.string.today_timetable), mCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.KOREAN));

        if (mMajor == -1 || mGrade == -1 || mClass == -1)
        {
            mData.info = mContext.getString(R.string.no_setting_my_grade);
            return mData;
        }

        boolean mFileExists = new File(ClassTool.mFilePath + ClassTool.TimeTableDBName).exists();
        if (!mFileExists)
        {
            mData.info = mContext.getString(R.string.not_exists_data);
            return mData;
        }

        String mTimeTable = "";

        Database mDatabase = new Database();
        mDatabase.openDatabase(ClassTool.mFilePath, ClassTool.TimeTableDBName);


        Cursor mCursor;
        mCursor = mDatabase.getData(ClassTool.tableName, "M" + mMajor + mGrade + mClass);

        /**
         * Move to Row
         * ---- moveToFirst
         * ---- moveToNext
         * ---- moveToPosition
         * ---- moveToLast
         *
         * Mon : DayOfWeek : 0
         * Tus : DayOfWeek : 1
         * ...
         * Fri : DayOfWeek : 4
         */
        mCursor.moveToPosition((DayOfWeek * 7) + 1);

        for (int period = 0; period < 7; period++)
        {
            String mSubject;

            /**
             * | | | |
             * 0 1 2 3
             */

            mSubject = mCursor.getString(0);

            if (mSubject != null && !mSubject.isEmpty()
                    && mSubject.contains("\n"))
                mSubject = mSubject.replace("\n", " (") + ")";

            mTimeTable += Integer.toString(period + 1) + ". " + mSubject;

            if (mCursor.moveToNext())
            {
                mTimeTable += "\n";
            }
        }

        mData.info = mTimeTable;

        return mData;
    }

    public static class todayTimeTableData
    {
        public String title;
        public String info;
    }
}
