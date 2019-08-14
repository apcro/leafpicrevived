package com.alienpants.liz;

import android.content.Context;

import androidx.fragment.app.DialogFragment;

/**
 * Created by dnld on 9/9/17.
 */

public class ThemedDialogFragment extends DialogFragment {
    ThemeHelper themeHelper;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof ThemedActivity)
            themeHelper = ((ThemedActivity) context).getThemeHelper();*/
        themeHelper = ThemeHelper.getInstance(getContext());
    }

    public ThemeHelper getThemeHelper() {
        return themeHelper;
    }

    public int getPrimaryColor() {
        return themeHelper.getPrimaryColor();
    }

    public int getDialogStyle() {
        return themeHelper.getDialogStyle();
    }

    public int getAccentColor() {
        return themeHelper.getAccentColor();
    }

    public Theme getBaseTheme() {
        return themeHelper.getBaseTheme();
    }

    public int getBackgroundColor() {
        return themeHelper.getBackgroundColor();
    }

    public int getCardBackgroundColor() {
        return themeHelper.getCardBackgroundColor();
    }

    public int getIconColor() {
        return themeHelper.getIconColor();
    }

    public int getTextColor() {
        return themeHelper.getTextColor();
    }

    @Override
    public int getTheme() {
        return getDialogStyle();
    }
}
