package com.kdh.med.Updates;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.Calendar;

import com.kdh.med.Tool.Preference;
import com.kdh.med.Tool.ProcessTask;
import com.kdh.med.Tool.Tools;

/**
 * Created by KDH on 2018-02-18.
 */

public class UpdateService extends Service
{
    Calendar mCalendar;
    Preference mPref;
    BapDownloadTask mProcessTask;

    boolean onlyWIFI;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mCalendar = Calendar.getInstance();
        mPref = new Preference(getApplicationContext());
        onlyWIFI = mPref.getBoolean("updateWiFi", true);

        if (Tools.isOnline(getApplicationContext()))
        {
            // 네트워크 연결됨
            if (onlyWIFI && !Tools.isWifi(getApplicationContext()))
            {
                // 와이파이에서만 업데이트 && 와이파이 연결안됨
                stopSelf();
            }

            // 토요일일경우 하루를 추가해줌
            if (mCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                mCalendar.add(Calendar.DATE, 1);

            mProcessTask = new BapDownloadTask(this);
            mProcessTask.execute(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

        }
        else
        {
            // 네트워크 연결 안됨
            stopSelf();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    public class BapDownloadTask extends ProcessTask
    {
        public BapDownloadTask(Context mContext)
        {
            super(mContext);
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

        }
    }
}