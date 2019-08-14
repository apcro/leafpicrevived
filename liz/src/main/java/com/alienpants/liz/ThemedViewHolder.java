package com.alienpants.liz;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dnld on 08/04/17.
 */

public abstract class ThemedViewHolder extends RecyclerView.ViewHolder implements Themed {

    public ThemedViewHolder(View view) {
        super(view);
    }

    @Override
    public void refreshTheme(ThemeHelper themeHelper) {
        for (View view : ViewUtil.getAllChildren(itemView)) {
            if (view instanceof Themed) ((Themed) view).refreshTheme(themeHelper);
        }
    }
}