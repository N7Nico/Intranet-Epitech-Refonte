package com.nico_11_riv.intranetepitech.database.setters.projects;

import com.nico_11_riv.intranetepitech.database.Allprojects;
import com.nico_11_riv.intranetepitech.database.setters.user.GUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 *
 * Created by Jimmy on 10/02/2016.
 *
 */
public class PAllProjects {
    public PAllProjects(String api) {
        GUser user = new GUser();
        try {
            JSONArray jsonArray = new JSONArray(api);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tmp = jsonArray.getJSONObject(i);
                Allprojects allprojects = new Allprojects(user.getLogin());
                allprojects.setCodemodule(!Objects.equals(tmp.getString("codemodule"), "null") ? tmp.getString("codemodule") : "n/a");
                allprojects.setProject(!Objects.equals(tmp.getString("project"), "null") ? tmp.getString("project") : "n/a");
                allprojects.setEndacti(!Objects.equals(tmp.getString("end_acti"), "null") ? tmp.getString("end_acti") : "n/a");
                allprojects.setActititle(!Objects.equals(tmp.getString("acti_title"), "null") ? tmp.getString("acti_title") : "n/a");
                allprojects.setTitlemodule(!Objects.equals(tmp.getString("title_module"), "null") ? tmp.getString("title_module") : "n/a");
                allprojects.setBeginacti(!Objects.equals(tmp.getString("begin_acti"), "null") ? tmp.getString("begin_acti") : "n/a");
                allprojects.setScolaryear(!Objects.equals(tmp.getString("scolaryear"), "null") ? tmp.getString("scolaryear") : "n/a");
                allprojects.setCodeacti(!Objects.equals(tmp.getString("codeacti"), "null") ? tmp.getString("codeacti") : "n/a");
                allprojects.setRegistered(!Objects.equals(tmp.getString("registered"), "null") ? tmp.getString("registered") : "n/a");
                allprojects.setCodeinstance(!Objects.equals(tmp.getString("codeinstance"), "null") ? tmp.getString("codeinstance") : "n/a");
                allprojects.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
