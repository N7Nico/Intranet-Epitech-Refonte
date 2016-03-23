package com.nico_11_riv.intranetepitech.database;

import com.orm.SugarRecord;

public class Currentprojets extends SugarRecord {
    private String login;
    private String title;
    private String titlelink;
    private String timelinestart;
    private String timelineend;
    private String idactivite;
    private String soutenancename;
    private String soutenancelink;
    private String soutenancesalle;
    private String titlemodule;

    public Currentprojets() {
    }

    public Currentprojets(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitlelink() {
        return titlelink;
    }

    public void setTitlelink(String titlelink) {
        this.titlelink = titlelink;
    }

    public String getTimelinestart() {
        return timelinestart;
    }

    public void setTimelinestart(String timelinestart) {
        this.timelinestart = timelinestart;
    }

    public String getTimelineend() {
        return timelineend;
    }

    public void setTimelineend(String timelineend) {
        this.timelineend = timelineend;
    }

    public String getIdactivite() {
        return idactivite;
    }

    public void setIdactivite(String idactivite) {
        this.idactivite = idactivite;
    }

    public String getSoutenancename() {
        return soutenancename;
    }

    public void setSoutenancename(String soutenancename) {
        this.soutenancename = soutenancename;
    }

    public String getSoutenancelink() {
        return soutenancelink;
    }

    public void setSoutenancelink(String soutenancelink) {
        this.soutenancelink = soutenancelink;
    }

    public String getSoutenancesalle() {
        return soutenancesalle;
    }

    public void setSoutenancesalle(String soutenancesalle) {
        this.soutenancesalle = soutenancesalle;
    }

    public String getTitlemodule() {
        return titlemodule;
    }

    public void setTitlemodule(String titlemodule) {
        this.titlemodule = titlemodule;
    }
}
