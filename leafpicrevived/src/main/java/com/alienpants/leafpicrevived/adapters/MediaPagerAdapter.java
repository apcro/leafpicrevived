package com.alienpants.leafpicrevived.adapters;


import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.alienpants.leafpicrevived.data.Media;
import com.alienpants.leafpicrevived.fragments.GifFragment;
import com.alienpants.leafpicrevived.fragments.ImageFragment;
import com.alienpants.leafpicrevived.fragments.VideoFragment;

import java.util.ArrayList;

/**
 * Created by dnld on 18/02/16.
 */

public class MediaPagerAdapter extends FragmentStatePagerAdapter {

    private final String TAG = "asd";
    private ArrayList<Media> media;
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public MediaPagerAdapter(FragmentManager fm, ArrayList<Media> media) {
        super(fm);
        this.media = media;
    }

    @Override
    public Fragment getItem(int pos) {
        Media media = this.media.get(pos);
        if (media.isVideo()) return VideoFragment.newInstance(media);
        if (media.isGif()) return GifFragment.newInstance(media);
        else return ImageFragment.newInstance(media);
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    public void swapDataSet(ArrayList<Media> media) {
        this.media = media;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return media.size();
    }
}