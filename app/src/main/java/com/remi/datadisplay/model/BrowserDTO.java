package com.remi.datadisplay.model;

import java.io.Serializable;

public class BrowserDTO  implements Serializable {
    String Browser;
    String Version;
    String Platform;

    public String getBrowser() {
        return Browser;
    }

    public String getVersion() {
        return Version;
    }

    public String getPlatform() {
        return Platform;
    }
}
