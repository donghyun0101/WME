package com.kdh.med.Calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kdh.med.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity
{
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("2017년 일정");

        viewPager = findViewById(R.id.mViewpager);
        if (viewPager != null)
        {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = findViewById(R.id.mTabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);

        setCurrentItem();
    }

    private void setupViewPager(ViewPager viewPager)
    {
        Adapter mAdapter = new Adapter(getSupportFragmentManager());

        mAdapter.addFragment("3월", CalendarFragment.getInstance(3));
        mAdapter.addFragment("4월", CalendarFragment.getInstance(4));
        mAdapter.addFragment("5월", CalendarFragment.getInstance(5));
        mAdapter.addFragment("6월", CalendarFragment.getInstance(6));
        mAdapter.addFragment("7월", CalendarFragment.getInstance(7));
        mAdapter.addFragment("8월", CalendarFragment.getInstance(8));
        mAdapter.addFragment("9월", CalendarFragment.getInstance(9));
        mAdapter.addFragment("10월", CalendarFragment.getInstance(10));
        mAdapter.addFragment("11월", CalendarFragment.getInstance(11));
        mAdapter.addFragment("12월", CalendarFragment.getInstance(12));
        mAdapter.addFragment("2019년 1월", CalendarFragment.getInstance(1));
        mAdapter.addFragment("2019년 2월", CalendarFragment.getInstance(2));

        viewPager.setAdapter(mAdapter);
    }

    private void setCurrentItem()
    {
        int month = Calendar.getInstance().get(Calendar.MONTH);

        if (month >= 2) month -= 2;
        else month += 10;

        viewPager.setCurrentItem(month);
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

}
