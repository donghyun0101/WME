package com.kdh.med.Test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.kdh.med.R;

/**
 * Created by whdghks913 on 2015-12-10.
 * Modified by KDH on 2018-02-18.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ExamTimeViewHolder>
{
    private ArrayList<ExamTimeInfo> mValues = new ArrayList<>();

    public void addItem(int time, String subject)
    {
        ExamTimeInfo addInfo = new ExamTimeInfo();

        addInfo.time = time;
        addInfo.subject = subject;

        mValues.add(addInfo);
    }

    @Override
    public TestAdapter.ExamTimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_test_item, parent, false);

        return new ExamTimeViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ExamTimeViewHolder holder, int position)
    {
        ExamTimeInfo mInfo = getItemData(position);

        holder.mTime.setText(String.valueOf(mInfo.time));
        holder.mSubject.setText(mInfo.subject);
    }

    @Override
    public int getItemCount()
    {
        return mValues.size();
    }

    public ExamTimeInfo getItemData(int position)
    {
        return mValues.get(position);
    }

    public class ExamTimeViewHolder extends RecyclerView.ViewHolder
    {
        public final Context mContext;
        public final TextView mTime, mSubject;

        public ExamTimeViewHolder(View mView)
        {
            super(mView);

            mContext = mView.getContext();
            mTime = mView.findViewById(R.id.list_item_entry_title);
            mSubject = mView.findViewById(R.id.list_item_entry_summary);
        }
    }

    public class ExamTimeInfo
    {
        public int time;
        public String subject;
    }
}