package com.nico_11_riv.intranetepitech.database;

import android.text.Html;

import com.nico_11_riv.intranetepitech.database.setters.user.GUser;
import com.orm.SugarRecord;

public class Messages extends SugarRecord {
    private String token;
    private boolean old;
    private String idmessage;
    private String title;
    private String content;
    private String date;
    private String login;
    private String picture;

    public Messages () {
        GUser user = new GUser();
        this.token = user.getToken();
        this.old = false;
    }

    public Messages(String token) {
        this.token = token;
        this.old = false;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isOld() {
        return old;
    }

    public void setOld(boolean old) {
        this.old = old;
    }

    public String getIdmessage() {
        return idmessage;
    }

    public void setIdmessage(String idmessage) {
        this.idmessage = idmessage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
