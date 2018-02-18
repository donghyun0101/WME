package com.kdh.med;

import com.kdh.med.Class.ClassActivity;
import com.kdh.med.Setting.SettingActivity;
import com.kdh.med.Test.TestActivity;
import com.kdh.med.bap.BapActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kdh.med.Tool.Preference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by KDH on 2018-02-18.
 */

public class MainActivity extends AppCompatActivity
{
    private Animations anims;
    private RelativeLayout a, b, c, d, e, fv;
    private View a1, b1, d1, e1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(null);

        init();

        TranslateAnimation lr = anims.LeftToRight();
        TranslateAnimation tb = anims.TopToBottom();
        TranslateAnimation bt = anims.BottomToTop();
        TranslateAnimation rl = anims.RightToLeft();

        a.startAnimation(lr);
        b.startAnimation(tb);
        c.startAnimation(lr);
        d.startAnimation(bt);
        e.startAnimation(rl);
        fv.startAnimation(tb);

        a1.startAnimation(lr);
        b1.startAnimation(tb);
        d1.startAnimation(bt);
        e1.startAnimation(rl);

        showUpdate();
    }

    private void showUpdate()
    {
        try
        {
            Preference mPref = new Preference(getApplicationContext());
            PackageManager packageManager = getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);

            int versionCode = info.versionCode;

            if (mPref.getInt("Code", 0) != versionCode)
            {
                mPref.putInt("Code", versionCode);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.update_title);
                builder.setMessage(R.string.update);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
            }

        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        PackageInfo versionname = null;

        try
        {
            versionname = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        String versionN = versionname.versionName;

        if (id == R.id.action_developer)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Developer")
                    .setMessage("원주의료고등학교 재학생 김동현\n(C)2015~2018 김동현 All rights reserved.")
                    .setPositiveButton("확인", null)
                    .show();
            return true;
        }

        else if (id == R.id.action_opensource)
        {
            new AlertDialog.Builder(this)
                    .setView(R.layout.dialog_opensource)
                    .show();
            return true;
        }

        else if (id == R.id.action_changelog)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Version " + versionN)
                    .setMessage(R.string.update)
                    .setPositiveButton("OK", null)
                    .show();
            return true;
        }

        else if (id == R.id.action_version)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Application Version")
                    .setMessage(versionN)
                    .setPositiveButton("OK", null)
                    .show();
            return true;
        }

        else if (id == R.id.action_using)
        {
            new AlertDialog.Builder(this)
                    .setTitle("의료고앱 간단 사용법!")
                    .setMessage(R.string.update_message)
                    .setPositiveButton("OK", null)
                    .show();
            return true;
        }

        else if (id == R.id.action_sendbug)
        {
            Uri uri = Uri.parse("http://goto.kakao.com/@wmeappbug");
            Intent i1 = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i1);
            Toast.makeText(MainActivity.this, "플러스 친구를 추가한 후 오류를 제보해 주세요!", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void wmesite(View v)
    {
        Uri uri = Uri.parse("http://www.wonjumedi.hs.kr");
        Intent i1 = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i1);
    }

    public void wmeoutsite(View v)
    {
        Uri uri2 = Uri.parse("http://wonjumedi.meistergo.co.kr/m");
        Intent i2 = new Intent(Intent.ACTION_VIEW, uri2);
        startActivity(i2);
    }

    public void calenderClass(View v)
    {
        String schdYear, schdMonth;

        SimpleDateFormat mFormatYear = new SimpleDateFormat("yyyy", Locale.KOREA);
        SimpleDateFormat mFormatMonth = new SimpleDateFormat("MM", Locale.KOREA);

        Calendar mCalendar = Calendar.getInstance();
        long nowDate = mCalendar.getTimeInMillis();

        schdYear = mFormatYear.format(nowDate);
        schdMonth = mFormatMonth.format(nowDate);

        Uri uri3 = Uri.parse("http://180.81.125.27/schedule/list.do?&m=0209&s=wjmedi&schdYear=" + schdYear + "&schdMonth=" + schdMonth);
        Intent i3 = new Intent(Intent.ACTION_VIEW, uri3);
        startActivity(i3);

        //startActivity(new Intent(this, CalendarActivity.class));
        //이후 업데이트에 적용
    }

    public void classTableClass(View v)
    {
        startActivity(new Intent(this, ClassActivity.class));
    }

    public void bapClass(View v)
    {
        startActivity(new Intent(this, BapActivity.class));
    }

    public void testClass(View v)
    {
        startActivity(new Intent(this, TestActivity.class));
    }

    public void settingClass(View v)
    {
        startActivity(new Intent(this, SettingActivity.class));
    }

    private void init()
    {
        // TODO: Implement this method
        anims = new Animations();

        TranslateAnimation lrs = anims.LeftToRightSet();
        TranslateAnimation tbs = anims.TopToBottomSet();
        TranslateAnimation bts = anims.BottomToTopSet();
        TranslateAnimation rls = anims.RightToLeftSet();

        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        c = findViewById(R.id.c);
        d = findViewById(R.id.d);
        e = findViewById(R.id.e);
        fv = findViewById(R.id.fv);

        a1 = findViewById(R.id.a1);
        b1 = findViewById(R.id.b1);
        d1 = findViewById(R.id.d1);
        e1 = findViewById(R.id.e1);

        a.startAnimation(lrs);
        b.startAnimation(tbs);
        c.startAnimation(lrs);
        d.startAnimation(bts);
        e.startAnimation(rls);
        fv.startAnimation(tbs);

        a1.startAnimation(lrs);
        b1.startAnimation(tbs);
        d1.startAnimation(bts);
        e1.startAnimation(rls);
    }
}
