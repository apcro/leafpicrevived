package com.alienpants.leafpicrevived.util.preferences;

import com.alienpants.leafpicrevived.CardViewStyle;
import com.alienpants.leafpicrevived.data.sort.SortingMode;
import com.alienpants.leafpicrevived.data.sort.SortingOrder;

/**
 * Class for storing Preference default values.
 */
public final class Defaults {

    public static final int TIMELINE_ITEMS_PORTRAIT = 4;
    public static final int TIMELINE_ITEMS_LANDSCAPE = 5;
    static final int FOLDER_COLUMNS_PORTRAIT = 2;
    static final int FOLDER_COLUMNS_LANDSCAPE = 3;
    static final int MEDIA_COLUMNS_PORTRAIT = 3;
    static final int MEDIA_COLUMNS_LANDSCAPE = 4;
    static final int ALBUM_SORTING_MODE = SortingMode.DATE.getValue();
    static final int ALBUM_SORTING_ORDER = SortingOrder.DESCENDING.getValue();
    static final int CARD_STYLE = CardViewStyle.MATERIAL.getValue();
    static final boolean SHOW_VIDEOS = true;
    static final boolean SHOW_MEDIA_COUNT = true;
    static final boolean SHOW_ALBUM_PATH = false;
    static final int LAST_VERSION_CODE = 0;
    static final boolean FORCE_ENGLISH = false;
    static final boolean SHOW_EASTER_EGG = false;
    static final boolean ANIMATIONS_DISABLED = false;
    static final boolean TIMELINE_ENABLED = false;
    static final boolean LOOP_VIDEO = false;

    // Prevent class instantiation
    private Defaults() {
    }
}
