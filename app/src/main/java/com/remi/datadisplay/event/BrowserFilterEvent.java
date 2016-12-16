package com.remi.datadisplay.event;


import java.util.ArrayList;

public class BrowserFilterEvent {
    private ArrayList<String> selectedBrowsers = new ArrayList<>();

    public BrowserFilterEvent(ArrayList<String> selectedBrowsers) {
        this.selectedBrowsers = selectedBrowsers;
    }

    public ArrayList<String> getSelectedBrowsers() {
        return selectedBrowsers;
    }

    public void setSelectedBrowsers(ArrayList<String> selectedBrowsers) {
        this.selectedBrowsers = selectedBrowsers;
    }
}
