package com.kdh.med.bap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kdh.med.R;
import com.kdh.med.Tool.BapTool;

import java.util.ArrayList;

/**
 * Created by whdghks913 on 2015-12-01.
 * Modified by KDH on 2018-02-18.
 */

class BapViewHolder
{
    public TextView mCalender;
    public TextView mDayOfTheWeek;
    public TextView mBfast;
    public TextView mLunch;
    public TextView mDinner;
    public TextView mBfast_kcal;
    public TextView mLunch_kcal;
    public TextView mDinner_kcal;
}

class BapListData
{
    public String mCalender;
    public String mDayOfTheWeek;
    public String mBfast;
    public String mLunch;
    public String mDinner;
    public String mBfast_kcal;
    public String mLunch_kcal;
    public String mDinner_kcal;
    public boolean isToday;
}

public class BapAdapter extends BaseAdapter
{
    private Context mContext = null;
    private ArrayList<BapListData> mListData = new ArrayList<BapListData>();

    public BapAdapter(Context mContext)
    {
        super();

        this.mContext = mContext;
    }

    public void addItem(String mCalender, String mDayOfTheWeek, String mBfast, String mLunch, String mDinner, String mBfast_kcal, String mLunch_kcal, String mDinner_kcal, boolean isToday)
    {
        BapListData addItemInfo = new BapListData();
        addItemInfo.mCalender = mCalender;
        addItemInfo.mDayOfTheWeek = mDayOfTheWeek;
        addItemInfo.mBfast = mBfast;
        addItemInfo.mLunch = mLunch;
        addItemInfo.mDinner = mDinner;
        addItemInfo.mBfast_kcal = mBfast_kcal;
        addItemInfo.mLunch_kcal = mLunch_kcal;
        addItemInfo.mDinner_kcal = mDinner_kcal;
        addItemInfo.isToday = isToday;

        mListData.add(addItemInfo);
    }

    public void clearData()
    {
        mListData.clear();
    }

    @Override
    public int getCount()
    {
        return mListData.size();
    }

    @Override
    public BapListData getItem(int position)
    {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        BapViewHolder mHolder;

        if (convertView == null)
        {
            mHolder = new BapViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_bap_item, null);

            mHolder.mCalender = convertView.findViewById(R.id.mCalender);
            mHolder.mDayOfTheWeek = convertView.findViewById(R.id.mDayOfTheWeek);
            mHolder.mBfast = convertView.findViewById(R.id.mBfast);
            mHolder.mLunch = convertView.findViewById(R.id.mLunch);
            mHolder.mDinner = convertView.findViewById(R.id.mDinner);
            mHolder.mBfast_kcal = convertView.findViewById(R.id.mBkcal);
            mHolder.mLunch_kcal = convertView.findViewById(R.id.mLkcal);
            mHolder.mDinner_kcal = convertView.findViewById(R.id.mDkcal);

            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (BapViewHolder) convertView.getTag();
        }

        BapListData mData = mListData.get(position);

        String mCalender = mData.mCalender;
        String mDayOfTheWeek = mData.mDayOfTheWeek;
        String mBfast = mData.mBfast;
        String mLunch = mData.mLunch;
        String mDinner = mData.mDinner;
        String mBfast_kcal = mData.mBfast_kcal;
        String mLunch_kcal = mData.mLunch_kcal;
        String mDinner_kcal = mData.mDinner_kcal;

        if (BapTool.mStringCheck(mBfast))
            mBfast = mContext.getResources().getString(R.string.no_data_bfast);
        else mBfast = BapTool.replaceString(mBfast);

        if (BapTool.mStringCheck(mLunch))
            mLunch = mContext.getResources().getString(R.string.no_data_lunch);
        else mLunch = BapTool.replaceString(mLunch);

        if (BapTool.mStringCheck(mDinner))
            mDinner = mContext.getResources().getString(R.string.no_data_dinner);
        else mDinner = BapTool.replaceString(mDinner);

        if (BapTool.mStringCheck(mBfast_kcal))
            mBfast_kcal = mContext.getResources().getString(R.string.no_data_kcal);
        if (BapTool.mStringCheck(mLunch_kcal))
            mLunch_kcal = mContext.getResources().getString(R.string.no_data_kcal);
        if (BapTool.mStringCheck(mDinner_kcal))
            mDinner_kcal = mContext.getResources().getString(R.string.no_data_kcal);

        mHolder.mCalender.setText(mCalender);
        mHolder.mDayOfTheWeek.setText(mDayOfTheWeek);
        mHolder.mBfast.setText(mBfast);
        mHolder.mLunch.setText(mLunch);
        mHolder.mDinner.setText(mDinner);
        mHolder.mBfast_kcal.setText(mBfast_kcal);
        mHolder.mLunch_kcal.setText(mLunch_kcal);
        mHolder.mDinner_kcal.setText(mDinner_kcal);

        return convertView;
    }
}