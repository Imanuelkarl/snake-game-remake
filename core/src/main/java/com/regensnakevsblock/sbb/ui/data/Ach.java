package com.regensnakevsblock.sbb.ui.data;

public class Ach {
    private String title;
    private boolean displayed;
    private String id;

    public Ach(String title, boolean displayed, String id) {
        this.title = title;
        this.displayed = displayed;
        this.id = id;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDisplayed() {
        return displayed;
    }

    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}
