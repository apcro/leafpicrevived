package com.alienpants.liz.ui;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;

import com.alienpants.liz.R;
import com.alienpants.liz.ThemeHelper;
import com.alienpants.liz.Themed;

/**
 * Created by dnld on 01/03/18.
 */

public class ThemedToolbar extends Toolbar implements Themed {

    public ThemedToolbar(Context context) {
        super(context, null, R.style.ToolbarTheme);
    }

    public ThemedToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context);
    }

    public ThemedToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context);
    }

    @Override
    public void refreshTheme(ThemeHelper themeHelper) {
        setBackgroundColor(themeHelper.getPrimaryColor());
    }
}
