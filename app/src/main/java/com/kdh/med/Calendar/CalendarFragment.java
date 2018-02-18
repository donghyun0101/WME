package com.kdh.med.Calendar;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kdh.med.R;
import com.kdh.med.Tool.Preference;
import com.kdh.med.Tool.RecyclerItemClickListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.kdh.med.Tool.BapTool.BAP_PREFERENCE_NAME;

/**
 * Created by KDH on 2016-06-16.
 * Modified by KDH on 2018-02-18.
 */

public class CalendarFragment extends Fragment
{
    public static final String EVENT_PREFERENCE_NAME = "WMEeventData";

    public static Fragment getInstance(int month)
    {
        CalendarFragment mFragment = new CalendarFragment();

        Bundle args = new Bundle();
        args.putInt("month", month);
        mFragment.setArguments(args);

        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        CalendarFragment.CalendarAsyncTask calenderUrl = new CalendarFragment.CalendarAsyncTask();
        calenderUrl.execute();

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recyclerview, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        final CalendarAdapter mAdapter = new CalendarAdapter();
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View mView, int position)
            {
                try
                {
                    String date = mAdapter.getItemData(position).date;
                    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);

                    Calendar mCalendar = Calendar.getInstance();
                    long nowTime = mCalendar.getTimeInMillis();

                    mCalendar.setTime(mFormat.parse(date));
                    long touchTime = mCalendar.getTimeInMillis();

                    long diff = (touchTime - nowTime);

                    boolean isPast = false;
                    if (diff < 0)
                    {
                        diff = -diff;
                        isPast = true;
                    }

                    int diffDays = (int) (diff /= 24 * 60 * 60 * 1000);
                    String mText = "";

                    if (diffDays == 0)
                        mText += "오늘 일정입니다.";
                    else if (isPast)
                        mText = "선택하신 날짜는 " + diffDays + "일전 날짜입니다";
                    else
                        mText = "선택하신 날짜까지 " + diffDays + "일 남았습니다";

                    Snackbar.make(mView, mText, Snackbar.LENGTH_SHORT).show();
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }));

        Bundle args = getArguments();
        int month = args.getInt("month");

        switch (month)
        {
            case 3:
                mAdapter.addItem("3.1절", "2016.03.01", true);
                mAdapter.addItem("입학식", "2016.03.02");
                mAdapter.addItem("잔류주", "2016.03.05");
                mAdapter.addItem("학급꾸미기 주간(03.07~03.11)", "2016.03.07");
                mAdapter.addItem("방과후시작/학부모상담주간/수업공개주간", "2016.03.14");
                mAdapter.addItem("학부모총회 및 학교설명회", "2016.03.17");
                mAdapter.addItem("국제의료기기병원설비전시회참석", "2016.03.18");
                mAdapter.addItem("잔류주", "2016.03.19");
                mAdapter.addItem("2016대한민국 고졸인재 Job Concert 참석", "2016.03.23");
                mAdapter.addItem("봉사활동", "2016.03.25");
                mAdapter.addItem("결핵검진(2,3학년)", "2016.03.28");
                mAdapter.addItem("체육관, 운동장 등 교내 외 시설 문화의날 행사", "2016.03.30");
                break;
            case 4:
                mAdapter.addItem("잔류주/2회기능사필기시험", "2016.04.02");
                mAdapter.addItem("식목일", "2016.04.05");
                mAdapter.addItem("학생건강검진", "2016.04.05");
                mAdapter.addItem("지방기능경기대회(04.06~04.11)", "2016.04.06");
                mAdapter.addItem("요양기관 봉사활동", "2016.04.08");
                mAdapter.addItem("표준화심리검사/영어어휘력평가(1,2학년)", "2016.04.08");
                mAdapter.addItem("국회의원 총선거", "2016.04.13");
                mAdapter.addItem("잔류주", "2016.04.16");
                mAdapter.addItem("영어듣기평가", "2016.04.19");
                mAdapter.addItem("병무행정설명회", "2016.04.20");
                mAdapter.addItem("성매매예방교육", "2016.04.22");
                mAdapter.addItem("공동실습소입소(04.25~04.29)", "2016.04.25");
                mAdapter.addItem("체육관, 운동장, 학교시설, 외부 등 문화의날 행사", "2016.04.27");
                mAdapter.addItem("잔류주", "2016.04.30");
                break;
            case 5:
                mAdapter.addItem("1회고사(중간-05.01~05.02)", "2016.05.01");
                mAdapter.addItem("재량휴업일(05.04~05.06)", "2016.05.04");
                mAdapter.addItem("산길걷기행사(학과별소풍)", "2016.05.13");
                mAdapter.addItem("교내체육대회", "2016.05.19");
                mAdapter.addItem("잔류주", "2016.05.21");
                mAdapter.addItem("봉사활동(요양기관)", "2016.05.27");
                mAdapter.addItem("시력검사", "2016.05.27");
                break;
            case 6:
                mAdapter.addItem("문화의날", "2016.06.01");
                mAdapter.addItem("건강체력평가", "2016.06.02");
                mAdapter.addItem("팝송경연대회", "2016.06.03");
                mAdapter.addItem("공동실습소입소(06.20~06.24)", "2016.06.20");
                mAdapter.addItem("잔류주", "2016.06.18");
                mAdapter.addItem("봉사활동(요양기관)", "2016.06.24");
                mAdapter.addItem("생명존중교육", "2016.06.24");
                break;
            case 7:
                mAdapter.addItem("잔류주", "2016.07.02");
                mAdapter.addItem("성폭력예방교육", "2016.07.08");
                mAdapter.addItem("잔류주", "2016.07.09");
                mAdapter.addItem("2회고사(기말-07.11~07.13)", "2016.07.11");
                mAdapter.addItem("배드민턴대회", "2016.07.13");
                mAdapter.addItem("학부모간담회 및 특강(예정안)", "2016.07.15");
                mAdapter.addItem("학과별프로젝트발표대회(07.18~07.19)", "2016.07.18");
                mAdapter.addItem("방학식", "2016.07.20");
                mAdapter.addItem("방학중방과후학교시작(예정)", "2016.07.21");
                break;
            case 8:
                mAdapter.addItem("개학식", "2016.08.16");
                break;
            case 9:
                mAdapter.addItem("재량휴업일", "2016.09.12");
                mAdapter.addItem("재량휴업일", "2016.09.13");
                break;
            case 10:
                mAdapter.addItem("중간고사", "2016.10.4");
                mAdapter.addItem("중간고사", "2016.10.5");
                mAdapter.addItem("문화의 날", "2016.10.12");
                mAdapter.addItem("해오름 축제", "2016.10.13");
                mAdapter.addItem("흡연 예방 교육", "2016.10.14");
                mAdapter.addItem("영어 체험 활동", "2016.10.19");
                mAdapter.addItem("신입생 원서접수\n영어 어휘력 평가", "2016.10.24");
                mAdapter.addItem("환경미화 심사\n봉사활동", "2016.10.28");
                mAdapter.addItem("수학여행(1학년)", "2016.10.30");
                mAdapter.addItem("수학여행(1학년)", "2016.10.31");
                break;
            case 11:
                mAdapter.addItem("수학여행(1학년)\n1차 합격자 발표\n(본교 홈페이지)", "2016.11.1");
                mAdapter.addItem("수학여행(1학년)", "2016.11.2");
                mAdapter.addItem("수학여행(1학년)\n입시전형일", "2016.11.3");
                mAdapter.addItem("RA자격증 시험", "2016.11.6");
                mAdapter.addItem("신입생 최종합격자 발표", "2016.11.9");
                mAdapter.addItem("동아리활동\n환경미화 심사", "2016.11.11");
                mAdapter.addItem("졸업고사(3학년)", "2016.11.14");
                mAdapter.addItem("졸업고사(3학년)", "2016.11.15");
                mAdapter.addItem("졸업고사(3학년)", "2016.11.16");
                mAdapter.addItem("학부모 간담회", "2016.11.18");
                mAdapter.addItem("개교기념일", "2016.11.21", true);
                mAdapter.addItem("문화의 날", "2016.11.23");
                mAdapter.addItem("신입생 1차 소집일", "2016.11.30");
                break;
            case 12:
                mAdapter.addItem("영어 인증 시험", "2016.12.2");
                mAdapter.addItem("졸업평가회", "2016.12.6");
                mAdapter.addItem("기말고사(1, 2학년)", "2016.12.14");
                mAdapter.addItem("기말고사(1, 2학년)", "2016.12.15");
                mAdapter.addItem("기말고사(1, 2학년)", "2016.12.16");
                mAdapter.addItem("의료기기 현장체험(중국 상해)", "2016.12.20");
                mAdapter.addItem("의료기기 현장체험(중국 상해)", "2016.12.21");
                mAdapter.addItem("의료기기 현장체험(중국 상해)", "2016.12.22");
                mAdapter.addItem("의료기기 현장체험(중국 상해)\n봉사활동", "2016.12.23");
                mAdapter.addItem("학과별 발표대회(전자과)", "2016.12.26");
                mAdapter.addItem("학과별 발표대회(기계과)", "2016.12.27");
                mAdapter.addItem("진급평가회(1, 2학년)", "2016.12.28");
                mAdapter.addItem("겨울방학식", "2016.12.30");
                break;
            case 1:
                mAdapter.addItem("합격자 등록", "2017.1.3");
                mAdapter.addItem("합격자 등록\n방학중 방과후 학교 실시", "2017.1.4");
                mAdapter.addItem("합격자 등록\n방학중 방과후 학교 실시", "2017.1.5");
                mAdapter.addItem("합격자 등록\n방학중 방과후 학교 실시", "2017.1.6");
                mAdapter.addItem("방학중 방과후 학교 실시", "2017.1.7");
                mAdapter.addItem("방학중 방과후 학교 실시", "2017.1.8");
                mAdapter.addItem("방학중 방과후 학교 실시", "2017.1.9");
                mAdapter.addItem("방학중 방과후 학교 실시", "2017.1.10");
                mAdapter.addItem("방학중 방과후 학교 실시", "2017.1.11");
                break;
            case 2:
                mAdapter.addItem("개학", "2017.2.7");
                mAdapter.addItem("졸업식 종업식", "2017.2.10");
                mAdapter.addItem("봄방학(18일간)", "2017.2.11");
                mAdapter.addItem("개학", "2017.2.28");
                break;
        }

        return recyclerView;
    }

    private class CalendarAsyncTask extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog asyncDialog = new ProgressDialog(getContext());
        Preference mPref = new Preference(getContext(), EVENT_PREFERENCE_NAME);

        @Override
        protected void onPreExecute()
        {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("일정을 가져오는 중...");
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                String schdYear, schdMonth;
                int cntF = 0, cntS = 0;
                int tempYear;

                SimpleDateFormat mFormatYear = new SimpleDateFormat("yyyy", Locale.KOREA);

                Calendar mCalendar = Calendar.getInstance();
                long nowDate = mCalendar.getTimeInMillis();
                int baseMonth = 3;

                schdYear = mFormatYear.format(nowDate);

                for (int i = 0; i < 2; i++)
                {
                    if (baseMonth == 13)
                    {
                        baseMonth += (-12);
                        tempYear = Integer.parseInt(schdYear) + 1;
                        schdYear = String.valueOf(tempYear);
                    }

                    schdMonth = String.format("%02d", baseMonth);
                    baseMonth += 1;

                    Document doc = Jsoup.connect("http://180.81.125.27/schedule/list.do?&m=0209&s=wjmedi&schdYear=" + schdYear + "&schdMonth=" + schdMonth).get();

                    Elements calenderLink = doc.select(".tb_base_box.tm_box tr");
                    Elements cDate = calenderLink.select("th[scope=row] span");
                    Elements cData = calenderLink.select("td");

                    String[][] mEvent = new String[500][500];

                    for (Element link : cDate)
                    {
                        mEvent[cntF][cntS] = link.text();
                        cntF++;
                    }

                    cntF = 0;
                    cntS++;

                    for (Element link : cData)
                    {
                        if (link.hasText())
                            mEvent[cntF][cntS] = link.text();
                        else
                            mEvent[cntF][cntS] = null;
                        cntF++;
                    }

                    cntF = 0;

                    for (Element link : cDate)
                    {
                        Log.wtf("mDate", schdYear + " " + schdMonth + " " + mEvent[cntF][cntS - 1] + " " + mEvent[cntF][cntS]);
                        cntF++;
                    }

                    cntS++;

                    Log.d("mDate", cntF + " " + cntS);

                    //mPref.putString("", "");\
                }

            } catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            asyncDialog.dismiss();
        }
    }
}

