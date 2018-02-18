package com.kdh.med.Test;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import spreadsheets.GoogleSheetTask;

import com.kdh.med.R;
import com.kdh.med.Tool.Database;
import com.kdh.med.Tool.TestTool;
import com.kdh.med.Tool.Preference;
import com.kdh.med.Tool.ClassTool;
import com.kdh.med.Tool.Tools;

/**
 * Created by KDH on 2018-02-18.
 */

public class TestActivity extends AppCompatActivity
{
    ViewPager viewPager;
    Preference mPref;
    int mGrade, mType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testtable);

        mPref = new Preference(getApplicationContext());
        mGrade = mPref.getInt("myGrade", -1);
        mType = mPref.getInt("myType", -1);
        boolean fileExists = TestTool.fileExists();

        Toolbar mToolbar = findViewById(R.id.toolbar);
        if ((mGrade != -1) && (mType != -1))
        {
            if (fileExists)
            {
                TestTool.examData mData = TestTool.getExamInfoData();
                mToolbar.setTitle(String.format(getString(R.string.exam_time_title), (mType == 0 ? "기계과" : "전자과"), mGrade, mData.date, "\n" + mData.type));
            }

            else
            {
                mToolbar.setTitle("No Exam Table");
            }
        }

        else
        {
            mToolbar.setTitle("No Exam Table");
        }

        setSupportActionBar(mToolbar);

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null)
        {
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onBackPressed();
                }
            });
        }

        if (mType == -1)
        {
            resetType();
            return;
        }

        if (mGrade == -1)
        {
            resetGrade();
            return;
        }

        if (!fileExists)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_exam_db_title);
            builder.setMessage(R.string.no_exam_db_message);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    downloadingDB();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    finish();
                }
            });
            builder.show();

            return;
        }

        viewPager = findViewById(R.id.mViewpager);
        if (viewPager != null)
        {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = findViewById(R.id.mTabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);

        FloatingActionButton mFab = findViewById(R.id.mFab);
        mFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                downloadingDB();
            }
        });
    }

    public void downloadingDB()
    {
        if (Tools.isOnline(getApplicationContext()))
        {
            if (Tools.isWifi(getApplicationContext()))
            {
                downloadStart();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.no_wifi_title);
                builder.setMessage(R.string.no_wifi_msg);
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        downloadStart();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_network_title);
            builder.setMessage(R.string.no_network_msg);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }
    }

    private void downloadStart()
    {
        new File(ClassTool.mFilePath + TestTool.ExamDBName).delete();
        ExamDBDownloadTask mTask = new ExamDBDownloadTask();
        mTask.execute(TestTool.mGoogleSpreadSheetUrl);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        Adapter mAdapter = new Adapter(getSupportFragmentManager());

        TestTool.examData mData = TestTool.getExamInfoData();
        int days = Integer.parseInt(mData.days);

        for (int day = 1; day <= days; day++)
        {
            mAdapter.addFragment(day + "일째", TestFragment.getInstance(mGrade, mType, day));
        }

        viewPager.setAdapter(mAdapter);
    }

    class Adapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager manager)
        {
            super(manager);
        }

        public void addFragment(String mTitle, Fragment mFragment)
        {
            mFragments.add(mFragment);
            mFragmentTitles.add(mTitle);
        }

        @Override
        public Fragment getItem(int position)
        {
            return mFragments.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mFragmentTitles.get(position);
        }
    }

    private void resetType()
    {
        mPref.remove("myType");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.action_setting_mymajor);
        builder.setItems(R.array.myMajor, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                mPref.putInt("myType", which);
                if (mGrade == -1)
                {
                    resetGrade();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "다시 로딩됩니다", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), TestActivity.class));
                    finish();
                }
            }
        });
        builder.show();
    }

    private void resetGrade()
    {
        mPref.remove("myGrade");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.action_setting_mygrade);
        builder.setItems(R.array.myGrade, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                mPref.putInt("myGrade", which + 1);
                Toast.makeText(getApplicationContext(), "다시 로딩됩니다", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), TestActivity.class));
                finish();
            }
        });
        builder.show();
    }

    class ExamDBDownloadTask extends GoogleSheetTask
    {
        private ProgressDialog mDialog;
        private Database mDatabase;
        private String[] columnFirstRow;

        @Override
        public void onPreDownload()
        {
            mDialog = new ProgressDialog(TestActivity.this);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(getString(R.string.loading_test));
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();

            mDatabase = new Database();

            this.startRowNumber = 0;
        }

        @Override
        public void onFinish(long result)
        {
            startActivity(new Intent(TestActivity.this, TestActivity.class));
            finish();

            if (mDialog != null)
            {
                mDialog.dismiss();
                mDialog = null;
            }

            if (mDatabase != null)
                mDatabase.release();
        }

        @Override
        public void onRow(int startRowNumber, int position, String[] row)
        {
            if (startRowNumber == position)
            {
                columnFirstRow = row;

                StringBuilder Column = new StringBuilder();

                for (String column : row)
                {
                    Column.append(column);
                    Column.append(" text, ");
                }

                mDatabase.openOrCreateDatabase(ClassTool.mFilePath, TestTool.ExamDBName, TestTool.ExamTableName, Column.substring(0, Column.length() - 2));
            }
            else
            {
                int length = row.length;
                for (int i = 0; i < length; i++)
                {
                    mDatabase.addData(columnFirstRow[i], row[i]);
                }
                mDatabase.commit(TestTool.ExamTableName);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_reset_mygrade)
        {
            mPref.remove("myGrade");
            mType = -1;
            resetType();
        }

        return super.onOptionsItemSelected(item);
    }

}
