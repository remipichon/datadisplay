package remi.com.datadisplay.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ReviewDTO  implements Serializable {
    BrowserDTO computed_browser;
    LocationDTO geo;
    Integer rating;
    ArrayList<String> labels;

    public BrowserDTO getComputed_browser() {
        return computed_browser;
    }

    public LocationDTO getGeo() {
        return geo;
    }

    public Integer getRating() {
        return rating;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }
}
