package com.alienpants.leafpicrevived.views.themeable;

import android.content.Context;

import android.util.AttributeSet;

import androidx.annotation.Nullable;

import org.horaapps.liz.ThemeHelper;
import org.horaapps.liz.Themed;
import org.horaapps.liz.ui.ThemedIcon;

/**
 * Created by darken (darken@darken.eu) on 04.03.2017.
 */
public class ThemedSettingsIcon extends ThemedIcon implements Themed {

    public ThemedSettingsIcon(Context context) {
        this(context, null);
    }

    public ThemedSettingsIcon(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThemedSettingsIcon(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void refreshTheme(ThemeHelper themeHelper) {
        setColor(themeHelper.getIconColor());
    }
}
