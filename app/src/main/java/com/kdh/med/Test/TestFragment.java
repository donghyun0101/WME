package com.kdh.med.Test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kdh.med.R;
import com.kdh.med.Tool.TestTool;

import java.util.ArrayList;

/**
 * Created by whdghks913 on 2015-12-10.
 * Modified by KDH on 2018-02-18.
 */

public class TestFragment extends Fragment
{

    public static Fragment getInstance(int grade, int type, int position)
    {
        TestFragment mFragment = new TestFragment();

        Bundle args = new Bundle();
        args.putInt("grade", grade);
        args.putInt("type", type);
        args.putInt("position", position);
        mFragment.setArguments(args);

        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recyclerview, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        final TestAdapter mAdapter = new TestAdapter();
        recyclerView.setAdapter(mAdapter);

        Bundle args = getArguments();
        int grade = args.getInt("grade");
        int type = args.getInt("type");
        int position = args.getInt("position");

        ArrayList<TestTool.examTimeTableData> mValues = TestTool.getExamTimeTable(grade, type, position);
        for (int i = 0; i < mValues.size(); i++)
        {
            TestTool.examTimeTableData mData = mValues.get(i);
            mAdapter.addItem(i + 1, mData.subject);
        }

        return recyclerView;
    }
}

