package com.nico_11_riv.intranetepitech;

import android.graphics.Color;
import android.graphics.RectF;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.nico_11_riv.intranetepitech.api.APIErrorHandler;
import com.nico_11_riv.intranetepitech.api.IntrAPI;
import com.nico_11_riv.intranetepitech.database.Schedule;
import com.nico_11_riv.intranetepitech.database.Userinfos;
import com.nico_11_riv.intranetepitech.database.setters.schedule.PSchedule;
import com.nico_11_riv.intranetepitech.database.setters.user.GUser;
import com.nico_11_riv.intranetepitech.database.setters.user.GUserInfos;
import com.nico_11_riv.intranetepitech.database.setters.user.PUserInfos;
import com.nico_11_riv.intranetepitech.toolbox.CircleTransform;
import com.nico_11_riv.intranetepitech.toolbox.IsConnected;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpClientErrorException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by nicol on 15/03/2016.
 */

@EFragment(R.layout.fragment_schedule)
public class ScheduleActivityFragment extends Fragment implements MonthLoader.MonthChangeListener, WeekView.EventClickListener {

    @RestService
    IntrAPI api;

    @Bean
    APIErrorHandler ErrorHandler;

    @ViewById
    WeekView weekView;

    private GUser user = new GUser();
    private GUserInfos user_info = new GUserInfos();
    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    private IsConnected ic;
    private String startDate;
    private String endDate;
    private Calendar calendar;
    private int wait;

    @UiThread
    public void mToast(String msg, int time) {
        Toast.makeText(getContext(), msg, time).show();
    }

    @AfterInject
    void afterInject() {
        api.setRestErrorHandler(ErrorHandler);
    }

    @UiThread
    void filluserinfosui() {
        TextView tv = (TextView) getActivity().findViewById(R.id.menu_login);
        tv.setText(user_info.getLogin());
        tv = (TextView) getActivity().findViewById(R.id.menu_email);
        tv.setText(user_info.getEmail());

        ImageView iv = (ImageView) getActivity().findViewById(R.id.menu_img);
        Picasso.with(getContext()).load(user_info.getPicture()).transform(new CircleTransform()).into(iv);
    }

    @Background
    void setUserInfos() {
        List<Userinfos> uInfos = Userinfos.findWithQuery(Userinfos.class, "SELECT * FROM Userinfos WHERE login = ?", user.getLogin());
        if (uInfos.size() > 0)
            filluserinfosui();
        if (ic.connected()) {
            Userinfos.deleteAll(Userinfos.class, "login = ?", user.getLogin());
            api.setCookie("PHPSESSID", user.getToken());
            try {
                PUserInfos infos = new PUserInfos();
                infos.init(api.getuserinfo(user.getLogin()));
            } catch (HttpClientErrorException e) {
                Log.d("Response", e.getResponseBodyAsString());
                mToast(e.getMessage(), Toast.LENGTH_SHORT);
            } catch (NullPointerException e) {
                mToast(e.getMessage(), Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
            user_info = new GUserInfos();
            filluserinfosui();
        }
    }

    void setStartDate(int newYear, int newMonth) {
        calendar = Calendar.getInstance();
        // passing month-1 because 0-->jan, 1-->feb... 11-->dec
        calendar.set(newYear, newMonth - 1, 1);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
        Date datee = calendar.getTime();
        DateFormat DATE_FORMATT = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
        startDate = DATE_FORMATT.format(datee);
    }

    void setEndDate(int newYear, int newMonth) {
        calendar = Calendar.getInstance();
        // passing month-1 because 0-->jan, 1-->feb... 11-->dec
        calendar.set(newYear, newMonth - 1, 1);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        Date date = calendar.getTime();
        DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
        endDate = DATE_FORMAT.format(date);
    }

    @UiThread
    void notif() {
        weekView.notifyDatasetChanged();
    }

    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month - 1) || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
    }

    @Background
    void setSchedule(int newYear, int newMonth) {
        setStartDate(newYear, newMonth);
        setEndDate(newYear, newMonth);
        if (ic.connected()) {
            api.setCookie("PHPSESSID", user.getToken());
            try {
                PSchedule pl = new PSchedule();
                pl.init(api.getplanning(startDate, endDate));
            } catch (HttpClientErrorException e) {
                Log.d("Response", e.getResponseBodyAsString());
                mToast(e.getMessage(), Toast.LENGTH_SHORT);
            } catch (NullPointerException e) {
                mToast(e.getMessage(), Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
        }
       events.clear();
        List<Schedule> pl = Schedule.findWithQuery(Schedule.class, "Select * from Schedule where login = ? and registerevent = ? or registerevent = ?", user.getLogin(), "registered", "present");
        for (int i = 0; i < pl.size(); i++) {
            Schedule info = pl.get(i);
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
            try {
                cal.setTime(df.parse(info.getStart()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cale = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
            try {
                cale.setTime(sdf.parse(info.getEnd()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            WeekViewEvent event = new WeekViewEvent(1, info.getActititle(), cal, cale);
            Map<String, String> eventypes = new HashMap<String, String>() {{
                put("class", "#6faabd");
                put("exam", "#d7906f");
                put("rdv", "#e2aa55");
                put("tp", "#a28ab9");
                put("other", "#668cb3");
            }};
            if (eventypes.get(info.getTypecode()) != null)
                event.setColor(Color.parseColor(eventypes.get(info.getTypecode())));
            else
                event.setColor(Color.parseColor("#668cb3"));
            events.add(event);
        }
        List<WeekViewEvent> matchedEvents = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : events) {
            if (eventMatches(event, newYear, newMonth)) {
                matchedEvents.add(event);
            }
        }
        events = matchedEvents;
//        notif();
        wait = 1;
    }

    @AfterViews
    void init() {
        ic = new IsConnected(getActivity().getApplicationContext());
        weekView.setMonthChangeListener(this);
        weekView.setOnEventClickListener(this);
        setUserInfos();
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        wait = 0;
        setSchedule(newYear, newMonth);
        while (wait == 0);
        return events;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }
}
