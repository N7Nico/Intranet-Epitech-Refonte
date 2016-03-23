package com.nico_11_riv.intranetepitech.database.setters.user;

import com.nico_11_riv.intranetepitech.database.Userinfos;

import org.json.JSONException;
import org.json.JSONObject;

public class PUserInfos {
    public PUserInfos() {
    }

    public void init(String api) {
        try {
            GUser user = new GUser();
            JSONObject json = new JSONObject(api);
            if (json.has("login")) {
                Userinfos userinfos = new Userinfos(user.getLogin());
                userinfos.setLogin(json.getString("login"));
                userinfos.setTitle(json.getString("title"));
                userinfos.setLastname(json.getString("lastname"));
                userinfos.setFirstname(json.getString("firstname"));
                userinfos.setEmail(user.getLogin() + "@epitech.eu");
                userinfos.setScolaryear(json.getString("scolaryear"));
                userinfos.setPicture(json.getString("picture"));
                userinfos.setPromo(json.getString("promo"));
                userinfos.setSemester(json.getString("semester"));
                userinfos.setLocation(json.getString("location"));
                userinfos.setCoursecode(json.getString("course_code"));
                userinfos.setStudentyear(json.getString("studentyear"));
                userinfos.setCredits(json.getString("credits"));
                userinfos.setGpa(json.getJSONArray("gpa").getJSONObject(0).getString("gpa"));
                userinfos.save();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
