package com.kdh.med.Tool;

import android.content.Context;
import android.os.AsyncTask;

import toast.library.meal.MealLibrary;

/**
 * Created by KDH on 2018-02-18.
 */

public abstract class ProcessTask extends AsyncTask<Integer, Integer, Long>
{
    final Context mContext;
    String[] Calender, Bfast, Lunch, Dinner, Bfast_kcal, Lunch_kcal, Dinner_kcal;

    public abstract void onPreDownload();

    public abstract void onUpdate(int progress);

    public abstract void onFinish(long result);

    public ProcessTask(Context mContext)
    {
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        onPreDownload();
    }

    /**
     * params[0] : year
     * params[1] : month
     * params[2] : day
     */
    @Override
    protected Long doInBackground(Integer... params)
    {
        publishProgress(5);

        final String CountryCode = "gwe.go.kr"; // 접속 할 교육청 도메인
        final String schulCode = "K100000474"; // 학교 고유 코드
        final String schulCrseScCode = "4"; // 학교 종류 코드 1
        final String schulKndScCode = "04"; // 학교 종류 코드 2

        final String year = Integer.toString(params[0]);
        String month = Integer.toString(params[1] + 1);
        String day = Integer.toString(params[2]);

        if (month.length() <= 1)
            month = "0" + month;
        if (day.length() <= 1)
            day = "0" + day;

        publishProgress(12);

        try
        {
            Calender = MealLibrary.getDateNew(CountryCode, schulCode, schulCrseScCode, schulKndScCode, "1", year, month, day);

            publishProgress(25);

            Bfast = MealLibrary.getMealNew(CountryCode, schulCode, schulCrseScCode, schulKndScCode, "1", year, month, day);

            publishProgress(39);

            Lunch = MealLibrary.getMealNew(CountryCode, schulCode, schulCrseScCode, schulKndScCode, "2", year, month, day);

            publishProgress(51);

            Dinner = MealLibrary.getMealNew(CountryCode, schulCode, schulCrseScCode, schulKndScCode, "3", year, month, day);

            publishProgress(62);

            Bfast_kcal = MealLibrary.getKcalNew(CountryCode, schulCode, schulCrseScCode, schulKndScCode, "1", year, month, day);

            publishProgress(68);

            Lunch_kcal = MealLibrary.getKcalNew(CountryCode, schulCode, schulCrseScCode, schulKndScCode, "2", year, month, day);

            publishProgress(79);

            Dinner_kcal = MealLibrary.getKcalNew(CountryCode, schulCode, schulCrseScCode, schulKndScCode, "3", year, month, day);

            publishProgress(95);

            BapTool.saveBapData(mContext, Calender, Bfast, Lunch, Dinner, Bfast_kcal, Lunch_kcal, Dinner_kcal);

            publishProgress(100);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0l;
    }

    @Override
    protected void onProgressUpdate(Integer... params)
    {
        onUpdate(params[0]);
    }

    @Override
    protected void onPostExecute(Long result)
    {
        super.onPostExecute(result);

        onFinish(result);
    }
}