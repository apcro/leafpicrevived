package com.alienpants.leafpicrevived.timeline.data;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;

/**
 * Model for showing the Timeline headers.
 */
public class TimelineHeaderModel implements TimelineItem {

    private Calendar calendar;
    private String headerText;

    public TimelineHeaderModel(@NonNull Calendar calendar) {
        this.calendar = calendar;
    }

    @NonNull
    public Calendar getDate() {
        return calendar;
    }

    @Nullable
    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(@NonNull String headerText) {
        this.headerText = headerText;
    }

    @Override
    public int getTimelineType() {
        return TYPE_HEADER;
    }
}
