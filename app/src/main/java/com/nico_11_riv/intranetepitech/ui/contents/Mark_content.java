package com.nico_11_riv.intranetepitech.ui.contents;

public class Mark_content {
    private String mark;
    private String corrector;
    private String event;
    private String module;
    private String content;

    public Mark_content(String mark, String corrector, String event, String module, String content) {
        super();
        this.mark = mark;
        this.corrector = corrector;
        this.event = event;
        this.module = module;
        this.content = content;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getCorrector() {
        return corrector;
    }

    public void setCorrector(String corrector) {
        this.corrector = corrector;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
