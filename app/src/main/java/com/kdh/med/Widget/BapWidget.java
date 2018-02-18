package com.kdh.med.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Calendar;

import com.kdh.med.R;
import com.kdh.med.bap.BapActivity;
import com.kdh.med.Tool.BapTool;
import com.kdh.med.Tool.Preference;
import com.kdh.med.Tool.ProcessTask;
import com.kdh.med.Tool.Tools;

/**
 * Created by KDH on 2018-02-18.
 */

public class BapWidget extends AppWidgetProvider
{

    static void updateSmallAppWidget(Context mContext, AppWidgetManager appWidgetManager, int appWidgetId, boolean ifNotUpdate)
    {
        RemoteViews mViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_bap);

        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        BapTool.restoreBapDateClass mData = BapTool.restoreBapData(mContext, year, month, day);

        mViews.setTextViewText(R.id.mCalender, mData.Calender);

        if (mData.isBlankDay)
        {
            // 데이터 없음
            if (Tools.isOnline(mContext))
            {
                // Only Wifi && Not Wifi
                if (new Preference(mContext).getBoolean("updateWiFi", true) && !Tools.isWifi(mContext))
                {
                    mViews.setTextViewText(R.id.mLunch, mContext.getString(R.string.widget_no_data));
                }
                else if (ifNotUpdate)
                {
                    // 급식 데이터 받아옴
                    BapDownloadTask mProcessTask = new BapDownloadTask(mContext);
                    mProcessTask.execute(year, month, day);
                }
            }
            else
            {
                mViews.setTextViewText(R.id.mLunch, mContext.getString(R.string.widget_no_data));
            }
        }
        else
        {
            // 데이터 있음

            /**
             * hour : 0~23
             *
             * 0~13 : Lunch
             * 14~23 : Dinner
             */
            String mTitle, mTodayMeal;
            int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
            if (hour <= 9)
            {
                mTitle = mContext.getString(R.string.today_bfast);
                mTodayMeal = mData.Bfast;
                if (BapTool.mStringCheck(mTodayMeal))
                {
                    mTodayMeal = mContext.getString(R.string.no_data_bfast);
                }
                else
                {
                    mTodayMeal = BapTool.replaceString(mTodayMeal);
                }
            }

            else if (hour <= 13 && hour > 9)
            {
                mTitle = mContext.getString(R.string.today_lunch);
                mTodayMeal = mData.Lunch;
                if (BapTool.mStringCheck(mTodayMeal))
                {
                    mTodayMeal = mContext.getString(R.string.no_data_lunch);
                }
                else
                {
                    Toast.makeText(mContext, mTodayMeal, Toast.LENGTH_SHORT).show();
                    mTodayMeal = BapTool.replaceString(mTodayMeal);
                    Toast.makeText(mContext, mTodayMeal, Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                mTitle = mContext.getString(R.string.today_dinner);
                mTodayMeal = mData.Dinner;
                if (BapTool.mStringCheck(mTodayMeal))
                {
                    mTodayMeal = mContext.getString(R.string.no_data_dinner);
                }
                else
                {
                    mTodayMeal = BapTool.replaceString(mTodayMeal);
                }
            }

            mViews.setTextViewText(R.id.mLunchTitle, mTitle);
            mViews.setTextViewText(R.id.mLunch, mTodayMeal);
        }

        PendingIntent layoutPendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, BapActivity.class), 0);
        mViews.setOnClickPendingIntent(R.id.mWidgetLayout, layoutPendingIntent);

        Intent mIntent = new Intent(mContext, WidgetBroadCast.class);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(mContext, 0, mIntent, 0);
        mViews.setOnClickPendingIntent(R.id.mUpdateLayout, updatePendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, mViews);
    }

    @Override
    public void onUpdate(Context mContext, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds)
        {
            int minResizeWidth = appWidgetManager.getAppWidgetInfo(appWidgetId).minResizeWidth;
            int minResizeHeight = appWidgetManager.getAppWidgetInfo(appWidgetId).minResizeHeight;

            updateSmallAppWidget(mContext, appWidgetManager, appWidgetId, false);

        }
    }

    @Override
    public void onReceive(Context mContext, Intent mIntent)
    {
        super.onReceive(mContext, mIntent);

        String mAction = mIntent.getAction();
        if (mAction.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE))
        {
            updateAllBapWidget(mContext);
        }
    }

    @Override
    public void onEnabled(Context context)
    {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context)
    {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static class BapDownloadTask extends ProcessTask
    {
        Context mContext;

        public BapDownloadTask(Context mContext)
        {
            super(mContext);
            this.mContext = mContext;
        }

        @Override
        public void onPreDownload()
        {
        }

        @Override
        public void onUpdate(int progress)
        {
        }

        @Override
        public void onFinish(long result)
        {
            Intent mIntent = new Intent(mContext, WidgetBroadCast.class);
            mContext.sendBroadcast(mIntent);
        }
    }

    public static void updateAllBapWidget(Context mContext)
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, BapWidget.class));

        for (int appWidgetId : appWidgetIds)
        {

            Bundle mBundle = appWidgetManager.getAppWidgetOptions(appWidgetId);
            int maxWidth = mBundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
            int maxHeight = mBundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

            updateSmallAppWidget(mContext, appWidgetManager, appWidgetId, false);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context mContext, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions)
    {
        super.onAppWidgetOptionsChanged(mContext, appWidgetManager, appWidgetId, newOptions);

//        int minWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
//        int minHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        int maxWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
        int maxHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

        updateSmallAppWidget(mContext, appWidgetManager, appWidgetId, false);
    }
}

