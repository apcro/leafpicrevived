package com.alienpants.leafpicrevived.util.preferences;

import com.alienpants.leafpicrevived.CardViewStyle;
import com.alienpants.leafpicrevived.data.sort.SortingMode;
import com.alienpants.leafpicrevived.data.sort.SortingOrder;

/**
 * Class for storing Preference default values.
 */
public final class Defaults {

    public static final int FOLDER_COLUMNS_PORTRAIT = 2;
    public static final int FOLDER_COLUMNS_LANDSCAPE = 3;
    public static final int MEDIA_COLUMNS_PORTRAIT = 3;
    public static final int MEDIA_COLUMNS_LANDSCAPE = 4;
    public static final int TIMELINE_ITEMS_PORTRAIT = 4;
    public static final int TIMELINE_ITEMS_LANDSCAPE = 5;
    public static final int ALBUM_SORTING_MODE = SortingMode.DATE.getValue();
    public static final int ALBUM_SORTING_ORDER = SortingOrder.DESCENDING.getValue();
    public static final int CARD_STYLE = CardViewStyle.MATERIAL.getValue();
    public static final boolean SHOW_VIDEOS = true;
    public static final boolean SHOW_MEDIA_COUNT = true;
    public static final boolean SHOW_ALBUM_PATH = false;
    public static final int LAST_VERSION_CODE = 0;
    public static final boolean FORCE_ENGLISH = false;
    public static final boolean SHOW_EASTER_EGG = false;
    public static final boolean ANIMATIONS_DISABLED = false;
    public static final boolean TIMELINE_ENABLED = false;
    public static final boolean LOOP_VIDEO = false;

    // Prevent class instantiation
    private Defaults() {
    }
}
