package com.alienpants.leafpicrevived.about;

/**
 * Created by dnld on 04/03/18.
 */

public class Contact {
    private String value;
    private String label;

    Contact(String value, String label) {
        this.value = value;
        this.label = label;
    }

    String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
