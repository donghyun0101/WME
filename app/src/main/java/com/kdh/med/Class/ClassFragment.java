package com.kdh.med.Class;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kdh.med.R;
import com.kdh.med.Tool.ClassTool;

/**
 * Created by KDH on 2018-02-18.
 */

public class ClassFragment extends Fragment
{

    public static Fragment getInstance(int mMajor, int mGrade, int mClass, int dayOfWeek)
    {
        ClassFragment mFragment = new ClassFragment();

        Bundle args = new Bundle();
        args.putInt("mMajor", mMajor);
        args.putInt("mGrade", mGrade);
        args.putInt("mClass", mClass);
        args.putInt("dayOfWeek", dayOfWeek);
        mFragment.setArguments(args);

        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recyclerview, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        final ClassAdapter mAdapter = new ClassAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        Bundle args = getArguments();
        int mMajor = args.getInt("mMajor");
        int mGrade = args.getInt("mGrade");
        int mClass = args.getInt("mClass");
        int dayOfWeek = args.getInt("dayOfWeek");

        ClassTool.timeTableData mData = ClassTool.getTimeTableData(mMajor, mGrade, mClass, dayOfWeek);

        for (int position = 0; position < 7; position++)
        {
            mAdapter.addItem(position + 1, mData.subject[position]);
        }

        return recyclerView;
    }
}
