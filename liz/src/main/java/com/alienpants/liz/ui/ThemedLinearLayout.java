package com.alienpants.liz.ui;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.util.AttributeSet;

import com.alienpants.liz.R;
import com.alienpants.liz.ThemeHelper;
import com.alienpants.liz.Themed;


/**
 * Created by darken (darken@darken.eu) on 04.03.2017.
 */
public class ThemedLinearLayout extends LinearLayoutCompat implements Themed {

    int color;

    public ThemedLinearLayout(Context context) {
        this(context, null);
    }

    public ThemedLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThemedLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ThemedLinearLayout,
                0, 0);
        color = a.getInt(R.styleable.ThemedLinearLayout_liz_background_style, 1);
        a.recycle();
    }

    @Override
    public void refreshTheme(ThemeHelper theme) {

        switch (color) {
            case 1:
                setBackgroundColor(theme.getBackgroundColor());
                break;
            case 2:
                setBackgroundColor(theme.getCardBackgroundColor());
                break;
            case 3:
                setBackgroundColor(theme.getPrimaryColor());
                break;
            case 4:
                setBackgroundColor(theme.getAccentColor());
                break;
        }
    }
}
