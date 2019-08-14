package com.alienpants.liz.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.alienpants.liz.ThemeHelper;
import com.alienpants.liz.Themed;


/**
 * Created by dnld on 04.03.2017.
 */
public class ThemedEditText extends androidx.appcompat.widget.AppCompatEditText implements Themed {


    public ThemedEditText(Context context) {
        this(context, null);
    }

    public ThemedEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThemedEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //setBackgroundResource(android.R.color.transparent);
    }

    @Override
    public void refreshTheme(ThemeHelper theme) {
        setTextColor(theme.getTextColor());
        setHintTextColor(theme.getSubTextColor());
        setHighlightColor(theme.getPrimaryColor());
        //ThemeHelper.setCursorColor(this, themeViews.getAccentColor());
        //setBackgroundTintList(themeViews.getTintList());
        //setBaseColor(themeViews.getAccentColor());
        //setUnderlineColor(themeViews.getAccentColor());
    }
}
