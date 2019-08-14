package com.alienpants.leafpic.interfaces;

import com.alienpants.leafpic.data.Album;
import com.alienpants.leafpic.data.Media;

import java.util.ArrayList;

public interface MediaClickListener {

    void onMediaClick(Album album, ArrayList<Media> media, int position);
}
