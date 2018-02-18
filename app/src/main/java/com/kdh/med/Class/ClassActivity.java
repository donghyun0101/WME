package com.kdh.med.Class;

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
import java.util.Calendar;
import java.util.List;

import spreadsheets.GoogleSheetTask;

import com.kdh.med.R;
import com.kdh.med.Tool.Database;
import com.kdh.med.Tool.Preference;
import com.kdh.med.Tool.ClassTool;
import com.kdh.med.Tool.Tools;

/**
 * Created by KDH on 2018-02-18.
 */

public class ClassActivity extends AppCompatActivity
{
    Preference mPref;
    ViewPager viewPager;
    String Majoing = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classtable);

        mPref = new Preference(getApplicationContext());
        int mMajor = mPref.getInt("myMajor", -1);
        int mGrade = mPref.getInt("myGrade", -1);
        int mClass = mPref.getInt("myClass", -1);

        if (mMajor == 1) Majoing = "기계과";
        else Majoing = "전자과";

        Toolbar mToolbar = findViewById(R.id.toolbar);

        if ((mGrade != -1) && (mClass != -1))
        {
            mToolbar.setTitle(Majoing + " " + mGrade + "학년" + mClass + "반");
        }

        else
        {
            mToolbar.setTitle("No Time Table");
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

        if ((mGrade == -1) || (mClass == -1))
        {
            resetMajor();
            return;
        }

        if (!ClassTool.fileExists())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_time_table_db_title);
            builder.setMessage(R.string.no_time_table_db_message);
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

        setCurrentItem();
    }

    private void setCurrentItem()
    {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek > 1 && dayOfWeek < 7)
        {
            viewPager.setCurrentItem(dayOfWeek - 2);
        }
        else
        {
            viewPager.setCurrentItem(0);
        }
    }

    private void resetMajor()
    {
        mPref.remove("myMajor");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.action_setting_mymajor);
        builder.setItems(R.array.myMajor, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                mPref.putInt("myMajor", which + 1);
                resetGrade();
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
                resetClass();
            }
        });
        builder.show();
    }

    private void resetClass()
    {
        mPref.remove("myClass");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.action_setting_myclass);
        builder.setItems(R.array.myClass, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                mPref.putInt("myClass", which + 1);
                Toast.makeText(getApplicationContext(), "다시 로딩됩니다", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ClassActivity.class));
                finish();
            }
        });
        builder.show();
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
        new File(ClassTool.mFilePath + ClassTool.TimeTableDBName).delete();
        DBDownloadTask mTask = new DBDownloadTask();
        mTask.execute(ClassTool.mGoogleSpreadSheetUrl);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        Adapter mAdapter = new Adapter(getSupportFragmentManager());

        for (int dayOfWeek = 0; dayOfWeek < 5; dayOfWeek++)
        {
            mAdapter.addFragment(ClassTool.mDisplayName[dayOfWeek], ClassFragment.getInstance(mPref.getInt("myMajor", -1), mPref.getInt("myGrade", -1), mPref.getInt("myClass", -1), dayOfWeek));
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

    class DBDownloadTask extends GoogleSheetTask
    {
        private ProgressDialog mDialog;
        private Database mDatabase;
        private String[] columnFirstRow;

        @Override
        public void onPreDownload()
        {
            mDialog = new ProgressDialog(ClassActivity.this);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(getString(R.string.loading_table));
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();

            mDatabase = new Database();

            this.startRowNumber = 0;
        }

        @Override
        public void onFinish(long result)
        {
            startActivity(new Intent(ClassActivity.this, ClassActivity.class));
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

                mDatabase.openOrCreateDatabase(ClassTool.mFilePath, ClassTool.TimeTableDBName, ClassTool.tableName, Column.substring(0, Column.length() - 2));
            }
            else
            {
                int length = row.length;
                for (int i = 0; i < length; i++)
                {
                    mDatabase.addData(columnFirstRow[i], row[i]);
                }
                mDatabase.commit(ClassTool.tableName);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_class_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset_mymajor)
        {
            resetMajor();
            return true;

        }
        else if (id == R.id.action_share_timetable)
        {
            shareTimeTable();
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareTimeTable()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.action_share_day);
        builder.setItems(R.array.myWeek, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                shareTimeTable(which, mPref.getInt("myMajor", -1), mPref.getInt("myGrade", -1), mPref.getInt("myClass", -1));
            }
        });
        builder.show();
    }

    private void shareTimeTable(int position, int mMajor, int mGrade, int mClass)
    {
        try
        {
            String mText = "";

            ClassTool.timeTableData mData = ClassTool.getTimeTableData(mMajor, mGrade, mClass, position + 2);

            String[] subject = mData.subject;

            for (int period = 0; period < 7; period++)
            {
                mText += "\n" + (period + 1) + "교시 : " + subject[period];
            }

            String title = getString(R.string.action_share_timetable);
            Intent msg = new Intent(Intent.ACTION_SEND);
            msg.addCategory(Intent.CATEGORY_DEFAULT);
            msg.putExtra(Intent.EXTRA_TITLE, title);
            msg.putExtra(Intent.EXTRA_TEXT, String.format(
                    getString(R.string.action_share_timetable_msg),
                    ClassTool.mDisplayName[position], mText));
            msg.setType("text/plain");
            startActivity(Intent.createChooser(msg, title));

        } catch (Exception ex)
        {
            ex.printStackTrace();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.I_do_not_know_the_error_title);
            builder.setMessage(R.string.I_do_not_know_the_error_message);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }
    }
}