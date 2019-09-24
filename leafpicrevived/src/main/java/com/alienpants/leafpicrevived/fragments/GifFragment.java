package com.alienpants.leafpicrevived.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alienpants.leafpicrevived.data.Media;

import pl.droidsonroids.gif.GifImageView;

/**
 * Media Fragment for showing an Image (static)
 */
public class GifFragment extends BaseMediaFragment {

    @NonNull
    public static GifFragment newInstance(@NonNull Media media) {
        return BaseMediaFragment.newInstance(new GifFragment(), media);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        GifImageView photoView = new GifImageView(getContext());
        photoView.setImageURI(media.getUri());
        setTapListener(photoView);
        return photoView;
    }
}
