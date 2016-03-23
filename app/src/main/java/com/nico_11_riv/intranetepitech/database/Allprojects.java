package com.nico_11_riv.intranetepitech.database;

import com.orm.SugarRecord;

/**
 *
 * Created by Jimmy on 10/02/2016.
 *
 */
public class Allprojects extends SugarRecord {
    private String login;
    private String codemodule;
    private String project;
    private String endacti;
    private String actititle;
    private String titlemodule;
    private String beginacti;
    private String scolaryear;
    private String codeacti;
    private String registered;
    private String codeinstance;
    private String typeacty;

    public Allprojects() {
    }

    public Allprojects(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCodemodule() {
        return codemodule;
    }

    public void setCodemodule(String codemodule) {
        this.codemodule = codemodule;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getEndacti() {
        return endacti;
    }

    public void setEndacti(String endacti) {
        this.endacti = endacti;
    }

    public String getActititle() {
        return actititle;
    }

    public void setActititle(String actititle) {
        this.actititle = actititle;
    }

    public String getTitlemodule() {
        return titlemodule;
    }

    public void setTitlemodule(String titlemodule) {
        this.titlemodule = titlemodule;
    }

    public String getBeginacti() {
        return beginacti;
    }

    public void setBeginacti(String beginacti) {
        this.beginacti = beginacti;
    }

    public String getScolaryear() {
        return scolaryear;
    }

    public void setScolaryear(String scolaryear) {
        this.scolaryear = scolaryear;
    }

    public String getCodeacti() {
        return codeacti;
    }

    public void setCodeacti(String codeacti) {
        this.codeacti = codeacti;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getCodeinstance() {
        return codeinstance;
    }

    public void setCodeinstance(String codeinstance) {
        this.codeinstance = codeinstance;
    }

    public String getTypeacty() {
        return typeacty;
    }

    public void setTypeacty(String typeacty) {
        this.typeacty = typeacty;
    }
}
