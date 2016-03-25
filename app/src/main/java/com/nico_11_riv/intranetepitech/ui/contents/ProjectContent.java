package com.nico_11_riv.intranetepitech.ui.contents;

public class ProjectContent {
    private String projectName;
    private String date;
    private String moduletitle;

    public ProjectContent(String projectName, String date, String moduletitle) {
        this.projectName = projectName;
        this.date = date;
        this.moduletitle = moduletitle;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModuletitle() {
        return moduletitle;
    }

    public void setModuletitle(String moduletitle) {
        this.moduletitle = moduletitle;
    }
}
