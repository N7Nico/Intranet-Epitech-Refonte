package com.nico_11_riv.intranetepitech.database.setters.projects;

import android.util.Log;
import android.webkit.URLUtil;

import com.nico_11_riv.intranetepitech.api.IntrAPI;
import com.nico_11_riv.intranetepitech.database.Allmodules;
import com.nico_11_riv.intranetepitech.database.Modules;
import com.nico_11_riv.intranetepitech.database.Projects;
import com.nico_11_riv.intranetepitech.database.Userinfos;
import com.nico_11_riv.intranetepitech.database.setters.user.GUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;

/**
 * Created by Jimmy on 02/03/2016.
 */

public class PProject {

    GUser user = new GUser();
    List<Userinfos> info = Userinfos.findWithQuery(Userinfos.class, "SELECT * FROM Userinfos WHERE login = ?", user.getLogin());

    public void parsemoduleapi(String rslt, IntrAPI api) {
        try {
            JSONArray a = new JSONArray(rslt);
            for (int i = 0; i < a.length(); i++) {
                if (!Objects.equals(a.getJSONObject(i).getString("status"), "notregistered")) {
                    api.setCookie("PHPSESSID", user.getToken());
                    String returnapi = api.getactivite(a.getJSONObject(i).getString("scolaryear"), a.getJSONObject(i).getString("code"), a.getJSONObject(i).getString("codeinstance"));
                    JSONObject object = null;
                    try {
                        object = new JSONObject(returnapi);
                        if (object.has("activites") && Objects.equals(object.getString("student_registered"), "1")) {
                            JSONArray array = object.getJSONArray("activites");
                            for (int o = 0; o < array.length(); o++) {
                                try {
                                    JSONObject tmp = array.getJSONObject(o);
                                    if (Objects.equals(tmp.getString("is_projet"), "true")) {
                                        api.setCookie("PHPSESSID", user.getToken());
                                        getProject(api.getproject(a.getJSONObject(i).getString("scolaryear"), a.getJSONObject(i).getString("code"), a.getJSONObject(i).getString("codeinstance"), tmp.getString("codeacti")), api);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parsemoduledb(List<Modules> allmodules, int i, IntrAPI api) {
        api.setCookie("PHPSESSID", user.getToken());
        String returnapi = api.getactivite(allmodules.get(i).getScolaryear(), allmodules.get(i).getCodemodule(), allmodules.get(i).getCodeinstance());
        JSONObject object = null;
        try {
            object = new JSONObject(returnapi);
            if (object.has("activites") && Objects.equals(object.getString("student_registered"), "1")) {
                JSONArray array = object.getJSONArray("activites");
                for (int a = 0; a < array.length(); a++) {
                    try {
                        JSONObject tmp = array.getJSONObject(a);
                        if (Objects.equals(tmp.getString("is_projet"), "true")) {
                            api.setCookie("PHPSESSID", user.getToken());
                            getProject(api.getproject(allmodules.get(i).getScolaryear(), allmodules.get(i).getCodemodule(), allmodules.get(i).getCodeinstance(), tmp.getString("codeacti")), api);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseallmoduledb(List<Allmodules> allmodules, int i, IntrAPI api) {
        api.setCookie("PHPSESSID", user.getToken());
        String returnapi = api.getactivite(allmodules.get(i).getScolaryear(), allmodules.get(i).getCode(), allmodules.get(i).getCodeinstance());
        JSONObject object = null;
        try {
            object = new JSONObject(returnapi);
            if (object.has("activites") && Objects.equals(object.getString("student_registered"), "1")) {
                JSONArray array = object.getJSONArray("activites");
                for (int a = 0; a < array.length(); a++) {
                    try {
                        JSONObject tmp = array.getJSONObject(a);
                        if (Objects.equals(tmp.getString("is_projet"), "true")) {
                            api.setCookie("PHPSESSID", user.getToken());
                            getProject(api.getproject(allmodules.get(i).getScolaryear(), allmodules.get(i).getCode(), allmodules.get(i).getCodeinstance(), tmp.getString("codeacti")), api);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public PProject(IntrAPI api) {
        List<Allmodules> allmodules = Allmodules.findWithQuery(Allmodules.class, "SELECT * FROM Allmodules WHERE login = ?", this.user.getLogin());
        List<Modules> modules= Modules.findWithQuery(Modules.class, "SELECT * FROM Modules WHERE login = ?", this.user.getLogin());
        if (allmodules.size() == 0) {
            if (modules.size() == 0) {
                api.setCookie("PHPSESSID", user.getToken());
                parsemoduleapi(api.getallmodules(), api);
            }
            else if (modules.size() > 0) {
                for (int i = 0; i < modules.size(); i++) {
                    parsemoduledb(modules, i, api);
                }
            }
        }
        else {
            for (int i = 0; i < allmodules.size(); i++) {
                parseallmoduledb(allmodules, i, api);
            }
        }
    }


    public void getProject(String api, IntrAPI intra) {
        JSONObject ori = null;
        try {
            ori = new JSONObject(api);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Projects project = new Projects(this.user.getLogin());
            project.setScolaryear(ori != null ? ori.getString("scolaryear") : null);
            project.setCodemodule(ori != null ? ori.getString("codemodule") : null);
            project.setCodeinstance(ori != null ? ori.getString("codeinstance") : null);
            project.setCodeacti(ori != null ? ori.getString("codeacti") : null);
            project.setInstancelocation(ori != null ? ori.getString("instance_location") : null);
            project.setModuletitle(ori != null ? ori.getString("module_title") : null);
            project.setIdactivite(ori != null ? ori.getString("id_activite") : null);
            project.setProjecttitle(ori != null ? ori.getString("project_title") : null);
            project.setTypecode(ori != null ? ori.getString("type_title") : null);
            project.setTypecode(ori != null ? ori.getString("type_code") : null);
            project.setRegister(ori != null ? ori.getString("register") : null);
            project.setNbmin(ori != null ? ori.getString("nb_min") : null);
            project.setNbmax(ori != null ? ori.getString("nb_max") : null);
            project.setBegin(ori != null ? ori.getString("begin") : null);
            project.setEnd(ori != null ? ori.getString("end") : null);
            project.setEndregister(ori != null ? ori.getString("end_register") : null);
            project.setDeadline(ori != null ? ori.getString("deadline") : null);
            project.setTitle(ori != null ? ori.getString("title") : null);
            project.setDescription(ori != null ? ori.getString("description") : null);
            project.setClosed(ori != null ? ori.getString("closed") : null);
            project.setInstanceregistered(ori != null ? ori.getString("instance_registered") : null);
            project.setUserprojectstatus(ori != null ? ori.getString("user_project_status") : null);
            project.setFileurl("");
            if (!Objects.equals(ori != null ? ori.getString("type_code") : null, "rdv") && URLUtil.isValidUrl("https://intra.epitech.eu/module/" + (ori != null ? ori.getString("scolaryear") : null) + "/" + (ori != null ? ori.getString("codemodule") : null) + "/" + (ori != null ? ori.getString("codeinstance") : null) + "/" + (ori != null ? ori.getString("codeacti") : null))) {
                intra.setCookie("PHPSESSID", user.getToken());
                String path = "https://www.intra.epitech.eu";
                String in = null;
                try {
                    in = intra.getprojectfile(ori != null ? ori.getString("scolaryear") : null, ori != null ? ori.getString("codemodule") : null, ori != null ? ori.getString("codeinstance") : null, ori != null ? ori.getString("codeacti") : null);
                } catch (HttpClientErrorException e) {
                    Log.d("Response", e.getResponseBodyAsString());
                }
                try {
                    JSONArray json = new JSONArray(in);
                    JSONObject tmp = json.getJSONObject(0);
                    if (tmp.has("fullpath")) {
                        path += tmp.getString("fullpath");
                        project.setFileurl(path);
                    }
                } catch (JSONException e) {
                    project.setFileurl("");
                    e.printStackTrace();
                } catch (HttpClientErrorException e) {
                    Log.d("Response", e.getResponseBodyAsString());
                }  catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            project.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
