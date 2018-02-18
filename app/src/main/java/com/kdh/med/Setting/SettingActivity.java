package com.kdh.med.Setting;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.kdh.med.R;
import com.kdh.med.Tool.BapTool;
import com.kdh.med.Updates.UpdateAlarm;

import java.io.File;

/**
 * Created by KDH on 2018-02-18.
 */

public class SettingActivity extends AppCompatActivity
{
    public final static String mFilePath = "/data/data/com.kdh.med/databases/";
    public static final String ExamDBName = "WMEtest.db";
    public static final String TimeTableDBName = "WMECLASS.db";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("설정");
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

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(R.id.container, new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment
    {

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_setting);

            setOnPreferenceClick(findPreference("infoAutoUpdate"));
            setOnPreferenceChange(findPreference("autoBapUpdate"));
            setOnPreferenceChange(findPreference("updateLife"));
            setOnPreferenceClick(findPreference("deleted_table"));
        }

        private void setOnPreferenceClick(Preference mPreference)
        {
            mPreference.setOnPreferenceClickListener(onPreferenceClickListener);
        }

        private Preference.OnPreferenceClickListener onPreferenceClickListener = new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                String getKey = preference.getKey();

                if ("deleted_table".equals(getKey))
                {
                    int errCode = 0;

                    try
                    {
                        File f1 = new File(mFilePath + ExamDBName);

                        errCode = 1;
                        File f2 = new File(mFilePath + TimeTableDBName);

                        errCode = 2;
                        f1.delete();

                        errCode = 3;
                        f2.delete();

                        errCode = 4;
                        BapTool.removeBapData(getActivity().getBaseContext());

                    } catch (Exception e)
                    {
                        Toast.makeText(getActivity(), "오류가 발생했습니다! 에러코드 : " + errCode + "\n개발자에게 문의해 주세요!", Toast.LENGTH_LONG);
                    }

                }

                return true;
            }
        };

        private void setOnPreferenceChange(Preference mPreference)
        {
            mPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);

            if (mPreference instanceof ListPreference)
            {
                ListPreference listPreference = (ListPreference) mPreference;
                int index = listPreference.findIndexOfValue(listPreference.getValue());
                mPreference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
            }
            else if (mPreference instanceof EditTextPreference)
            {
                String values = ((EditTextPreference) mPreference).getText();
                if (values == null) values = "";
                onPreferenceChangeListener.onPreferenceChange(mPreference, values);
            }
        }

        private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener()
        {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                String stringValue = newValue.toString();

                if (preference instanceof EditTextPreference)
                {
                    preference.setSummary(stringValue);

                }
                else if (preference instanceof ListPreference)
                {

                    /**
                     * ListPreference의 경우 stringValue가 entryValues이기 때문에 바로 Summary를
                     * 적용하지 못한다 따라서 설정한 entries에서 String을 로딩하여 적용한다
                     */
                    ListPreference listPreference = (ListPreference) preference;
                    int index = listPreference.findIndexOfValue(stringValue);

                    preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

                    UpdateAlarm updateAlarm = new UpdateAlarm(getActivity());
                    updateAlarm.cancel();

                    if (index == 0) updateAlarm.autoUpdate();
                    else if (index == 1) updateAlarm.SaturdayUpdate();
                    else if (index == 2) updateAlarm.SundayUpdate();

                }
                else if (preference instanceof CheckBoxPreference)
                {
                    com.kdh.med.Tool.Preference mPref = new com.kdh.med.Tool.Preference(getActivity());

                    if (mPref.getBoolean("firstOfAutoUpdate", true))
                    {
                        mPref.putBoolean("firstOfAutoUpdate", false);
                    }

                    if (!mPref.getBoolean("autoBapUpdate", false) && preference.isEnabled())
                    {
                        int updateLife = Integer.parseInt(mPref.getString("updateLife", "0"));

                        UpdateAlarm updateAlarm = new UpdateAlarm(getActivity());
                        if (updateLife == 1) updateAlarm.autoUpdate();
                        else if (updateLife == 0) updateAlarm.SaturdayUpdate();
                        else if (updateLife == -1) updateAlarm.SundayUpdate();

                    }
                    else
                    {
                        UpdateAlarm updateAlarm = new UpdateAlarm(getActivity());
                        updateAlarm.cancel();
                    }
                }
                return true;
            }
        };
    }
}
