package com.alienpants.leafpic.views.themeable;

import android.content.Context;

import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.alienpants.liz.ThemeHelper;
import com.alienpants.liz.Themed;

/**
 * Created by darken (darken@darken.eu) on 04.03.2017.
 */
public class ThemedCardView extends CardView implements Themed {

    public ThemedCardView(Context context) {
        this(context, null);
    }

    public ThemedCardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThemedCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void refreshTheme(ThemeHelper themeHelper) {
        setCardBackgroundColor(themeHelper.getCardBackgroundColor());
    }
}
