package com.alienpants.leafpicrevived.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.alienpants.leafpicrevived.R;
import com.alienpants.leafpicrevived.data.Media;
import com.alienpants.leafpicrevived.data.StorageHelper;
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial;

import org.horaapps.liz.ThemeHelper;
import org.horaapps.liz.ui.ThemedIcon;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A Media Fragment for showing a Video Preview.
 */
public class VideoFragment extends BaseMediaFragment {

    @BindView(R.id.media_view) ImageView previewView;
//    @BindView(R.id.video_play_icon) ThemedIcon playVideoIcon;

    private ThemedIcon mPlayVideoIcon;

    @NonNull
    public static VideoFragment newInstance(@NonNull Media media) {
        return BaseMediaFragment.newInstance(new VideoFragment(), media);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, rootView);
        mPlayVideoIcon = rootView.findViewById(R.id.video_play_icon);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPlayVideoIcon.setOnClickListener(v -> {
            Uri uri = StorageHelper.getUriForFile(getContext(), media.getFile());
            Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(uri, media.getMimeType());
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        });

        // TODO: See where we can move this. Seems like boilerplate code that belongs in
        // a utility class or Builder of some sort.
        RequestOptions options =
                new RequestOptions().signature(media.getSignature()).centerCrop()
                        .diskCacheStrategy(
                                DiskCacheStrategy.AUTOMATIC);

        Glide.with(getContext()).load(media.getUri()).apply(options).into(previewView);
        setTapListener(previewView);
    }

    @Override
    public void refreshTheme(ThemeHelper themeHelper) {
//        playVideoIcon.refreshTheme(themeHelper);
    }

}