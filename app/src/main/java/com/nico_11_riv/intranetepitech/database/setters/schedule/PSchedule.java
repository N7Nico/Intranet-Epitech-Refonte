package com.nico_11_riv.intranetepitech.database.setters.schedule;

/**
 *
 * Created by victor on 19/01/2016.
 *
 */

import com.nico_11_riv.intranetepitech.database.Schedule;
import com.nico_11_riv.intranetepitech.database.setters.user.GUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;


public class PSchedule {
    public PSchedule() {
    }

    public void init(String api, int newYear, int newMonth) {
        GUser user = new GUser();
        try {
            JSONArray planning = new JSONArray(api);
            /*List<Schedule> m = Schedule.findWithQuery(Schedule.class, "SELECT * FROM Schedule WHERE login = ? AND startdate = ? AND enddate = ?", user.getLogin(), startDate, endDate);
            for (int n = 0; n < m.size(); n++) {
                Schedule schedule = m.get(n);
                schedule.delete();
            }*/
            for (int i = 0; i < planning.length(); ++i) {
                JSONObject tmp = planning.getJSONObject(i);
                if (tmp.has("scolaryear")) {
                    Schedule pl;
                    List<Schedule> m = Schedule.findWithQuery(Schedule.class, "SELECT * FROM Schedule WHERE login = ? AND codeevent = ?", user.getLogin(), !Objects.equals(tmp.getString("codeevent"), "null") ? tmp.getString("codeevent") : "n/a");
                    if (m.size() < 1) {
                        pl = new Schedule(user.getLogin());
                        pl.setScolaryear(!Objects.equals(tmp.getString("scolaryear"), "null") ? tmp.getString("scolaryear") : "n/a");
                        pl.setCodemodule(!Objects.equals(tmp.getString("codemodule"), "null") ? tmp.getString("codemodule") : "n/a");
                        pl.setCodeinstance(!Objects.equals(tmp.getString("codeinstance"), "null") ? tmp.getString("codeinstance") : "n/a");
                        pl.setCodeacti(!Objects.equals(tmp.getString("codeacti"), "null") ? tmp.getString("codeacti") : "n/a");
                        pl.setCodeevent(!Objects.equals(tmp.getString("codeevent"), "null") ? tmp.getString("codeevent") : "n/a");
                        pl.setSemester(!Objects.equals(tmp.getString("semester"), "null") ? tmp.getString("semester") : "n/a");
                        pl.setTitlemodule(!Objects.equals(tmp.getString("titlemodule"), "null") ? tmp.getString("titlemodule") : "n/a");
                        pl.setActititle(!Objects.equals(tmp.getString("acti_title"), "null") ? tmp.getString("acti_title") : "n/a");
                        pl.setStart(!Objects.equals(tmp.getString("start"), "null") ? tmp.getString("start") : "n/a");
                        pl.setEnd(!Objects.equals(tmp.getString("end"), "null") ? tmp.getString("end") : "n/a");
                        pl.setResigtermodule(!Objects.equals(tmp.getString("module_registered"), "null") ? tmp.getString("module_registered") : "n/a");
                        pl.setAllowtoken(!Objects.equals(tmp.getString("allow_token"), "null") ? tmp.getString("allow_token") : "n/a");
                        pl.setRegisterevent(!Objects.equals(tmp.getString("event_registered"), "null") ? tmp.getString("event_registered") : "n/a");
                        pl.setTypecode(!Objects.equals(tmp.getString("type_code"), "null") ? tmp.getString("type_code") : "n/a");
                        pl.setNewyear(newYear);
                        pl.setNewmonth(newMonth);
                        pl.save();
                    }


                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}