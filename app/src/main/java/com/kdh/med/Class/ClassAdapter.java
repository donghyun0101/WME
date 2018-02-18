package com.kdh.med.Class;

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

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.TimeTableViewHolder>
{
    //    private int mBackground;
    private ArrayList<TimeTableInfo> mValues = new ArrayList<>();

    public ClassAdapter(Context mContext)
    {
//        TypedValue mTypedValue = new TypedValue();
//        mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
//        mBackground = mTypedValue.resourceId;
    }

    public void addItem(int time, String name)
    {
        TimeTableInfo addInfo = new TimeTableInfo();

        addInfo.time = time;
        addInfo.name = name;

        mValues.add(addInfo);
    }

    @Override
    public ClassAdapter.TimeTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_class_item, parent, false);
//      mView.setBackgroundResource(mBackground);

        return new TimeTableViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final TimeTableViewHolder holder, int position)
    {
        TimeTableInfo mInfo = getItemData(position);

        holder.mTimeText.setText(String.valueOf(mInfo.time));
        holder.mTimeName.setText(mInfo.name);
    }

    @Override
    public int getItemCount()
    {
        return mValues.size();
    }

    public TimeTableInfo getItemData(int position)
    {
        return mValues.get(position);
    }

    public class TimeTableViewHolder extends RecyclerView.ViewHolder
    {
        //        public final View mView;
        public final TextView mTimeText, mTimeName;

        public TimeTableViewHolder(View mView)
        {
            super(mView);
//            this.mView = mView;

            mTimeText = mView.findViewById(R.id.mTimeText);
            mTimeName = mView.findViewById(R.id.mTimeName);
        }
    }

    public class TimeTableInfo
    {
        public int time;
        public String name;
    }
}