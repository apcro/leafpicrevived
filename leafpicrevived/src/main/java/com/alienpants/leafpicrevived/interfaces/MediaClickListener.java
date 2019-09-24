package com.alienpants.leafpicrevived.interfaces;

import com.alienpants.leafpicrevived.data.Album;
import com.alienpants.leafpicrevived.data.Media;

import java.util.ArrayList;

public interface MediaClickListener {

    void onMediaClick(Album album, ArrayList<Media> media, int position);
}
